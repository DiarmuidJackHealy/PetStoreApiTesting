package com.sparta.tech243.user;

import com.sparta.tech243.ApiConfig;
import com.sparta.tech243.user.pojo.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserPostTests {
    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getUserBasePath();

    @BeforeAll
    static void setUp() {
        given(getRequestSpec()
                .setBasePath(BASE_PATH)
                .setBody("""
                        {
                          "id": 6,
                          "username": "theUser6",
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
                .spec(getJsonResponseWithStatus(200));
    }

    @AfterAll
    static void tearDown() {
        for (int i = 0; i < 8; i++) {
                    given(getRequestSpec()
                            .setBasePath(BASE_PATH + "/theUser" + i)
                            .addParam("/theUser" + i)
                            .build())
                            .when()
                            .delete()
                            .then()
                            .log().all()
                            .spec(getResponseWithStatus(200));

        }

    }

    @Test
    @DisplayName("Create employee post request returns created employee")
    void createEmployeePostRequest_ReturnsCreatedEmployee() {
        User user =
            given(getRequestSpec()
                .setBasePath(BASE_PATH)
                .setBody("""
                        {
                          "id": 6,
                          "username": "theUser6",
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
        MatcherAssert.assertThat(user.getId(), Matchers.is(6));
    }

    @Test
    @DisplayName("Create employee with list post request returns created employees")
    void createEmployeeBulkPostRequest_ReturnsCreatedEmployees() {
        User[] user =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/createWithList")
                        .setBody("""
                        [
                           {
                            "id": 0,
                            "username": "theUser0",
                            "firstName": "Eve",
                            "lastName": "Burton",
                            "email": "eve@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          },
                          {
                            "id": 1,
                            "username": "theUser1",
                            "firstName": "John",
                            "lastName": "James",
                            "email": "john@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          },
                          {
                            "id": 2,
                            "username": "theUser2",
                            "firstName": "Tom",
                            "lastName": "Allen",
                            "email": "Tom@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          },
                          {
                            "id": 3,
                            "username": "theUser3",
                            "firstName": "Aston",
                            "lastName": "Presley",
                            "email": "Aston@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          },
                          {
                            "id": 4,
                            "username": "theUser4",
                            "firstName": "John",
                            "lastName": "Herbert",
                            "email": "john@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          },
                          {
                            "id": 5,
                            "username": "theUser5",
                            "firstName": "Adam",
                            "lastName": "Alrasi",
                            "email": "Adam@email.com",
                            "password": "12345",
                            "phone": "12345",
                            "userStatus": 1
                          }
                        ]
                        """)
                        .setContentType(ContentType.JSON)
                        .build())
                        .when()
                        .post()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .as(User[].class);
        int employeeID = 0;
        for (User currUser: user) {
            MatcherAssert.assertThat(currUser.getId(), Matchers.is(employeeID));
            employeeID++;
        }

    }

    @Test
    @DisplayName("Created user get request returns created employee")
    void createdUserGetRequest_ReturnsCreatedEmployee() {
        User user =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/theUser")
                        .build())
                        .when()
                        .get()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .as(User.class);
        MatcherAssert.assertThat(user.getId(), Matchers.is(10));
    }

    @Test
    @DisplayName("Create employee Put request returns updated created employee")
    void createEmployeePutRequest_ReturnsUpdatedCreatedEmployee() {
        User user =
                given(getRequestSpec()
                        .setBasePath(BASE_PATH + "/theUser6")
                        .setBody("""
                        {
                          "id": 7,
                          "username": "theUser7",
                          "firstName": "Tom",
                          "lastName": "Pearson",
                          "email": "Tom@email.com",
                          "password": "12345",
                          "phone": "12345",
                          "userStatus": 1
                        }
                        """)
                        .setContentType("application/json")
                        .build())
                        .when()
                        .put()
                        .then()
                        .log().all()
                        .spec(getJsonResponseWithStatus(200))
                        .extract()
                        .as(User.class);
        MatcherAssert.assertThat(user.getId(), Matchers.is(7));
    }

    private static ResponseSpecification getJsonResponseWithStatus(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .expectContentType(ContentType.JSON)
                .build();
    }

    private static RequestSpecBuilder getRequestSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(BASE_URI);
    }

    private static ResponseSpecification getResponseWithStatus(int status) {
        return new ResponseSpecBuilder()
                .expectStatusCode(status)
                .build();
    }

    //    @ParameterizedTest
//    @ValueSource(ints = {0,1,2,3,4,5})
//    @DisplayName("Created users bulk get request returns created employees")
//    void createdUserBulkGetRequest_ReturnsCreatedEmployees(int employeeID) {
//        User user =
//                given(getRequestSpec()
//                        .setBasePath(BASE_PATH + "/theUser"+ employeeID)
//                        .build())
//                        .when()
//                        .get()
//                        .then()
//                        .log().all()
//                        .spec(getJsonResponseWithStatus(200))
//                        .extract()
//                        .as(User.class);
//        MatcherAssert.assertThat(user.getId(), Matchers.is(employeeID));
//    }
}
