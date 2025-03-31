package TestCases.Cart;


import static io.restassured.RestAssured.given;
import api.utils.configReader;
import api.utils.Validations.Assertions;
import api.utils.APIEndpointReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class viewCart {

    private String sessionCookie;
    private String authToken;

    @BeforeClass
    public void setup() {
        // Load base URL dynamically
        RestAssured.baseURI = configReader.getProperty("baseURL");

        // Reload properties to fetch updated session and auth token
        configReader.reloadProperties();

        sessionCookie = configReader.getProperty("sessionCookie");
        authToken = configReader.getProperty("authToken");

        System.out.println("Loaded Session Cookie: " + sessionCookie);
        System.out.println("Loaded Auth Token: " + authToken);
    }

    @Test(priority = 4)
    public void testViewCart() {
    	// Build the correct URL dynamically
        String viewCartURL = RestAssured.baseURI + APIEndpointReader.getEndpoint("getCartProduct")
                + "&api_token=" + authToken;

        System.out.println("-------------------View Cart API--------------------------");

        // Make API request to View Cart end point
        Response response = given()
                .contentType("application/json")
                .header("Cookie", "OCSESSID=" + sessionCookie)
                .queryParam("api_token", authToken)
                .log().all()
                .when()
                .get(viewCartURL)
                .then()
                .log().all()
                .extract()
                .response();

        // Validate HTTP response code
        Assertions.validate200Response(response);
        
        System.out.println("View cart Response: " + response.getBody().asString());
    } 
}






