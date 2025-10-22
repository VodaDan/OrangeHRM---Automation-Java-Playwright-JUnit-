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

    private static String baseUrl;
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
        baseUrl = "http://localhost/orangehrm-5.7/web/index.php/";
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

        APIResponse deleteResponse = api.delete(baseUrl + employeeEndpoint,options);
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

        APIResponse getEmployeeDetailsResponse = api.get(baseUrl+ employeeEndpoint + "/"+ empNumber);

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
        APIResponse getEmployeeDetailsResponse = api.get(baseUrl + employeeEndpoint + "/" + adminUserId);
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
       APIResponse getEmployeeCountResponse = api.get(baseUrl + employeeEndpoint + "/count");

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
        APIResponse getUserDetails = api.get(baseUrl + usersEndpoint + "/" + globalUserId);

        JsonObject userDetailsResponse = JsonParser.parseString(getUserDetails.text()).getAsJsonObject();
        JsonObject userDetailsData = userDetailsResponse.getAsJsonObject("data");

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

        APIResponse createUserResponse = ApiUtils.createUser(generatedUser).getResponse();

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

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/admin/users", method = "DELETE")
    @Description("API_USER_TC03 - Verify request to delete an user.")
    public void deleteUserRequestApiTest () {
        // Create a new user for test, verify response and extract id
        User generatedUser = new User().generateRandomUser();

        ApiUtils.CreateUserResponseDTO createUserResponseDTO = ApiUtils.createUser(generatedUser);
        APIResponse createResponse = createUserResponseDTO.getResponse();
        assertThat(createResponse).isOK();
        int id = JsonParser.parseString(createResponse.text()).getAsJsonObject().getAsJsonObject("data").get("id").getAsInt();

        // Create payload for request with id to be deleted
        Map<String, Object> payload = new HashMap<>();
        payload.put("ids",Collections.singletonList(id));

        APIResponse deleteResponse = api.delete(baseUrl + usersEndpoint, RequestOptions.create().setData(payload));

        // Check response and verify id that was deleted is the id of the user created
        assertThat(deleteResponse).isOK();

        JsonObject jsonResponse = JsonParser.parseString(deleteResponse.text()).getAsJsonObject();
        JsonArray idsArray = jsonResponse.get("data").getAsJsonArray();

        assertTrue(idsArray.toString().contains(String.valueOf(id)));
    }

    @Test
    @Tag("api")
    @Endpoint(value = "/api/v2/admin/users/{id}", method = "PUT")
    @Description("API_USER_TC04 - Verify request to uptate an user details.")
    public void updateUserRequestApiTest(){
        // Create a new user for test, verify response and extract id
        User generatedUser = new User().generateRandomUser();

        ApiUtils.CreateUserResponseDTO createUserResponseDTO = ApiUtils.createUser(generatedUser);
        APIResponse createResponse = createUserResponseDTO.getResponse();
        assertThat(createResponse).isOK();
        int id = JsonParser.parseString(createResponse.text()).getAsJsonObject().getAsJsonObject("data").get("id").getAsInt();

        // Create new user and payload for request
        User updateUser = new User().generateRandomUser();
        int updatedEmployeeEmp = ApiUtils.createEmployee(updateUser);

        Map<String, Object> payload = new HashMap<>();
        payload.put("changePassword",true);
        payload.put("username", updateUser.getUsername());
        payload.put("password", updateUser.getPassword());
        payload.put("status", updateUser.getStatus());
        if(updateUser.getRole().equals("Admin")){
            payload.put("userRoleId", 1);
        } else {
            payload.put("userRoleId", 2);
        }
        payload.put("empNumber", updatedEmployeeEmp);

        // Send the request
        APIResponse updateUserResponse = api.put(baseUrl + usersEndpoint + "/" + id, RequestOptions.create().setData(payload));

        // Assert User data
        JsonObject responseJson = JsonParser.parseString(updateUserResponse.text()).getAsJsonObject();
        JsonObject dataJson = responseJson.get("data").getAsJsonObject();

        assertEquals(updateUser.getUsername(), dataJson.get("userName").getAsString());
        assertEquals(updateUser.getStatus(), dataJson.get("status").getAsBoolean());

        // Assert Employee data
        JsonObject employeeJson = dataJson.get("employee").getAsJsonObject();

        assertEquals(updateUser.getFirstName(),employeeJson.get("firstName").getAsString());
        assertEquals(updateUser.getLastName(),employeeJson.get("lastName").getAsString());
        assertEquals(updateUser.getEmployeeId(),employeeJson.get("employeeId").getAsString());
    }

}
