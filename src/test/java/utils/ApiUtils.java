package utils;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    // Obsolete : Data cleanup is automatically done by owner on a regular basis.
    public static void employeeCleanupById(String employeeId) {
        try(Playwright playwright = Playwright.create()) {
            String baseUrl = "https://opensource-demo.orangehrmlive.com";
            APIRequestContext request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(baseUrl));
            String endpoint = "/web/index.php/api/v2/pim/employees";

            Map<String, Object> payload = new HashMap<>();
            payload.put("ids", Collections.singletonList(employeeId));

            RequestOptions options = RequestOptions.create()
                    .setHeader("Content-Type","application/json")
                    .setData(payload);

            APIResponse deleteResponse = request.delete(endpoint,options);

            System.out.println("Status code: " + deleteResponse.status());
            System.out.println("Response: " + deleteResponse.text());

            if(deleteResponse.ok()) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Failed to delete the employee - " + deleteResponse.status() + ".");
            }
            request.dispose();

        }
    }
}
