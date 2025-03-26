package TestCases.Customer;

import static io.restassured.RestAssured.given;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.utils.configReader;
import api.utils.jsonReader;
import api.utils.Validations.Assertions;
import api.utils.customer.customerAPIEndpointsReader;
import api.utils.customer.customerConfigReader;
import api.utils.APIEndpointReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class customerAddToCart {

    private String sessionCookie;

    @BeforeClass
    public void setup() {
    	
    	//setting up baseURLs and extracting auth token and session cookies form config.properties file
    	
        RestAssured.baseURI = customerConfigReader.getProperty("customerBaseURL");
        configReader.reloadProperties();
 
        sessionCookie = customerConfigReader.getProperty("sessionCookie");

        System.out.println("Session Cookie: " + sessionCookie);
        
    }

//------------------------------------- Add Product to Cart API ----------------------------------------
    @Test(priority = 3)
    public void addProductToCart() {
    	
    	
    	
    	System.out.println("-----------------------------Add Product into cart---------------------------------------");
    	
    	
        // Read product data from JSON file
    	
        Map<String, String> productData = jsonReader.getTestData("src/test/resources/TestData/customerCartData.json");

        // Build the correct URL dynamically
        
        String cartURL = RestAssured.baseURI + customerAPIEndpointsReader.getEndpoint("customerAddToCart");
        System.out.println(cartURL);

        // Send data using formParams()
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("OCSE?SSID", sessionCookie) // cookie based authentication
                .formParams(productData) // passing json data in form parameter
                .log().all()
                .when()
                .post(cartURL)
                .then()
                .log().all()
                .extract()
                .response();
        
//------------------------------------Validations------------------------------------
        
        Assertions.validate200Response(response);
        
        // Print response
        System.out.println("Add Product Response: " + response.getBody().asString());
    }

}


