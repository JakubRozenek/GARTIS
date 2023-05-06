package database;

public class jobs {
    private Integer jobID;
    private String status;
    private String descriptionWork;
    private String estimatedTime;
    private String actualTime;
    private String mechanic;
    private String vehicleRegistration;
    private String descriptionCarried;

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescriptionWork() {
        return descriptionWork;
    }

    public void setDescriptionWork(String descriptionWork) {
        this.descriptionWork = descriptionWork;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getDescriptionCarried() {
        return descriptionCarried;
    }

    public void setDescriptionCarried(String descriptionCarried) {
        this.descriptionCarried = descriptionCarried;
    }

    public jobs(Integer jobID, String status, String descriptionWork, String estimatedTime, String actualTime, String mechanic, String vehicleRegistration, String descriptionCarried) {
        this.jobID = jobID;
        this.status = status;
        this.descriptionWork = descriptionWork;
        this.estimatedTime = estimatedTime;
        this.actualTime = actualTime;
        this.mechanic = mechanic;
        this.vehicleRegistration = vehicleRegistration;
        this.descriptionCarried = descriptionCarried;
    }
}
