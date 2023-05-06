package database;

public class orderHistory {

    // Creating variables for the creation of an account
    private String part_ID;
    private String partName;
    private String code;
    private String manufacturer;
    private String vehicleType;
    private String year;
    private String price;
    private String date;

    // Getters and setter for each variable
    public String getPart_ID() {
        return part_ID;
    }

    public void setPart_ID(String part_ID) {
        this.part_ID = part_ID;
    }

    public String getPartName() { return partName; }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Constructor for creating the account
    public orderHistory(String part_ID, String partName, String code, String manufacturer, String vehicleType, String year, String price, String date) {
        this.part_ID = part_ID;
        this.partName = partName;
        this.code = code;
        this.manufacturer = manufacturer;
        this.vehicleType = vehicleType;
        this.year = year;
        this.price = price;
        this.date = date;
    }
}