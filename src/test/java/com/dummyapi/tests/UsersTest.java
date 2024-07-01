package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.UsersEndpoint;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.testng.Assert.*;

public class UsersTest extends TestBase {
        final String usersUrl = UsersEndpoint.USERS;

        private JSONObject getTestData() throws IOException {
                String content = new String(Files.readAllBytes(Paths.get("src/test/resources/testdata/users.json")));
                return new JSONObject(content);
        }

        private int generateUniqueId() {
                return Math.abs(UUID.randomUUID().hashCode());
        }


         @Test
    public void testGetAllUsers() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(usersUrl)
                .then()
                .statusCode(200)
                .time(lessThan(2000L))
                .body("$", hasSize(greaterThan(0)))
                .body("[0]", allOf(
                    hasKey("id"),
                    hasKey("name"),
                    hasKey("username"),
                    hasKey("email"),
                    hasKey("address")
                ))
                .body("[0].address", allOf(
                    hasKey("street"),
                    hasKey("city"),
                    hasKey("state"),
                    hasKey("zipcode")
                ))
                .extract().response();
    
                // assert user data structure
        assertEquals(response.getContentType(), "application/json");
        assertTrue(response.getBody().asString().contains("name"));
        assertTrue(response.getBody().asString().contains("username"));
        assertTrue(response.getBody().asString().contains("email"));
        assertTrue(response.getBody().asString().contains("address"));
    }


     @Test
    public void testGetSingleUser() throws IOException {
        JSONObject testData = getTestData();
        JSONObject existingUser = testData.getJSONObject("existingUser");

        // Get the already existing userID  
        int existingUserId = existingUser.getInt("id");

        // Get the existing user
        Response getResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .when()
                .get(usersUrl + "/" + existingUserId)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .extract().response();
    
        // assert user data structure
        assertEquals(getResponse.getContentType(), "application/json");
        JSONObject createdUser = new JSONObject(getResponse.getBody().asString());
        assertEquals(createdUser.getString("name"), existingUser.getString("name"));
        assertEquals(createdUser.getString("username"), existingUser.getString("username"));
        assertEquals(createdUser.getString("email"), existingUser.getString("email"));
    }

    @Test
    public void testGetNonExistingUser() throws IOException {
        JSONObject testData = getTestData();
        JSONObject nonExistingUser = testData.getJSONObject("nonexistingUser");

        // Get the non existing userID
        int nonExistingUserId = nonExistingUser.getInt("id");

        Response getResponse = given()
                        .spec(requestSpec)
                        .header("Content-Type", "application/json")
                        .when()
                        .get(usersUrl + "/" + nonExistingUserId)
                        .then()
                        .statusCode(404)
                        .time(lessThan(5000L))
                        .extract().response();
            
                // // Assert Expected Response Message
                assertEquals(getResponse.getContentType(), "application/json");
                JSONObject fetchNonExistingUser = new JSONObject(getResponse.getBody().asString());
                String expectedMessage = String.format("{\"message\":\"User ID %d not found!\"}", nonExistingUserId);
                assertEquals(fetchNonExistingUser.toString(), expectedMessage);
}  

     @Test
    public void testCreateAndGetUser() throws IOException {
        JSONObject testData = getTestData();
        JSONObject validUser = testData.getJSONObject("validUser");

        // Generate a unique ID 
        int uniqueId = generateUniqueId();
        validUser.put("id", uniqueId); // Add the generated ID to the user data


        // Create user
        Response createResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .body(validUser.toString())
                .when()
                .post(usersUrl)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .body("message", equalTo("User created successfully:"))
                .extract().response();

        assertEquals(createResponse.getContentType(), "application/json");
        JSONObject createdUser = new JSONObject(createResponse.getBody().asString());
        assertEquals(createdUser.getString("name"), validUser.getString("name"));
        assertEquals(createdUser.getString("username"), validUser.getString("username"));
        assertEquals(createdUser.getString("email"), validUser.getString("email"));
    
        // Extract created user ID
        int createdUserId = createResponse.jsonPath().getInt("id");

        // Get the created user
        Response getResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .when()
                .get(usersUrl + "/" + createdUserId)
                .then()
                .statusCode(404)
                .time(lessThan(5000L))
                .extract().response();
    
        // Assert Expected Response Message
        assertEquals(getResponse.getContentType(), "application/json");
        JSONObject fetchCreatedUser = new JSONObject(getResponse.getBody().asString());
        String expectedMessage = String.format("{\"message\":\"User ID %d not found!\"}", createdUserId);
        assertEquals(fetchCreatedUser.toString(), expectedMessage);
    }


    @Test
    public void testCreateUserWithExistingID() throws IOException {
        JSONObject testData = getTestData();
        JSONObject validUser = testData.getJSONObject("validUser");
        JSONObject existingUser = testData.getJSONObject("existingUser");

        int existingId = existingUser.getInt("id");
        validUser.put("id", existingId);

        // Create user with alreday existing ID 1
        Response createResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .body(validUser.toString())
                .when()
                .post(usersUrl)
                .then()
                .statusCode(400)
                .time(lessThan(5000L))
                .extract().response();
    
        // Assert Expected Response Message
        assertEquals(createResponse.getContentType(), "application/json");
        JSONObject fetchExistingUserResponse = new JSONObject(createResponse.getBody().asString());
        String expectedMessage = String.format("{\"message\":\"The user with the id %d already exists\"}", existingId);
        assertEquals(fetchExistingUserResponse.toString(), expectedMessage);
    }
}