package TestCases.Customer;


import static io.restassured.RestAssured.given;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.utils.APIEndpointReader;
import api.utils.configReader;
import api.utils.jsonReader;
import api.utils.Validations.Assertions;
import api.utils.customer.customerAPIEndpointsReader;
import api.utils.customer.customerConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class customerLogin {

    private String sessionCookie;
    private String authToken;

    @BeforeClass
    public void setup() {
    	
        // Setting up base URL and extracting auth token and session cookie from config.properties
    	
        RestAssured.baseURI = customerConfigReader.getProperty("customerBaseURL");
        configReader.reloadProperties();
        
        sessionCookie = customerConfigReader.getProperty("sessionCookie");

    }

    // ------------------------------------- Customer Login API ------------------------------------------------
    @Test(priority = 1)
    public void customerLogin() {
        System.out.println("-----------------------------Customer Login---------------------------------------");

        // Read login credentials from JSON file
        Map<String, String> loginData = jsonReader.getTestData("src/test/resources/TestData/customerLoginData.json");

        // Build login URL dynamically
        String loginURL = customerAPIEndpointsReader.getEndpoint("customerBaseURL");

        // Send login request using formParams()
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(loginData) // Pass login credentials as form data
                .cookie("OCSESSID", sessionCookie)
                .log().all()
                .when()
                .post("loginURL")
                .then()
                .log().all()
                .extract()
                .response();

        // Validate 200 response
        Assertions.validate200Response(response);

        // Get HTML response as string
        String htmlResponse = response.getBody().asString();

        // Parse HTML using Jsoup
        Document doc = Jsoup.parse(htmlResponse);

        // Create JSON Object to store login result
        JSONObject loginResponse = new JSONObject();

        // Check if login was successful
        Element accountLink = doc.selectFirst("a[href*='route=account/account']");
        Element errorMsg = doc.selectFirst("div.alert-danger");

        if (accountLink != null) {
            loginResponse.put("status", "success");
            loginResponse.put("message", "Login successful");
            loginResponse.put("redirect_url", accountLink.attr("href"));
        } else if (errorMsg != null) {
            loginResponse.put("status", "failed");
            loginResponse.put("message", errorMsg.text().trim());
        } else {
            loginResponse.put("status", "unknown");
            loginResponse.put("message", "Unexpected response. Check HTML.");
        }

        // Extract cart information if available
        Element cartElement = doc.selectFirst("span#cart-total");
        loginResponse.put("cartItems", cartElement != null ? cartElement.text().trim() : "No cart info");

        // Print JSON response
        System.out.println("Login Response in JSON: " + loginResponse.toString(2));

    }
}
