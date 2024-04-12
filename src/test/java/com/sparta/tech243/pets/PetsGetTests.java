package com.sparta.tech243.pets;

import com.sparta.tech243.ApiConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class PetsGetTests {
    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getBasePet();

    @Test
    @DisplayName("Get Pet by ID returns the correct order")
    void getPetByIdReturnsTheCorrectOrder() {
        Integer petID =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("id");

        assertThat(petID, Matchers.is(2));
    }

    @Test
    @DisplayName("Get Pet by ID returns the correct name")
    void getPetByIdReturnsTheCorrectName() {
        String petID =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("name");

        assertThat(petID, Matchers.is("Cat 2"));
    }

    @Test
    @DisplayName("Get Pet by ID returns the correct photo url")
    void getPetByIdReturnsTheCorrectPhotoURL() {
        Integer petID =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().getList("photoUrls").size();

        assertThat(petID, Matchers.is(2));
    }

    @Test
    @DisplayName("Get Pet by ID returns the correct amount of tags")
    void getPetByIdReturnsTheCorrectAmountOfTags() {
        Integer tags =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().getList("tags").size();

        assertThat(tags, Matchers.is(2));
    }

    @Test
    @DisplayName("Get Pet by ID returns the correct status")
    void getPetByIdReturnsTheCorrectStatus() {
        String petStatus =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id",2)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("status");

        assertThat(petStatus, Matchers.is("available"));
    }

    @Test
    @DisplayName("Get pet by incorrect ID returns 404")
    void getPetByIncorrectIDReturns404() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/{pet_id}")
                        .addPathParam("pet_id", 700)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getReponseStatus(404))
                        .extract()
                        .response();

        // Verify that the response status code is 404
        assertThat(response.getStatusCode(), Matchers.is(404));

        // Verify that the response body contains the expected message "Pet not found"
        assertThat(response.getBody().asString(), containsString("Pet not found"));
    }

    @Test
    @DisplayName("Get all available pets and count them")
    void getAllAvailablePets_CountsThem() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByStatus")
                        .addQueryParam("status", "available")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Object> pets = response.jsonPath().getList("$");
        int numberOfPets = pets.size();

        MatcherAssert.assertThat(numberOfPets, Matchers.is(7));
    }

    @Test
    @DisplayName("Get all pending pets and count them")
    void getAllPendingPets_CountsThem() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByStatus")
                        .addQueryParam("status", "pending")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Object> pets = response.jsonPath().getList("$");
        int numberOfPets = pets.size();

        MatcherAssert.assertThat(numberOfPets, Matchers.is(2));
    }

    @Test
    @DisplayName("Get all sold pets and count them")
    void getAllSoldPets_CountsThem() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByStatus")
                        .addQueryParam("status", "sold")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Object> pets = response.jsonPath().getList("$");
        int numberOfPets = pets.size();

        MatcherAssert.assertThat(numberOfPets, Matchers.is(1));
    }

    @Test
    @DisplayName("Get a 400 for invalid status")
    void getAllPets_CountsThem() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByStatus")
                        .addQueryParam("status", "av")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(400)
                        .extract()
                        .response();
        assertThat(response.getBody().asString(), containsString("not in the allowable values"));
    }

    @Test
    @DisplayName("Get all pets by a tag and return the correct value")
    void getAllPetsByATag1() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByTags")
                        .addQueryParam("tags", "tag1")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Object> pets = response.jsonPath().getList("$");
        int numberOfPets = pets.size();

        MatcherAssert.assertThat(numberOfPets, Matchers.is(3));
    }

    @Test
    @DisplayName("Get all pets by a tag and return the correct value")
    void getAllPetsByATag3() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByTags")
                        .addQueryParam("tags", "tag3")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        List<Object> pets = response.jsonPath().getList("$");
        int numberOfPets = pets.size();

        MatcherAssert.assertThat(numberOfPets, Matchers.is(7));
    }

    @Test
    @DisplayName("Get a 400 for invalid tag")
    void getA400ForInvalidTag() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/findByTags")
                        .addQueryParam("tags", "")
                        .build())
                        .when()
                        .get()
                        .then()
                        .statusCode(400)
                        .extract()
                        .response();
        assertThat(response.getBody().asString(), containsString("No tags provided. Try again"));
    }


    private RequestSpecBuilder getRequestSpec(){
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH);

    }

    private ResponseSpecification getJsonResponseWithStatus(Integer status){
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification getReponseStatus(Integer status){
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }
}
