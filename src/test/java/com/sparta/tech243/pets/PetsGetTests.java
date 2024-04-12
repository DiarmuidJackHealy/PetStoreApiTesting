package com.sparta.tech243.pets;

import com.sparta.tech243.ApiConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PetsGetTests {

    public static final String BASE_URI = ApiConfig.getBaseUri();
    public static final String BASE_PATH = ApiConfig.getPetBasePath();

    @Test
    @DisplayName("GET /pet/findByStatus")
    void getPetByStatus() {


    }

    @Test
    @DisplayName("GET /pet/findByTags")
    void getPetByTags() {

    }

    @Test
    @DisplayName("GET /pet/{petId}")
    void getPetById() {

    }

}
