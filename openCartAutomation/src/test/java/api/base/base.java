package api.base;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;


import api.utils.configReader;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;


public class base {

    protected static RequestSpecification request;
   
    // Setup before all tests
    public static void setup() {
        RestAssured.baseURI = configReader.getProperty("baseURL");

        request = RestAssured
                .given()
                .header("Authorization", "Bearer " + configReader.getProperty("authToken"))
                .contentType(configReader.getProperty("contentType"));
    }
}