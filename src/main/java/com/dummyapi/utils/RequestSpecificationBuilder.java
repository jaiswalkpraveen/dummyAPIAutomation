package com.dummyapi.utils;

import com.dummyapi.config.APIConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecificationBuilder {
    public static RequestSpecification getRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(APIConfig.BASE_URL)
                .setContentType("application/json")
                .build();
    }
}