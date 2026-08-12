package com.framework.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * REST API utility wrapper for RestAssured, integrated with ExtentReports.
 */
public class RestAPIUtil {

    private final String baseURI;
    private final String testName;

    public RestAPIUtil(String baseURI) {
        this.baseURI = baseURI;
        this.testName = "unknown";
        RestAssured.baseURI = baseURI;
    }

    public String getTestMethodName() {
        return testName;
    }

    /**
     * Log a message to console.
     */
    public void log(Object message) {
        System.out.println(message);
    }

    /**
     * Log only to console.
     */
    public void logC(Object message) {
        System.out.println(message);
    }

    /**
     * Log only to console (alias).
     */
    public void logE(Object message) {
        System.out.println(message);
    }

    // ── GET requests ──────────────────────────────────────────────

    public int getStatusCode(String path) {
        return given().when().get(path).statusCode();
    }

    public String getBody(String path) {
        return given().when().get(path).body().asPrettyString();
    }

    public Response getResponse(String path) {
        return given().when().get(path);
    }

    // ── POST requests ─────────────────────────────────────────────

    public int postStatusCode(String path, Object body) {
        return given().body(body).when().post(path).statusCode();
    }

    public String postBody(String path, Object body) {
        return given().body(body).when().post(path).body().asPrettyString();
    }

    public Response postResponse(String path, Object body) {
        return given().body(body).when().post(path);
    }

    // ── PUT requests ──────────────────────────────────────────────

    public int putStatusCode(String path, Object body) {
        return given().body(body).when().put(path).statusCode();
    }

    public String putBody(String path, Object body) {
        return given().body(body).when().put(path).body().asPrettyString();
    }

    // ── DELETE requests ───────────────────────────────────────────

    public int deleteStatusCode(String path) {
        return given().when().delete(path).statusCode();
    }

    public String deleteBody(String path) {
        return given().when().delete(path).body().asPrettyString();
    }

    // ── Utility delegates ─────────────────────────────────────────

    public String intToString(int value) {
        return GeneralUtil.intToString(value);
    }

    public int stringToInt(String value) {
        return GeneralUtil.stringToInt(value);
    }

    public boolean stringEquals(String v1, String v2) {
        return GeneralUtil.stringEquals(v1, v2);
    }
}
