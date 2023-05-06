package database;

public class customer {
    private Integer customerID;
    private String name;
    private String buildingNumber;
    private String streetAddress;
    private String city;
    private String postcode;
    private String home;
    private String mobile;

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public customer(Integer customerID, String name, String buildingNumber, String streetAddress, String city, String postcode, String home, String mobile) {
        this.customerID = customerID;
        this.name = name;
        this.buildingNumber = buildingNumber;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postcode = postcode;
        this.home = home;
        this.mobile = mobile;
    }
}
