package api.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class APIEndpointReader {

    private static Properties properties;

    static {
        try {
            String endpointPath = "src/main/resources/API_Endpoints.properties";
            FileInputStream fis = new FileInputStream(endpointPath);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load apiEndpoints.properties.");
        }
    }

    // Get endpoint by key
    public static String getEndpoint(String key) {
        return properties.getProperty(key);
    }
}
