package com.sparta.tech243.store;

import com.sparta.tech243.ApiConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.fail;

public class StorePostTests {
    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getBasePathStore();
    private Integer petId;

    @BeforeEach
    void setUp() {
        this.petId =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order")
                        .setContentType("application/json")
                        .setBody("{\n" +
                                "  \"id\": 10,\n" +
                                "  \"petId\": 198772,\n" +
                                "  \"quantity\": 7,\n" +
                                "  \"shipDate\": \"2024-04-12T08:05:00.286Z\",\n" +
                                "  \"status\": \"approved\",\n" +
                                "  \"complete\": true\n" +
                                "}")
                        .build())
                        .when()
                        .post()
                        .then()
                        .assertThat().statusCode(200)
                        .extract().jsonPath().get("petId");
    }

    @AfterEach
    void tearDown() {
        if (petId != null) {
            given(getRequestSpec()
                    .setBasePath(BASE_PATH + "/order/{order_id}")
                    .addPathParam("order_id", petId)
                    .build())
                    .when()
                    .delete()
                    .then()
                    .spec(getReponseStatus(200));
        }
    }

    @Test
    @DisplayName("Get order by ID returns the correct order")
    void getOrderByIdReturnsTheCorrectOrder() {
        Integer petID =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id", 10)
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .jsonPath().get("petId");

        MatcherAssert.assertThat(petID, Matchers.is(198772));
    }

    @Test
    @DisplayName("Testing the pet gets deleted")
    void testingThePetGetsDeleted() {
        Response response =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/order/{order_id}")
                        .addPathParam("order_id",10)
                        .build())
                        .when()
                        .delete()
                        .then()
                        .log().all()
                        .spec(getReponseStatus(200))
                        .extract()
                        .response();

        String status = response.getBody().asString();

        MatcherAssert.assertThat(status, Matchers.is(""));
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
