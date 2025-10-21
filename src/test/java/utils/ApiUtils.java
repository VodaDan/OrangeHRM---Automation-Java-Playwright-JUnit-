package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import model.User;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import tests.ApiTests;

import java.text.Normalizer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiUtils {

    private static APIRequestContext request;

    public static class AuthContext {
        public final String token;
        public final APIRequestContext api;

        public AuthContext(String token, APIRequestContext api) {
            this.token = token;
            this.api = api;
        }

        public String getToken() {
            return token;
        }

        public APIRequestContext getApi() {
            return api;
        }
    }


    public static AuthContext extractToken() {
        Playwright playwright = Playwright.create();
        request = playwright.request().newContext(new APIRequest.NewContextOptions());
        APIResponse getLoginResponse = request.get("http://localhost/orangehrm-5.7/web/index.php/auth/login");
        Matcher m = Pattern.compile(":token=\"&quot;([a-zA-Z0-9._-]+)&quot;\"").matcher(getLoginResponse.text());
        String csrf = m.find() ? m.group(1) : null;

        FormData data = FormData.create();
        data.append("_token",csrf);
        data.append("username","adminuser");
        data.append("password","!Adminuser123");

        APIResponse postLoginResponse = request.post("http://localhost/orangehrm-5.7/web/index.php/auth/validate", RequestOptions.create().setForm(data));
        APIRequestContext api = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost/orangehrm-5.7/web/index.php")
                .setStorageState(request.storageState()));

        JsonObject obj = JsonParser.parseString(request.storageState()).getAsJsonObject();
        JsonArray cookies = obj.getAsJsonArray("cookies");

        String token = null;
        for (JsonElement el : cookies) {
            JsonObject cookie = el.getAsJsonObject();
            if (cookie.get("name").getAsString().equals("_orangehrm")) {
                String value = cookie.get("value").getAsString();
                token = value;
            }
        }

        return new AuthContext(token,api);
    }

    public static int createEmployee(User employeeSent) {
        User employee = employeeSent;

        FormData employeeForm = FormData.create();
        employeeForm.append("firstName",employee.getFirstName());
        employeeForm.append("middleName","");
        employeeForm.append("lastName",employee.getLastName());
//        employeeForm.append("empPicture","");
        employeeForm.append("employeeId",employee.getEmployeeId());

        APIResponse createEmployeeResponse = request.post("http://localhost/orangehrm-5.7/web/index.php/api/v2/pim/employees", RequestOptions.create().setForm(employeeForm));

        String employeeResponseBody = createEmployeeResponse.text();
        JsonObject jsonResponse = JsonParser.parseString(employeeResponseBody).getAsJsonObject();
        JsonObject dataResponse = jsonResponse.getAsJsonObject("data");

        return dataResponse.get("empNumber").getAsInt();
    }

    public static APIResponse createUser(User userSent) {
        int emp = ApiUtils.createEmployee(userSent);

        Map<String, Object> userData = new HashMap<>();
        userData.put("username", userSent.getUsername());
        userData.put("password", userSent.getPassword());
        userData.put("status", userSent.getStatus());
        if(userSent.getRole().equals("Admin")){
            userData.put("userRoleId", 1);
        } else {
            userData.put("userRoleId", 2);
        }
        userData.put("empNumber", emp);

        APIResponse createUserResponse = request.post(
                "http://localhost/orangehrm-5.7/web/index.php/api/v2/admin/users",
                RequestOptions.create().setData(userData) // Use setData() instead of setForm()
        );

        return createUserResponse;
    }
}
