package TestCases.User;

import static io.restassured.RestAssured.given;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.utils.configReader;
import api.utils.jsonReader;
import api.utils.Validations.Assertions;
import api.utils.APIEndpointReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AddUserDetails {

    private String sessionCookie;
    private String authToken;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = configReader.getProperty("baseURL");

        configReader.reloadProperties();
        // Load authToken and sessionCookie from config.properties
        authToken = configReader.getProperty("authToken");
        sessionCookie = configReader.getProperty("sessionCookie");

        System.out.println("Auth Token: " + authToken);
        System.out.println("Session Cookie: " + sessionCookie);
    }

//-------------------------- Add Customer API --------------------------------
    @Test(priority = 2)
    public void addCustomer() {
    	
    	
    	
    	System.out.println("-----------------------------Adding Users Details---------------------------------------");
    	
    	
    	
    	// Read JSON data from file
        Map<String, String> customerData = jsonReader.getTestData("src/test/resources/TestData/userData.json");

        // Build the correct URL dynamically
        String customerURL = RestAssured.baseURI + APIEndpointReader.getEndpoint("addCustomerData")
                + "&api_token=" + authToken;

        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("OCSESSID", sessionCookie)
                .formParams(customerData) // Map passed directly as formParams
                .log().all()
                .when()
                .post(customerURL)
                .then()
                .log().all()
                .extract()
                .response();
        
        Assertions.validate200Response(response);
        
        System.out.println("Add Customer Response: " + response.getBody().asString());
        
   
    }
}
