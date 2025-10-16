package tests;

import base.BaseTest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiTests extends BaseTest {

    private static String token;
    private static APIRequestContext api;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @BeforeAll
    public static void extractToken() {
        playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost/orangehrm-5.7/web/index.php"));
        APIResponse getLoginResponse = request.get("http://localhost/orangehrm-5.7/web/index.php/auth/login");
        Matcher m = Pattern.compile(":token=\"&quot;([a-zA-Z0-9._-]+)&quot;\"").matcher(getLoginResponse.text());
        String csrf = m.find() ? m.group(1) : null;

        System.out.println("CSRF: " + csrf);

        Map<String,String> loginForm = new HashMap<>();
        FormData data = FormData.create();
        data.append("_token",csrf);
        data.append("username","adminuser");
        data.append("paassword","!Adminuser123");

        APIResponse postLoginResponse = request.post("http://localhost/orangehrm-5.7/web/index.php/auth/validate", RequestOptions.create().setForm(data));
        api = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("http://localhost/orangehrm-5.7/web/index.php")
                .setStorageState(request.storageState()));
        // System.out.println(request.storageState());
        System.out.println(postLoginResponse.text());

        JsonObject obj = JsonParser.parseString(request.storageState()).getAsJsonObject();
        JsonArray cookies = obj.getAsJsonArray("cookies");

        for (JsonElement el : cookies) {
            JsonObject cookie = el.getAsJsonObject();
            if (cookie.get("name").getAsString().equals("_orangehrm")) {
                String value = cookie.get("value").getAsString();
                System.out.println("_orangehrm cookie: " + value);
                token = value;
            }
        }

    }

    @Test
    public void deleteByEmployeeId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("ids", Collections.singletonList(15));

        RequestOptions options = RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Cookie","_orangehrm="+token)
                .setData(payload);

        APIResponse deleteResponse = api.delete("http://localhost/orangehrm-5.7/web/index.php/api/v2/pim/employees",options);
        System.out.println(deleteResponse.text());
    }
}
