package database;

public class employee {
    private Integer employeeID;
    private String fullName;
    private String jobRole;
    private String username;
    private String password;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
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

    public employee(Integer employeeID, String fullName, String jobRole, String username, String password) {
        this.employeeID = employeeID;
        this.fullName = fullName;
        this.jobRole = jobRole;
        this.username = username;
        this.password = password;
    }
}