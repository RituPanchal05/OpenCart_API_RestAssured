package TestCases.Cart;


import static io.restassured.RestAssured.given;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import api.utils.configReader;
import api.utils.jsonReader;
import api.utils.Validations.Assertions;
import api.utils.APIEndpointReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AddToCart {

    private String sessionCookie;
    private String authToken;

    @BeforeClass
    public void setup() {
    	
    	//setting up baseURLs and extracting auth token and session cookies form config.properties file
    	
        RestAssured.baseURI = configReader.getProperty("baseURL");
        configReader.reloadProperties();
 
        authToken = configReader.getProperty("authToken");
        sessionCookie = configReader.getProperty("sessionCookie");

        System.out.println("Auth Token: " + authToken);
        System.out.println("Session Cookie: " + sessionCookie);
        
    }

//------------------------------------- Add Product to Cart API ----------------------------------------
    @Test(priority = 3)
    public void addProductToCart() {
    	
    	
    	
    	System.out.println("-----------------------------Add Product into cart---------------------------------------");
    	
    	
        // Read product data from JSON file
    	
        Map<String, String> productData = jsonReader.getTestData("src/test/resources/TestData/cartData.json");

        // Build the correct URL dynamically
        
        String cartURL = RestAssured.baseURI + APIEndpointReader.getEndpoint("addToCart")
                + "&api_token=" + authToken;

        // Send data using formParams()
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .cookie("OCSESSID", sessionCookie) // cookie based authentication
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

