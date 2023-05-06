package controllers;

// Importing essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

// Controlling the order history GUI
public class vehicles implements Initializable {

    // Table from FXML
    @FXML
    private TableView<database.vehicles> vehiclesTable;

    @FXML
    private TableColumn<database.vehicles, String> registrationNumberColumn, vehicleColourColumn, dateColumn, vehicleNameColumn;

    @FXML
    private TableColumn<database.vehicles, Integer> vehicleIDColumn;

    // Text fields from FXML
    @FXML
    private TextField registrationNumberText, vehicleColourText, dateText, vehicleNameText;

    // Variable holding the username of the user that is currently logged in
    private static Integer vehicleOwnerID;

    // Subroutine automatically runs everytime this file gets executed
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Load all the data into the table
        extractData();
    }

    // Add a vehicle for the specific owner
    public void addVehicle() {

        // Try to extract all the data from the SQL table
        try {
            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to send to the database
            PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Vehicles (vehicleOwnerID, registrationPlate, vehicleColor, date, vehicleName) VALUES (?, ?, ?, ?, ?);");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, getVehicleOwnerID());
            pstate.setString(2, registrationNumberText.getText());
            pstate.setString(3, vehicleColourText.getText());
            pstate.setString(4, dateText.getText());
            pstate.setString(5, vehicleNameText.getText());

            // Execute the query
            pstate.execute();

            // Update the table
            extractData();
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }
    }

    // Extract data from the database
    public void extractData() {

        // Adding Items to the tableView
        ObservableList<database.vehicles> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT * FROM Vehicles WHERE vehicleOwnerID == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, getVehicleOwnerID());

            // Save the results from the database to the ResultSet
            ResultSet rs = pstate.executeQuery();

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                items.add(new database.vehicles(
                        rs.getInt("vehicleID"),
                        rs.getString("registrationPlate"),
                        rs.getString("vehicleColor"),
                        rs.getString("date"),
                        rs.getString("vehicleName")
                ));
            }
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        vehicleIDColumn.setCellValueFactory(new PropertyValueFactory<database.vehicles, Integer>("vehicleID"));
        registrationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("registrationPlate"));
        vehicleColourColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleColor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        vehicleNameColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));

        // Adding the data to the table
        vehiclesTable.setItems(items);
    }

    // Variable to hold all the vehicles for the specific customer
    private String[] customerVehicles;

    // Getter for the vehicles that the customer has
    public String[] getCustomerVehicles() {
        return customerVehicles;
    }

    // Set the vehicles for the specific customer
    public void setCustomerVehicles(String[] customerVehicles) {
        this.customerVehicles = customerVehicles;
    }

    // Getter for the username that is currently logged in
    public static Integer getVehicleOwnerID() {
        return vehicleOwnerID;
    }

    // Setter for the login class to set the username
    public static void setVehicleOwnerID(Integer vehicleOwnerID) {
        vehicles.vehicleOwnerID = vehicleOwnerID;
    }
}