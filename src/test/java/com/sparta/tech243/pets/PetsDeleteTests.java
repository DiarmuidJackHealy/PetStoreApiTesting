package com.sparta.tech243.pets;

import com.sparta.tech243.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/*
HAPPY PATH
GIVEN  I submit a delete request to pet
WHEN I have a valid ID for an existing pet
THEN the pet will be deleted from the store
 */
/*
SAD PATH
GIVEN  I submit a delete request to pet
WHEN  I do not have a valid ID for an existing pet
THEN i  recive  a message from the server indicating an error
AND I will receive a 400 response code
 */

public class PetsDeleteTests {

public static final String BASE_URI = ApiConfig.getBaseUri();
public static final String BASE_PATH = ApiConfig.getBasePet();

private Integer petId;
@BeforeEach
void setUp() {
    this.petId =
            given(getRequestSpec()
                    .setBasePath(BASE_PATH)
                    .setContentType("application/json")
                    .setBody("{\n" +
                            "  \"id\": 10,\n" +
                            "  \"name\": \"test-dog\",\n" +
                            "  \"category\": {\n" +
                            "    \"id\": 1,\n" +
                            "    \"name\": \"Dogs\"\n" +
                            "  },\n" +
                            "  \"photoUrls\": [\n" +
                            "    \"string\"\n" +
                            "  ],\n" +
                            "  \"tags\": [\n" +
                            "    {\n" +
                            "      \"id\": 0,\n" +
                            "      \"name\": \"string\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"status\": \"available\"\n" +
                            "}")
                    .build())
                    .when()
                    .post()
                    .then()
                    .assertThat().statusCode(200)
                    .extract().jsonPath().get("petId");
}

@Test
@DisplayName("Check delete request with existing pet id returns 200 ok")
void checkDeleteRequestForExistingPetId() {
    Response response =
            given(getRequestSpec()
                    .setBasePath(BASE_PATH + "/{petId}")
                    .addPathParam("petId",10)
                    .build())
                    .when()
                    .log().all()
                    .delete()
                    .then()
                    .log().all()
                    .spec(getReponseStatus(200))
                    .extract()
                    .response();

    String status = response.getBody().asString();

    MatcherAssert.assertThat(status, Matchers.is("Pet deleted"));
}

@Test
@DisplayName("Check delete request with invalid pet id returns 400 status and error message")
void checkDeleteRequestForInvalidPetId() {
    Response response =
            given(getRequestSpec()
                    .setBasePath(BASE_PATH + "/{petId}")
                    .addPathParam("petId","!@*?193")
                    .build())
                    .when()
                    .log().all()
                    .delete()
                    .then()
                    .log().all()
                    .spec(getReponseStatus(400))
                    .extract()
                    .response();

    String status = response.getBody().asString();

    MatcherAssert.assertThat(status, Matchers.startsWith("{\"code\":400,\"message\":\"Input error:"));
}

private RequestSpecBuilder getRequestSpec(){
    return new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setBasePath(BASE_PATH);
}

private ResponseSpecification getReponseStatus(Integer status){
    return new ResponseSpecBuilder()
            .expectStatusCode(status)
            .build();
}
}