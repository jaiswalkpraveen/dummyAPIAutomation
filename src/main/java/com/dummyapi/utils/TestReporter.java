package com.dummyapi.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class TestReporter {
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void logStep(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }

    public static void logFail(String message) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }
}