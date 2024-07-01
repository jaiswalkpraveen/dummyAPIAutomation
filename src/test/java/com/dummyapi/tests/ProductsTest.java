package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.ProductsEndpoint;
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

public class ProductsTest extends TestBase {

    final String ProductUrl = ProductsEndpoint.PRODUCTS;

    private JSONObject getTestData() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/testdata/products.json")));
        return new JSONObject(content);
    }

    @Test
    public void testGetAllProducts() {
        Response response = given()
            .spec(requestSpec)
            .when()
            .get(ProductUrl) 
            .then()
            .statusCode(200)
            .time(lessThan(2000L))
            .body("$", hasSize(greaterThan(0)))
            .body("[0]", allOf(
                hasKey("id"),
                hasKey("productCategory"),
                hasKey("name"),
                hasKey("brand"),
                hasKey("description"),
                hasKey("basePrice"),
                hasKey("inStock"),
                hasKey("stock"),
                hasKey("featuredImage"),
                hasKey("thumbnailImage"),
                hasKey("storageOptions"),
                hasKey("display"),
                hasKey("CPU"),
                hasKey("camera"),
                hasKey("colorOptions")
            ))
            .body("[0].camera", allOf(
                    hasKey("rearCamera"),
                    hasKey("frontCamera")
                ))
            .extract().response();

        assertEquals(response.getContentType(), "application/json");
        assertTrue(response.getBody().asString().contains("id"));
        assertTrue(response.getBody().asString().contains("productCategory"));
        assertTrue(response.getBody().asString().contains("name"));
        assertTrue(response.getBody().asString().contains("brand"));
        assertTrue(response.getBody().asString().contains("description"));
        assertTrue(response.getBody().asString().contains("basePrice"));
        assertTrue(response.getBody().asString().contains("inStock"));
        assertTrue(response.getBody().asString().contains("stock"));
        assertTrue(response.getBody().asString().contains("featuredImage"));
        assertTrue(response.getBody().asString().contains("thumbnailImage"));
        assertTrue(response.getBody().asString().contains("storageOptions"));
        assertTrue(response.getBody().asString().contains("display"));
        assertTrue(response.getBody().asString().contains("CPU"));
        assertTrue(response.getBody().asString().contains("camera"));
        assertTrue(response.getBody().asString().contains("colorOptions"));
        assertTrue(response.getBody().asString().contains("camera"));

    }

        @Test
            public void testGetExistingProduct() throws IOException {
            JSONObject testData = getTestData();
            JSONObject existingProduct = testData.getJSONObject("existingProduct");
    
            // Get the already existing MovieID 
            int existingProductId = existingProduct.getInt("id");
    
            // Get the existing Movie
            Response getResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .when()
                .get(ProductUrl + "/" + existingProductId)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                
                .extract().response();
        
            // Assertions based on Product data structure
            assertEquals(getResponse.getContentType(), "application/json");
            JSONObject existingMovieResponse = new JSONObject(getResponse.getBody().asString());
            assertEquals(existingMovieResponse.getInt("id"), existingProduct.getInt("id"));
            assertEquals(existingMovieResponse.getString("productCategory"), existingProduct.getString("productCategory"));
            assertEquals(existingMovieResponse.getString("name"), existingProduct.getString("name"));
            assertEquals(existingMovieResponse.getString("brand"), existingProduct.getString("brand"));
            assertEquals(existingMovieResponse.getString("description"), existingProduct.getString("description"));
            assertEquals(existingMovieResponse.getInt("basePrice"), existingProduct.getInt("basePrice"));
            assertEquals(existingMovieResponse.getBoolean("inStock"), existingProduct.getBoolean("inStock"));
            assertEquals(existingMovieResponse.getInt("stock"), existingProduct.getInt("stock"));
            assertEquals(existingMovieResponse.getString("featuredImage"), existingProduct.getString("featuredImage"));
            assertEquals(existingMovieResponse.getString("thumbnailImage"), existingProduct.getString("thumbnailImage"));
            assertEquals(existingMovieResponse.getString("display"), existingProduct.getString("display"));
            assertEquals(existingMovieResponse.getString("CPU"), existingProduct.getString("CPU"));
            assertEquals(existingMovieResponse.getJSONArray("storageOptions").length(), existingProduct.getJSONArray("storageOptions").length());
            assertEquals(existingMovieResponse.getJSONArray("colorOptions").length(), existingProduct.getJSONArray("colorOptions").length());
            assertEquals(existingMovieResponse.getJSONObject("camera").length(), existingProduct.getJSONObject("camera").length());
        }

                @Test
        public void testGetNonExistingProduct() throws IOException {
                    JSONObject testData = getTestData();
                    JSONObject nonExistingProduct = testData.getJSONObject("nonExistingProduct");
            
                    // Get the non existing MovieID
                    int nonExistingProductId = nonExistingProduct.getInt("id");
            
                    Response getResponse = given()
                                    .spec(requestSpec)
                                    .header("Content-Type", "application/json")
                                    .when()
                                    .get(ProductUrl + "/" + nonExistingProductId)
                                    .then()
                                    .statusCode(404)
                                    .time(lessThan(5000L))
                                    .extract().response();
                        
                            // Assert Expected Response Message
                            assertEquals(getResponse.getContentType(), "application/json");
                            JSONObject fetchNonExistingMovie = new JSONObject(getResponse.getBody().asString());
                            String expectedMessage = String.format("{\"message\":\"Product ID %d not found!\"}", nonExistingProductId);
                            assertEquals(fetchNonExistingMovie.toString(), expectedMessage);
            } 
}