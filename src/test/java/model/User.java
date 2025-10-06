package model;

import org.junit.jupiter.api.Test;
import utils.TestDataFactory;

public class User {
    private String role;
    private String firstName;
    private String lastName;
    private String status;
    private String username;
    private String password;
    private String employeeId;

    public User(String role, String firstName, String lastName, String status, String username, String password, String employeeId) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.username = username;
        this.password = password;
        this.employeeId = employeeId;
    }

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User generateRandomUser() {
        firstName = TestDataFactory.generateRandomName();
        lastName = TestDataFactory.generateRandomLastName();
        employeeId = TestDataFactory.generateEmployeeId();
        status = "Enabled";
        role = "ESS";
        username = TestDataFactory.generateRandomUsername();
        password = TestDataFactory.generateRandomPassword();
        return new User(role,firstName,lastName,status,username,password,employeeId);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
