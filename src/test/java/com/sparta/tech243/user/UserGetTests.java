package com.sparta.tech243.user;

import com.sparta.tech243.ApiConfig;
import com.sparta.tech243.user.pojo.PatchUserRequest;
import com.sparta.tech243.user.pojo.User;
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

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserGetTests {
    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getUserBasePath();
    private String testUserName;
    private String testUserPassword;

    @BeforeEach
    void setUp() {
        User testUser =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH)
                        .setBody("""
                        {
                          "id": 100,
                          "username": "scrum",
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
                        .post()
                .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .as(User.class);

        this.testUserName = testUser.getUsername();
        this.testUserPassword = testUser.getPassword();
    }

    @Test
    @DisplayName("Get specific user from their username")
    void getSpecificUserByUserName() {
        User user =
                given(
                        getRequestSpec()
                                .setBasePath(BASE_PATH + "/{username}")
                                .addPathParam("username", testUserName)
                                .build()
                )
                .when()
                        .get()
                .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                            .as(User.class);

        MatcherAssert.assertThat(user.getUsername(), Matchers.is(testUserName));
    }

    @Test
    @DisplayName("Search for a specific user that does not exist")
    void getSpecificUserByNonExistentUserName_Returns404() {
        Response response =
                given(
                        getRequestSpec()
                                .setBasePath(BASE_PATH + "/{username}")
                                .addPathParam("username", "copernicus")
                                .build()
                )
                .when()
                        .get()
                .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(404))
                        .extract()
                        .response();

        MatcherAssert.assertThat(response.statusCode(), Matchers.is(404));
    }

    @Test
    @DisplayName("Login as a specific user")
    void getLoginUser() {
        Response response =
                given(
                        getRequestSpec()
                                .setBasePath(BASE_PATH + "/login?{username}&{password}")
                                .addPathParams("username", testUserName, "password", testUserPassword)
                                .build()
                )
                    .when()
                        .get()
                    .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .response();

        MatcherAssert.assertThat(response.statusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Login as a specific user with an incorrect username")
    void getLoginUserIncorrectUserName() {
        Response response =
                given(
                        getRequestSpec()
                                .setBasePath(BASE_PATH + "/login?{username}&{password}")
                                .addPathParams("username", "Bill", "password", testUserPassword)
                                .build()
                )
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .response();

        MatcherAssert.assertThat(response.statusCode(), Matchers.is(200));
    }

    @Test
    @DisplayName("Login as a specific user with an incorrect password")
    void getLoginUserIncorrectPassword() {
        Response response =
                given(
                        getRequestSpec()
                                .setBasePath(BASE_PATH + "/login?{username}&{password}")
                                .addPathParams("username", testUserName, "password", "Boop")
                                .build()
                )
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .response();

        MatcherAssert.assertThat(response.statusCode(), Matchers.is(200));
    }


    private ResponseSpecification getJsonResponseWithStatus(Integer status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private ResponseSpecification getResponseStatus(Integer status){
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }

    private RequestSpecBuilder getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH);
    }
}
