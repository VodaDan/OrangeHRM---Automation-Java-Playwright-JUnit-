package tests;

import annotations.Endpoint;
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
import jdk.jfr.Description;
import model.User;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.ApiUtils;
import utils.UrlUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ApiTests extends BaseTest {

    private static String token;
    private static APIRequestContext api;

    private static String employeeEndpoint;
    private static String usersEndpoint;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @BeforeAll
    public static void setupTokens() {
        ApiUtils.AuthContext authContext = ApiUtils.extractToken();
        api = authContext.getApi();
        token = authContext.getToken();
        employeeEndpoint = "api/v2/pim/employees";
        usersEndpoint = "api/v2/admin/users";
    }

    // EMPLOYEE API --------------------------------------------------------------------------------

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/pim/employees/",method = "DELETE")
    @Description("API_TC01 - Verify request to delete employees by employee number")
    public void deleteByEmployeeNumberApiTest() {
        User employee = new User().generateRandomUser();
        int emp = ApiUtils.createEmployee(employee);
        Map<String, Object> payload = new HashMap<>();
        payload.put("ids", Collections.singletonList(emp));

        RequestOptions options = RequestOptions.create()
                .setHeader("Content-Type","application/json")
                .setHeader("Accept","application/json")
                .setHeader("Cookie","_orangehrm="+token)
                .setData(payload);

        APIResponse deleteResponse = api.delete("http://localhost/orangehrm-5.7/web/index.php/" + employeeEndpoint,options);
        assertThat(deleteResponse).isOK();
    }

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/pim/employees/{id}",method = "POST")
    @Description("API_TC02 - Verify request to create employees")
    public void createEmployeeApiTest() {
        User employee = new User().generateRandomUser();
        int empNumber = ApiUtils.createEmployee(employee); // post method is inside createEmployee

        assertTrue(empNumber>0, String.valueOf(empNumber));

        APIResponse getEmployeeDetailsResponse = api.get("http://localhost/orangehrm-5.7/web/index.php/"+ employeeEndpoint + "/"+ empNumber);

        JsonObject jsonResponse = JsonParser.parseString(getEmployeeDetailsResponse.text()).getAsJsonObject();
        JsonObject responseData = jsonResponse.getAsJsonObject("data");

        assertEquals(employee.getFirstName(), responseData.get("firstName").getAsString());
        assertEquals(employee.getLastName(), responseData.get("lastName").getAsString());
        assertEquals(employee.getEmployeeId(), responseData.get("employeeId").getAsString());
    }

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/pim/employees", method = "GET")
    @Description("API_TC03 - Verify request to view employees details")
    public void getEmployeeApiTest() {
        int adminUserId = 1;
        APIResponse getEmployeeDetailsResponse = api.get("http://localhost/orangehrm-5.7/web/index.php/" + employeeEndpoint + "/" + adminUserId);
        JsonObject jsonResponse = JsonParser.parseString(getEmployeeDetailsResponse.text()).getAsJsonObject();
        JsonObject responseData = jsonResponse.getAsJsonObject("data");

        assertEquals(globalUser.getFirstName(),responseData.get("firstName").getAsString());
        assertEquals(globalUser.getLastName(),responseData.get("lastName").getAsString());
        assertEquals(globalUser.getEmployeeId(),responseData.get("employeeId").getAsString());
    }

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/pim/employees/count", method = "GET")
    @Description("API_TC04 - Verify request to count total employees count")
    public void getTotalEmployeesCountApiTest() {
       APIResponse getEmployeeCountResponse = api.get("http://localhost/orangehrm-5.7/web/index.php/" + employeeEndpoint + "/count");

       assertThat(getEmployeeCountResponse).isOK();

       JsonObject jsonResponse = JsonParser.parseString(getEmployeeCountResponse.text()).getAsJsonObject();
       JsonObject responseData = jsonResponse.getAsJsonObject("data");

       assertTrue(responseData.get("count").getAsInt() > 1);
    }

    // USER API --------------------------------------------------------------------------------

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/admin/users/{id}", method = "GET")
    @Description("API_USER_TC01 - Verify request to retrive an user details.")
    public void verifyGetUserDetailsApiTest() {
        int globalUserId = 1;
        APIResponse getUserDetails = api.get("http://localhost/orangehrm-5.7/web/index.php/" + usersEndpoint + "/" + globalUserId);

        JsonObject userDetailsResponse = JsonParser.parseString(getUserDetails.text()).getAsJsonObject();
        JsonObject userDetailsData = userDetailsResponse.getAsJsonObject("data");
        System.out.println(userDetailsData);
        assertEquals(globalUser.getUsername(),userDetailsData.get("userName").getAsString());
        assertEquals(1,userDetailsData.get("id").getAsInt());

        JsonObject employeeData = userDetailsData.getAsJsonObject("employee");

        assertEquals(globalUser.getFirstName(),employeeData.get("firstName").getAsString());
        assertEquals(globalUser.getLastName(),employeeData.get("lastName").getAsString());
        assertEquals(globalUser.getEmployeeId(),employeeData.get("employeeId").getAsString());
    }

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/admin/users", method = "POST")
    @Description("API_USER_TC02 - Verify request to create an user.")
    public void creatUserRequestApiTest() {
        User generatedUser = new User().generateRandomUser();

        APIResponse createUserResponse = ApiUtils.createUser(generatedUser);

        assertThat(createUserResponse).isOK();

        JsonObject responseBody = JsonParser.parseString(createUserResponse.text()).getAsJsonObject();
        JsonObject dataBody = responseBody.get("data").getAsJsonObject();

        assertEquals(generatedUser.getUsername(), dataBody.get("userName").getAsString());
        assertEquals(generatedUser.getStatus(), dataBody.get("status").getAsBoolean());

        JsonObject employeeData = dataBody.get("employee").getAsJsonObject();

        assertEquals(generatedUser.getFirstName(), employeeData.get("firstName").getAsString());
        assertEquals(generatedUser.getLastName(), employeeData.get("lastName").getAsString());
        assertEquals(generatedUser.getEmployeeId(), employeeData.get("employeeId").getAsString());
    }

}
