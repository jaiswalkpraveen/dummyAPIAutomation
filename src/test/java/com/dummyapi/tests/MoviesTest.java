package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.MoviesEndpoint;
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

public class MoviesTest extends TestBase {

    final String moviesUrl = MoviesEndpoint.MOVIES;

    private JSONObject getTestData() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/testdata/movies.json")));
        return new JSONObject(content);
    }

    @Test
    public void testGetAllMovies() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get(moviesUrl)
                .then()
                .statusCode(200)
                .time(lessThan(2000L))
                .body("$", hasSize(greaterThan(0)))
                .body("[0]", allOf(
                hasKey("id"),
                hasKey("movie"),
                hasKey("rating"),
                hasKey("image"),
                hasKey("imdb_url")
            ))
                .extract().response();

         // assert blogPost data structure
        assertEquals(response.getContentType(), "application/json");
        assertTrue(response.getBody().asString().contains("id"));
        assertTrue(response.getBody().asString().contains("movie"));
        assertTrue(response.getBody().asString().contains("rating"));
        assertTrue(response.getBody().asString().contains("image"));
        assertTrue(response.getBody().asString().contains("imdb_url"));
    }

    @Test
        public void testGetExistingSingleMovie() throws IOException {
        JSONObject testData = getTestData();
        JSONObject existingMovie = testData.getJSONObject("existingMovie");

        // Get the already existing MovieID 
        int existingMovieId = existingMovie.getInt("id");

        // Get the existing Movie
        Response getResponse = given()
                .spec(requestSpec)
                .header("Content-Type", "application/json")
                .when()
                .get(moviesUrl + "/" + existingMovieId)
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .extract().response();
    
        // assert blogPost data structure
        assertEquals(getResponse.getContentType(), "application/json");
        JSONObject existingMovieResponse = new JSONObject(getResponse.getBody().asString());
        assertEquals(existingMovieResponse.getInt("id"), existingMovie.getInt("id"));
        assertEquals(existingMovieResponse.getFloat("rating"), existingMovie.getFloat("rating"));
        assertEquals(existingMovieResponse.getString("imdb_url"), existingMovie.getString("imdb_url"));
        assertEquals(existingMovieResponse.getString("image"), existingMovie.getString("image"));
        assertEquals(existingMovieResponse.getString("movie"), existingMovie.getString("movie"));
    }

        @Test
    public void testGetNonExistingMovie() throws IOException {
        JSONObject testData = getTestData();
        JSONObject nonExistingMovie = testData.getJSONObject("noExistingMovie");

        // Get the non existing MovieID
        int nonExistingMovieId = nonExistingMovie.getInt("id");

        Response getResponse = given()
                        .spec(requestSpec)
                        .header("Content-Type", "application/json")
                        .when()
                        .get(moviesUrl + "/" + nonExistingMovieId)
                        .then()
                        .statusCode(404)
                        .time(lessThan(5000L))
                        .extract().response();
            
                // // Assert Expected Response Message
                assertEquals(getResponse.getContentType(), "application/json");
                JSONObject fetchNonExistingMovie = new JSONObject(getResponse.getBody().asString());
                String expectedMessage = String.format("{\"message\":\"Movie ID %d not found!\"}", nonExistingMovieId);
                assertEquals(fetchNonExistingMovie.toString(), expectedMessage);
} 

}