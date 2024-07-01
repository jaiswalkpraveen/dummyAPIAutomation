package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.PokemonEndpoint;
import com.dummyapi.utils.TestReporter;

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
public void testGetAllPokemon() {
    TestReporter.logStep("Get all pokemon details"); 
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

        TestReporter.logStep("assert Pokemon data structure"); 
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
        JSONObject existingPokemon = testData.getJSONObject("existingPokemon");
    
        TestReporter.logStep("Get the already existing PokemonID"); 
        int existingPokMonId = existingPokemon.getInt("id");

        TestReporter.logStep("Get the existing pokemon"); 
        Response getResponse = given()
            .spec(requestSpec)
            .header("Content-Type", "application/json")
            .when()
            .get(PokemonUrl + "/" + existingPokMonId)
            .then()
            .statusCode(200)
            .time(lessThan(5000L))

            .extract().response();
    
                        
                        TestReporter.logStep("Assertions based on Pokemon data structure"); 
        assertEquals(getResponse.getContentType(), "application/json");
        JSONObject existingMovieResponse = new JSONObject(getResponse.getBody().asString());
        assertEquals(existingMovieResponse.getInt("id"), existingPokemon.getInt("id"));
        assertEquals(existingMovieResponse.getString("pokemon"), existingPokemon.getString("pokemon"));
        assertEquals(existingMovieResponse.getString("type"), existingPokemon.getString("type"));
        assertEquals(existingMovieResponse.getInt("hitpoints"), existingPokemon.getInt("hitpoints"));
        assertEquals(existingMovieResponse.getString("location"), existingPokemon.getString("location"));
        assertEquals(existingMovieResponse.getString("image_url"), existingPokemon.getString("image_url"));
        assertEquals(existingMovieResponse.getJSONArray("evolutions").length(), existingPokemon.getJSONArray("evolutions").length());
        assertEquals(existingMovieResponse.getJSONArray("abilities").length(), existingPokemon.getJSONArray("abilities").length());
    }

    @Test
    public void testGetNonExistingPokemon() throws IOException {
                JSONObject testData = getTestData();
                JSONObject nonExistingPokeMon = testData.getJSONObject("nonExistingPokemon");
        
                TestReporter.logStep("Get the non existing pokemonID"); 
                int nonExistingPokeMonId = nonExistingPokeMon.getInt("id");
        
                TestReporter.logStep("Get the non existing pokemon details"); 
                Response getResponse = given()
                                .spec(requestSpec)
                                .header("Content-Type", "application/json")
                                .when()
                                .get(PokemonUrl + "/" + nonExistingPokeMonId)
                                .then()
                                .statusCode(404)
                                .time(lessThan(5000L))
                                .extract().response();
                    
                        
                        TestReporter.logStep("Assert Expected Response Message"); 
                        assertEquals(getResponse.getContentType(), "application/json");
                        JSONObject fetchNonExistingMovie = new JSONObject(getResponse.getBody().asString());
                        String expectedMessage = String.format("{\"message\":\"Pokemon ID %d not found!\"}", nonExistingPokeMonId);
                        assertEquals(fetchNonExistingMovie.toString(), expectedMessage);
        } 
}