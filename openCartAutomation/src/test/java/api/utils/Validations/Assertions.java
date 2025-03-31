package api.utils.Validations;


import io.restassured.response.Response;
import org.testng.Assert;

public class Assertions {

    // Validate 200 OK response
    public static void validate200Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is not 200!");
        System.out.println("Response code 200 validated successfully.");
    }

    // Validate 201 Created response
    public static void validate201Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code is not 201!");
        System.out.println("Response code 201 validated successfully.");
    }

    // Validate 400 Bad Request response
    public static void validate400Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code is not 400!");
        System.out.println("Bad Request (400) - Check input data or format.");
    }

    // Validate 401 Unauthorized response
    public static void validate401Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 401, "Expected status code is not 401!");
        System.out.println("Unauthorized Access (401) - Invalid or missing credentials.");
    }

    // Validate 404 Not Found response
    public static void validate404Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 404, "Expected status code is not 404!");
        System.out.println("Resource Not Found (404).");
    }

    // Validate 500 Internal Server Error response
    public static void validate500Response(Response response) {
        Assert.assertEquals(response.getStatusCode(), 500, "Expected status code is not 500!");
        System.out.println("Internal Server Error (500) - Check server logs.");
    }

    // Generic Validation - If you want to pass status code dynamically
    public static void validateResponseCode(Response response, int expectedStatusCode) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Expected status code is not matched. Expected: " + expectedStatusCode + " but got: " + response.getStatusCode());
        System.out.println("Response with status code " + expectedStatusCode + " validated successfully.");
    }
}
  