package com.sparta.tech243.user;

import com.sparta.tech243.ApiConfig;
import com.sparta.tech243.user.pojos.User;
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
import java.util.Map;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;

public class UserDeleteTests {

    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getUserBasePath();
    private User user;
    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        //#############################Create a Test User########################################
        User user =
                given(getRequestSpecification()
                        .setBasePath(BASE_PATH)
                        .setBody("""
                        {
                          "id": 10,
                          "username": "theUser",
                          "firstName": "John",
                          "lastName": "James",
                          "email": "john@email.com",
                          "password": "12345",
                          "phone": "12345",
                          "userStatus": 1
                        }
                        """)
                        .setContentType("application/json")
                        .build())
                        .when()
                        //.log().all()
                        .post()
                        .then()
                        //.log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .as(User.class);
        this.username = user.getUsername();
        this.password = user.getPassword();

    }
    @Test
    @DisplayName("Check delete user request removes an existing user from the system")
    void checkDeleteUserRequestRemovesExistingUser() {

        Response response =
                given(
                        getRequestSpecification()
                                .setBasePath(BASE_PATH + "/login?{username}&{password}")
                                .addPathParams("username", username, "password", password)
                                .build()
                )
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .response();

        Response responseDeletion =
                    given(getRequestSpecification()
                        .setBasePath(BASE_PATH + "/user/{username}")
                        .addPathParam("username", username)
                        .build())
                        .when()
                        .delete()
                        .then()
                        .spec(getResponseStatus(404))
                            .extract()
                            .response();

        Response responseCheck =
                given(
                        getRequestSpecification()
                                .setBasePath(BASE_PATH + "/login?{username}&{password}")
                                .addPathParams("username", username, "password", password)
                                .build()
                )
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(400))
                        .extract()
                        .response();

        MatcherAssert.assertThat(responseCheck.statusCode(), Matchers.is(400));

    }

    private ResponseSpecification getJsonResponseWithStatus(Integer status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification getResponseStatus(Integer status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }


    private RequestSpecBuilder getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH);
    }












}
