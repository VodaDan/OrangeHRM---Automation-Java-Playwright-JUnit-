package model;

import utils.ApiUtils;
import utils.ProjectDataFactory;

public class Project {

    private int customerId; // required
    private String name; // required
    private String description;
    private int[] admins; //projectAdminsEmpNumbers

    public Project(int customerId, String name, String description, int[] admins) {
        this.customerId = customerId;
        this.name = name;
        this.description = description;
        this.admins = admins;
    }

    public Project() {
        this.name = ProjectDataFactory.generateProjectName();
        this.description = ProjectDataFactory.generateProjectDescription(this.name);
        this.customerId = 1; //Future feature: Create a data factory for customer

        User projectAdmin = new User().generateRandomUser();
        int emp = ApiUtils.createEmployee(projectAdmin);

        this.admins = new int[]{emp};
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int[] getAdmins() {
        return admins;
    }

    public void setAdmins(int[] admins) {
        this.admins = admins;
    }
}
