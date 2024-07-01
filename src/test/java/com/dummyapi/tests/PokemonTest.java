package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.PokemonEndpoint;
import io.restassured.response.Response;

import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.testng.Assert.*;

public class PokemonTest extends TestBase {

    final String PokemonUrl = PokemonEndpoint.POKEMON;

    private JSONObject getTestData() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/testdata/pokemon.json")));
        return new JSONObject(content);
    }

       @Test
    public void testGetAllPokMon() {
        Response response = given()
            .spec(requestSpec)
            .when()
            .get(PokemonUrl) 
            .then()
            .statusCode(200)
            .time(lessThan(2000L))
            .body("$", hasSize(greaterThan(0)))
            .body("[0]", allOf(
                hasKey("id"),
                hasKey("pokemon"),
                hasKey("type"),
                hasKey("abilities"),
                hasKey("hitpoints"),
                hasKey("evolutions"),
                hasKey("location"),
                hasKey("image_url")
            ))
            .extract().response();

            // assert PokMon data structure
        assertEquals(response.getContentType(), "application/json");
        assertTrue(response.getBody().asString().contains("id"));
        assertTrue(response.getBody().asString().contains("pokemon"));
        assertTrue(response.getBody().asString().contains("type"));
        assertTrue(response.getBody().asString().contains("hitpoints"));
        assertTrue(response.getBody().asString().contains("location"));
        assertTrue(response.getBody().asString().contains("image_url"));

    }

    @Test
            public void testGetExistingSinglePokMon() throws IOException {
            JSONObject testData = getTestData();
            JSONObject existingPokMon = testData.getJSONObject("existingPokeMon");
    
            // Get the already existing MovieID 
            int existingPokMonId = existingPokMon.getInt("id");
    
            // Get the existing Movie
            Response getResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .when()
                .get(PokemonUrl + "/" + existingPokMonId)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))

                .extract().response();
        
                           // Assertions based on Pokemon data structure
            assertEquals(getResponse.getContentType(), "application/json");
            JSONObject existingMovieResponse = new JSONObject(getResponse.getBody().asString());
            assertEquals(existingMovieResponse.getInt("id"), existingPokMon.getInt("id"));
            assertEquals(existingMovieResponse.getString("pokemon"), existingPokMon.getString("pokemon"));
            assertEquals(existingMovieResponse.getString("type"), existingPokMon.getString("type"));
            assertEquals(existingMovieResponse.getInt("hitpoints"), existingPokMon.getInt("hitpoints"));
            assertEquals(existingMovieResponse.getString("location"), existingPokMon.getString("location"));
            assertEquals(existingMovieResponse.getString("image_url"), existingPokMon.getString("image_url"));
            assertEquals(existingMovieResponse.getJSONArray("evolutions").length(), existingPokMon.getJSONArray("evolutions").length());
            assertEquals(existingMovieResponse.getJSONArray("abilities").length(), existingPokMon.getJSONArray("abilities").length());
        }

        @Test
        public void testGetNonExistingPokeMon() throws IOException {
                    JSONObject testData = getTestData();
                    JSONObject nonExistingPokeMon = testData.getJSONObject("nonExistingPokeMon");
            
                    // Get the non existing MovieID
                    int nonExistingPokeMonId = nonExistingPokeMon.getInt("id");
            
                    Response getResponse = given()
                                    .spec(requestSpec)
                                    .header("Content-Type", "application/json")
                                    .when()
                                    .get(PokemonUrl + "/" + nonExistingPokeMonId)
                                    .then()
                                    .statusCode(404)
                                    .time(lessThan(5000L))
                                    .extract().response();
                        
                            // Assert Expected Response Message
                            assertEquals(getResponse.getContentType(), "application/json");
                            JSONObject fetchNonExistingMovie = new JSONObject(getResponse.getBody().asString());
                            String expectedMessage = String.format("{\"message\":\"Pokemon ID %d not found!\"}", nonExistingPokeMonId);
                            assertEquals(fetchNonExistingMovie.toString(), expectedMessage);
            } 
}