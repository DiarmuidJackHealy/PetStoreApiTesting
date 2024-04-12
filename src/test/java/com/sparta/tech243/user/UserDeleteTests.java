package com.sparta.tech243.user;

import com.sparta.tech243.ApiConfig;
import com.sparta.tech243.user.pojos.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
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

    @BeforeEach
    void setUp() {
        //#############################Create a Test User########################################
        this.user =
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

        //#############################Login as Test User########################################
//        this.user =
//                given(getRequestSpecification()
//                        .setBasePath(BASE_PATH + "/login")
//                        //.addPathParams("username", user.getUsername(), "password", user.getPassword())
//                        .build())
//                    .when()
//                        .log().all()
//                        .get()
//                    .then()
//                        .spec(getJsonResponseWithStatus(200))
//                        .extract()
//                        .as(User.class);
    }
    @Test
    @DisplayName("Check delete user request removes an existing user from the system")
    void checkDeleteUserRequestRemovesExistingUser() {
            given(getRequestSpecification()
                    .setBasePath(BASE_PATH + "/" + user.getUsername())
                   // .addPathParam("username", user.getUsername())
                    .build())
                    .when()
                    .delete()
                    .then()
                    .spec(getResponseStatus(204));

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectStatusCode(Matchers.not(200));
        responseSpecBuilder.expectBody(Matchers.is(emptyOrNullString()));


                given(getRequestSpecification()
                        .setBasePath(BASE_PATH + "/login")
                        //.addPathParams("username", user.getUsername(), "password", user.getPassword())
                        .build())
                    .when()
                        .get()
                    .then()
                        .spec(responseSpecBuilder.build());

        //MatcherAssert.assertThat();

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
