package api.base;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.utils.configReader;
import static io.restassured.RestAssured.*;


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

