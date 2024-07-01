package com.dummyapi.tests;

import com.dummyapi.TestBase;
import com.dummyapi.endpoints.BlogPostsEndpoint;
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


public class BlogPostsTest extends TestBase {

final String blogPostUrl = BlogPostsEndpoint.BLOG_POSTS;

private JSONObject getTestData() throws IOException {
    String content = new String(Files.readAllBytes(Paths.get("src/test/resources/testdata/blogposts.json")));
    return new JSONObject(content);
}

@Test
public void testGetAllBlogPosts() {
    TestReporter.logStep("request all blogs");
    Response response = given()
            .spec(requestSpec)
            .when()
            .get(blogPostUrl)
            .then()
            .statusCode(200)
            .time(lessThan(2000L))
            .body("$", hasSize(greaterThan(0)))
            .body("[0]", allOf(
            hasKey("id"),
            hasKey("title"),
            hasKey("author"),
            hasKey("date_published"),
            hasKey("content")
            ))
            .extract().response();
    TestReporter.logStep("assert blogPost data structure");       
    assertEquals(response.getContentType(), "application/json");
    assertTrue(response.getBody().asString().contains("id"));
    assertTrue(response.getBody().asString().contains("title"));
    assertTrue(response.getBody().asString().contains("author"));
    assertTrue(response.getBody().asString().contains("date_published"));
    assertTrue(response.getBody().asString().contains("content"));
}

@Test
public void testGetExistingSingleBlog() throws IOException {
    JSONObject testData = getTestData();
    JSONObject existingBlogPost = testData.getJSONObject("existingBlogPost");

    TestReporter.logStep("Get the already existing BlogPostID"); 
    int existingBlogPostId = existingBlogPost.getInt("id");

    TestReporter.logStep("Get the existing BlogPost"); 
    Response getResponse = given()
            .spec(requestSpec)
            .header("Content-Type", "application/json")
            .when()
            .get(blogPostUrl + "/" + existingBlogPostId)
            .then()
            .statusCode(200)
            .time(lessThan(5000L))
            .extract().response();

    TestReporter.logStep("assert blogPost data structure");  
    assertEquals(getResponse.getContentType(), "application/json");
    JSONObject existingBlogResponse = new JSONObject(getResponse.getBody().asString());
    assertEquals(existingBlogResponse.getInt("id"), existingBlogPost.getInt("id"));
    assertEquals(existingBlogResponse.getString("title"), existingBlogPost.getString("title"));
    assertEquals(existingBlogResponse.getString("author"), existingBlogPost.getString("author"));
    assertEquals(existingBlogResponse.getString("date_published"), existingBlogPost.getString("date_published"));
    assertEquals(existingBlogResponse.getString("content"), existingBlogPost.getString("content"));
}

@Test
public void testGetNonExistingBlog() throws IOException {
    JSONObject testData = getTestData();
    JSONObject nonExistingBlogPost = testData.getJSONObject("nonExistingBlogPost");

    TestReporter.logStep("Get the non existing BlogPostID "); 
    int nonExistingBlogPostId = nonExistingBlogPost.getInt("id");

    Response getResponse = given()
                    .spec(requestSpec)
                    .header("Content-Type", "application/json")
                    .when()
                    .get(blogPostUrl + "/" + nonExistingBlogPostId)
                    .then()
                    .statusCode(404)
                    .time(lessThan(5000L))
                    .extract().response();
        
            TestReporter.logStep("Assert Expected Response Message"); 
            assertEquals(getResponse.getContentType(), "application/json");
            JSONObject fetchNonExistingBlogPost = new JSONObject(getResponse.getBody().asString());
            String expectedMessage = String.format("{\"message\":\"Post ID %d not found!\"}", nonExistingBlogPostId);
            assertEquals(fetchNonExistingBlogPost.toString(), expectedMessage);
}  
}        