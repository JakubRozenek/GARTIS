package database;

public class vehicles {
    private Integer vehicleID;
    private String registrationPlate;
    private String vehicleColor;
    private String date;
    private String vehicleName;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public vehicles(Integer vehicleID, String registrationPlate, String vehicleColor, String date, String vehicleName) {
        this.vehicleID = vehicleID;
        this.registrationPlate = registrationPlate;
        this.vehicleColor = vehicleColor;
        this.date = date;
        this.vehicleName = vehicleName;
    }
}
