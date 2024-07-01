package com.dummyapi;

import com.dummyapi.utils.RequestSpecificationBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class TestBase {
    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUp() {
        requestSpec = RequestSpecificationBuilder.getRequestSpecification();
    }
}