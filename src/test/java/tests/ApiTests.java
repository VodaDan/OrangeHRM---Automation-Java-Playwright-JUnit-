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
import utils.ApiUtils;
import utils.UrlUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ApiTests extends BaseTest {

    private static String token;
    private static APIRequestContext api;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @BeforeAll
    public static void setupTokens() {
        ApiUtils.AuthContext authContext = ApiUtils.extractToken();
        api = authContext.getApi();
        token = authContext.getToken();
    }

    @Test
    public void deleteByEmployeeId() {
        int emp = ApiUtils.createEmployee();
        Map<String, Object> payload = new HashMap<>();
        payload.put("ids", Collections.singletonList(emp));

        RequestOptions options = RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Cookie","_orangehrm="+token)
                .setData(payload);

        APIResponse deleteResponse = api.delete("http://localhost/orangehrm-5.7/web/index.php/api/v2/pim/employees",options);
        assertThat(deleteResponse).isOK();
    }
}
