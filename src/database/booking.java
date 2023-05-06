package database;

public class booking {
    private Integer bookingID;
    private String fullName;
    private String mobile;
    private String vehicleRegistration;
    private String bookingType;
    private String bookingDate;
    private String descriptionWork;
    private String customerType;

    public Integer getBookingID() {
        return bookingID;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String date) {
        this.bookingDate = date;
    }

    public String getDescriptionWork() {
        return descriptionWork;
    }

    public void setDescriptionWork(String descriptionWork) {
        this.descriptionWork = descriptionWork;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public booking(Integer bookingID, String fullName, String mobile, String vehicleRegistration, String bookingType, String bookingDate, String descriptionWork, String customerType) {
        this.bookingID = bookingID;
        this.fullName = fullName;
        this.mobile = mobile;
        this.vehicleRegistration = vehicleRegistration;
        this.bookingType = bookingType;
        this.bookingDate = bookingDate;
        this.descriptionWork = descriptionWork;
        this.customerType = customerType;
    }
}
