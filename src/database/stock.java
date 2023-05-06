package database;

public class stock {

    // Creating variables for the creation of an account
    Integer part_ID;
    String partName;
    String code;
    String manufacturer;
    String vehicleType;
    String year;
    String price;
    Integer stockLevel;

    // Creating getters and setters for the variable

    public Integer getPart_ID() {
        return part_ID;
    }

    public void setPart_ID(Integer part_ID) {
        this.part_ID = part_ID;
    }

    public String getPartName() {
        return partName;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(Integer stockLevel) {
        this.stockLevel = stockLevel;
    }

    // Creating the constructor
    public stock(Integer part_ID, String partName, String code, String manufacturer, String vehicleType, String year, String price, Integer stockLevel) {
        this.part_ID = part_ID;
        this.partName = partName;
        this.code = code;
        this.manufacturer = manufacturer;
        this.vehicleType = vehicleType;
        this.year = year;
        this.price = price;
        this.stockLevel = stockLevel;
    }
}