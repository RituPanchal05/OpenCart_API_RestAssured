package TestCases.Customer;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

public class customerRegistration {

	private String sessionCookie;
	private String configPath = System.getProperty("user.dir") + "/src/main/resources/customer/customer_config.properties";
    @BeforeClass
    public void setup() {
        
        // Load base URL and credentials from config.properties
        RestAssured.baseURI = customerConfigReader.getProperty("customerBaseURL");
        configReader.reloadProperties();
    }

//------------------------------------- Customer Registration API ----------------------------------------

    @Test(priority = 1)
    public void registerCustomer() {

        System.out.println("-----------------------------Customer Registration---------------------------------------");

        // Read customer registration data from JSON file
        Map<String, String> customerData = jsonReader.getTestData("src/test/resources/TestData/customerRegistrationData.json");

        // Build registration URL dynamically
        String cRegisterURL = RestAssured.baseURI + customerAPIEndpointsReader.getEndpoint("customerRegister");

        // Send data using formParams()
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParams(customerData) // Passing form data
                .log().all()
                .when()
                .post(cRegisterURL)
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();  
        
        
        // Extract and session cookie
        sessionCookie = response.getCookie("OCSESSID");


        if (sessionCookie != null && !sessionCookie.isEmpty()) {
            System.out.println("Session Cookie Captured: " + sessionCookie);

            // Update config.properties with token and session cookie
            Map<String, String> updates = Map.of(
                    "sessionCookie", sessionCookie
            );
            updateConfigFile(updates);
        } else {
            System.out.println("Failed to capture session cookie.");
        }

//---------------------------------Call convertHtmlToJson()---------------------------------------------------
        
        
        
	     String htmlResponse = response.getBody().asString();
	     String jsonResponse = convertHtmlToJson(htmlResponse);
	     System.out.println("Converted JSON: " + jsonResponse);
	     
	     
//------------------------------------ ----Validations ----------------------------------------------

	           Assertions.validate200Response(response);
   
    }
 

//----------------------------------Method to update config.properties dynamically--------------------------
    
    
    public void updateConfigFile(Map<String, String> updates) {
        Properties props = new Properties();

        try (FileInputStream in = new FileInputStream(configPath)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Failed to load config.properties file: " + e.getMessage());
            return;
        }

        // Update properties dynamically
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            System.out.println("Updating key: " + entry.getKey() + ", Value: " + entry.getValue());
            props.setProperty(entry.getKey(), entry.getValue());
        }

        // Save the updated properties
        try (FileOutputStream out = new FileOutputStream(configPath)) {
            props.store(out, "Updated properties dynamically after login");
            System.out.println("Config properties updated successfully!");
        } catch (IOException e) {
            System.out.println("Failed to update config.properties file: " + e.getMessage());
        }
        
    }
    
   
//------------------------------------ HTML to JSON Conversion ------------------------------------

    public String convertHtmlToJson(String html) {
        Document document = Jsoup.parse(html);
        
        Element firstNameElement = document.selectFirst("input[name=firstname]");
        Element lastNameElement = document.selectFirst("input[name=lastname]");



        System.out.println("First Name Element: " + firstNameElement);
        System.out.println("Last Name Element: " + lastNameElement);


        // Prepare JSON manually with extracted data
        String jsonResponse = "{";
        if (firstNameElement != null) {
            jsonResponse += "\"firstname\": \"" + firstNameElement.attr("value") + "\"";
        }
        if (lastNameElement != null) {
            if (!jsonResponse.equals("{")) {
                jsonResponse += ", ";
            }
            jsonResponse += "\"lastname\": \"" + lastNameElement.attr("value") + "\"";
        }
        jsonResponse += "}";

        return jsonResponse;
    }
        
}

