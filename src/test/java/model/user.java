package model;

public class user {
    private String role;
    private String name;
    private String status;
    private String username;
    private String password;

    public user(String role, String name, String status, String username, String password) {
        this.role = role;
        this.name = name;
        this.status = status;
        this.username = username;
        this.password = password;
    }

    public user () {

    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
