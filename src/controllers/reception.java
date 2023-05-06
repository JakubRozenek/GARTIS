package controllers;

// Importing all the essential modules
import database.stock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Subroutine controlling the GUI of the reception
public class reception implements Initializable {

    // Job table from FXML
    @FXML
    private TableView<database.jobs> jobTable;

    @FXML
    private TableColumn<database.jobs, String> jobStatusColumn, jobDescriptionColumn, jobEstimatedColumn, jobActualColumn, jobMechanicColumn, jobVehicleRegistrationColumn, jobDescriptionCompletedColumn;

    @FXML
    private TableColumn<database.jobs, Integer> jobIDColumn;

    // Stock table from FXML
    @FXML
    private TableView<database.stock> stockTable;

    @FXML
    private TableColumn<database.stock, String> stockPartColumn, stockCodeColumn, stockManufacturerColumn, stockVehicleColumn, stockYearColumn, stockPriceColumn;

    @FXML
    private TableColumn<database.stock, Integer> stockLevelColumn;

    // Customer table from FXML
    @FXML
    private TableView<database.customer> customerTable;

    @FXML
    private TableColumn<database.customer, String> customerNameColumn, customerBuildingColumn, customerStreetColumn, customerCityColumn, customerPostcodeColumn, customerHomeColumn, customerMobileColumn;

    // Booking table from FXML
    @FXML
    private TableView<database.booking> bookingTable;

    @FXML
    private TableColumn<database.booking, String> bookingNameColumn, bookingMobileColumn, bookingVehicleRegistrationColumn, bookingTypeColumn, bookingDateColumn, bookingDescriptionColumn, bookingCustomerColumn;

    // Text fields from FXML
    @FXML
    private TextField bookingNameText, bookingBuildingText, bookingStreetText, bookingCityText, bookingPostcodeText, bookingHomeText, bookingMobileText;

    @FXML
    private TextField customerNameText, customerBuildingText, customerStreetText, customerMobileText, customerCityText, customerPostcodeText, customerHomeText;

    @FXML
    private TextField stockSearchText, stockUpdateLevelText, stockPartText, stockManufacturerText, stockYearText, stockLevelText, stockCodeText, stockVehicleText, stockPriceText;

    // Text Area from FXML
    @FXML
    private TextArea bookingDescriptionText;

    // Pane from FXML
    @FXML
    private Pane dashboardPane, accountPane, bookingPane, stockPane, jobListPane;

    // Buttons from FXML
    @FXML
    private Button dashboardButton, accountButton, bookingButton, stockButton, jobListButton, walkBookingButton, makePaymentButton, generateInvoiceButton;

    // Split menu from FXML
    @FXML
    private SplitMenuButton accountSplitMenu;

    // Menu items from FXML
    @FXML
    private MenuItem customerAddItem, customerEditItem, customerRemoveItem, bookingAddItem, bookingEditItem, bookingRemoveItem, stockAddItem, stockEditItem, stockRemoveItem;

    // Date picker from FXML
    @FXML
    private DatePicker bookingDatePicker;

    // Checkbox from FXML
    @FXML
    private CheckBox motCheckbox, serviceCheckbox, repairCheckbox;

    // Combobox from FXML
    @FXML
    private ComboBox<String> bookingVehicleCombo;

    // Double to hold the total price of the mechanics labour
    private static Double mechanicLabourCost = 0.0;
    private static Double finalMechanic = 0.0;

    // Create a double variable to hold the total price of parts
    private static Double partsCost = 0.0;
    private static Double finalParts = 0.0;

    // Creating a variable to hold the grand total
    private static Double gt;

    // Allowing the total price to be rounded down
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    // Create a string to hold the booking type
    private String bookingType = null;

    // Referencing the main java class
    admin admin = new admin();

    // Referencing the report java class
    main.report report = new main.report();

    // Subroutine automatically runs everytime this file gets executed
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // When the user starts the program he will go to the dashboard
        dashboardPane.toFront();

        // Set the account split menu text according to the name of the user that is logged in
        accountSplitMenu.setText(controllers.admin.getUsername());

        // Handle all the action events for customer
        handleAccounts();
    }

    // Adding both prices together and adding the VAT costs
    public static Double calculateGrandTotal() {
        return (finalMechanic + finalParts) * 1.20;
    }

    // Setter for setting the final mechanic and parts cost + VAT
    public static void setGrandTotal(Double mechanicLabourCost, Double partsCost) {
        reception.finalMechanic = mechanicLabourCost;
        reception.finalParts = partsCost;
    }

    // Getter for grand total
    public static Double grandTotal() {
        return reception.gt;
    }

    // Calculating the final prices
    public void calculateFinale() {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query
            PreparedStatement pstate0 = state.getConnection().prepareStatement("SELECT Booking.customerType FROM Booking WHERE Booking.vehicleRegistration == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate0.setString(1, jobTable.getSelectionModel().getSelectedItem().getVehicleRegistration());

            // Save the results to a result set
            ResultSet rs = pstate0.executeQuery();

            // Check if the customer type is a account holder and add a 5% discount
            if (rs.getString("customerType").equals("Account Holder")) {

                // Prepare the query
                PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Jobs SET vat = ?, mechanicParts = ?, grandTotal = ? WHERE jobID == ?");

                Double calculateVAT = Double.parseDouble(decimalFormat.format((finalMechanic + finalParts) * 0.2));
                Double calculateTotal = Double.parseDouble(decimalFormat.format(finalMechanic + finalParts));
                Double calculateGrand = Double.parseDouble(decimalFormat.format(((finalMechanic + finalParts) * 1.2) * 0.95));

                gt = calculateGrand;

                // Fill in all the variables to be sent inside the statement
                pstate.setDouble(1, calculateVAT);
                pstate.setDouble(2, calculateTotal);
                pstate.setDouble(3, calculateGrand);
                pstate.setInt(4, jobTable.getSelectionModel().getSelectedItem().getJobID());

                // Execute the query
                pstate.execute();

                // Close the connection
                pstate.close();
            }

            // If the customer is a walk-in add only 2.5% discount
            else if (rs.getString("customerType").equals("Walk-In")) {

                // Prepare the query
                PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Jobs SET vat = ?, mechanicParts = ?, grandTotal = ? WHERE jobID == ?");

                double calculateVAT = Double.parseDouble(decimalFormat.format((finalMechanic + finalParts) * 0.2));
                double calculateTotal = Double.parseDouble(decimalFormat.format(finalMechanic + finalParts));
                double calculateGrand = Double.parseDouble(decimalFormat.format((finalMechanic + finalParts) * 1.2));

                // Adding the grand total to the public variable
                gt = calculateGrand;

                // Fill in all the variables to be sent inside the statement
                pstate.setDouble(1, calculateVAT);
                pstate.setDouble(2, calculateTotal);
                pstate.setDouble(3, calculateGrand);
                pstate.setInt(4, jobTable.getSelectionModel().getSelectedItem().getJobID());

                // Execute the query
                pstate.execute();

                // Close the connection
                pstate.close();
            }

            // Close the current connection
            rs.close();
            pstate0.close();

        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Get the parts cost from the database
    public void getPartsCost(Double cost) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT partsCost FROM Jobs WHERE jobID == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Execute the query
            ResultSet rs = pstate.executeQuery();

            // Add the new parts cost to the variable
            partsCost = rs.getDouble("partsCost");

            // Set the the grand total
            setGrandTotal(mechanicLabourCost, cost);

            // Close the connection
            pstate.close();
            rs.close();
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Calculate the cost of the parts
    public void generatePartsPrice() {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT partPrice FROM JobParts WHERE JobParts.jobID == ?;");

            // Fill in all the variables to be sent inside the statements
            pstate.setInt(1, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Execute the query
            ResultSet rs = pstate.executeQuery();

            // Looping through results
            while (rs.next()) {

                // Adding all the parts price
                partsCost += rs.getDouble("partPrice");
            }

            // Get the price cost
            getPartsCost(partsCost);

            // Close the current connection
            pstate.close();
            rs.close();

            // Prepare the query
            PreparedStatement pstate3 = state.getConnection().prepareStatement("UPDATE Jobs SET partsCost = ? WHERE jobID == ?");

            // Fill in all the variables to be sent inside the statement
            pstate3.setDouble(1, partsCost);
            pstate3.setInt(2, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Reset the parts cost
            partsCost = 0.00;

            // Execute the query
            pstate3.execute();

            // Close the current connection
            pstate3.close();
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Calculate only the labour cost
    public void generateMechanicPrice() {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT actualTime, mechanic, totalPrice FROM Jobs WHERE jobID == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Execute the query
            ResultSet rs = pstate.executeQuery();

            // Check what mechanic done the job and their respective hourly rate
            if (!rs.getString("mechanic").contains("Evans")) {

                // Calculate the price of the work done
                mechanicLabourCost = rs.getDouble("actualTime") * 105;

                // Prepare the query
                PreparedStatement pstate2 = state.getConnection().prepareStatement("UPDATE Jobs SET totalPrice = ? WHERE jobID == ?;");

                // Fill in all the variables to be sent inside the statement
                pstate2.setDouble(1, mechanicLabourCost);
                pstate2.setInt(2, jobTable.getSelectionModel().getSelectedItem().getJobID());

                // Execute the statement
                pstate2.execute();

                // Close the connection
                pstate2.close();
            }

            // If the mechanic is evans charge £125
            else if (rs.getString("mechanic").contains("Evans")) {

                // Calculate the price of the work done
                mechanicLabourCost = rs.getDouble("actualTime") * 125;

                // Prepare the query
                PreparedStatement pstate2 = state.getConnection().prepareStatement("UPDATE Jobs SET totalPrice = ? WHERE jobID == ?;");

                // Fill in all the variables to be sent inside the statement
                pstate2.setDouble(1, mechanicLabourCost);
                pstate2.setInt(2, jobTable.getSelectionModel().getSelectedItem().getJobID());

                // Execute the statement
                pstate2.execute();

                // Close the connection
                pstate2.close();
            }

            // Close the current connection
            pstate.close();
            rs.close();

            // Update the table
            extractStockData();
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Creating payment for completed jobs
    public void createPayment() {

        // Try to open the order history GUI
        try {

            // Loading the fxml of the order history
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/payment.fxml"));

            // Creating the stage to handle of the GUI
            Stage stage = new Stage();

            // Assigning the order history to the current scene
            stage.setScene(new Scene(root));

            // Setting the title of the window
            stage.setTitle("Payment");

            // Dont allow the user to resize the window
            stage.setResizable(false);

            // Style of the title bar
            stage.initStyle(StageStyle.UTILITY);

            // Show the login GUI to the user
            stage.show();
        }

        // Catch any errors
        catch (Exception exception) {
            System.err.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database
    public void extractJobData() {

        // Adding Items to the tableView
        ObservableList<database.jobs> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM Jobs;");

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                items.add(new database.jobs(
                        rs.getInt("jobID"),
                        rs.getString("status"),
                        rs.getString("descriptionWork"),
                        rs.getString("estimatedTime"),
                        rs.getString("actualTime"),
                        rs.getString("mechanic"),
                        rs.getString("vehicleRegistration"),
                        rs.getString("descriptionCarried")
                ));
            }
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        jobIDColumn.setCellValueFactory(new PropertyValueFactory<database.jobs, Integer>("jobID"));
        jobStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        jobDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionWork"));
        jobEstimatedColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedTime"));
        jobActualColumn.setCellValueFactory(new PropertyValueFactory<>("actualTime"));
        jobMechanicColumn.setCellValueFactory(new PropertyValueFactory<>("mechanic"));
        jobVehicleRegistrationColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        jobDescriptionCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionCarried"));

        // Adding the data to the table
        jobTable.setItems(items);
    }

    // Create, Edit and remove stock parts that are a part of the ledger
    public void stockParts(String stock) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // If the case meets the switch requirements it will execute it
            switch (stock) {

                // Create a new part
                case "create" -> {

                    // Prepare the query to get the job role of the user logging in
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Stock (partName, code, manufacturer, vehicleType, year, price, stockLevel) VALUES (?, ?, ?, ?, ?, ?, ?);");

                    pstate.setString(1, stockPartText.getText());
                    pstate.setString(2, stockCodeText.getText());
                    pstate.setString(3, stockManufacturerText.getText());
                    pstate.setString(4, stockVehicleText.getText());
                    pstate.setString(5, stockYearText.getText());
                    pstate.setString(6, stockPriceText.getText());
                    pstate.setString(7, stockLevelText.getText());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractStockData();

                    // Clear all the fields
                    clearText();
                }

                // Edit a existing part
                case "edit" -> {

                    // Prepare the query to get the job role of the user logging in
                    PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Stock SET partName = ?, code = ?, manufacturer = ?, vehicleType = ?, year = ?, price = ?, stockLevel = ? WHERE part_ID = ?;");

                    pstate.setString(1, stockPartText.getText());
                    pstate.setString(2, stockCodeText.getText());
                    pstate.setString(3, stockManufacturerText.getText());
                    pstate.setString(4, stockVehicleText.getText());
                    pstate.setString(5, stockYearText.getText());
                    pstate.setString(6, stockPriceText.getText());
                    pstate.setString(7, stockLevelText.getText());
                    pstate.setInt(8, stockTable.getSelectionModel().getSelectedItem().getPart_ID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractStockData();

                    // Clear all the fields
                    clearText();
                }

                // Remove a part
                case "remove" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("DELETE FROM Stock WHERE part_ID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setInt(1, stockTable.getSelectionModel().getSelectedItem().getPart_ID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractStockData();

                    // Clear all the fields
                    clearText();
                }
            }
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Update the level of the stock
    public void updateStock() {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to get the job role of the user logging in
            PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Stock SET stockLevel = ? WHERE part_ID = ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, stockTable.getSelectionModel().getSelectedItem().getStockLevel() + Integer.parseInt(stockUpdateLevelText.getText()));
            pstate.setInt(2, stockTable.getSelectionModel().getSelectedItem().getPart_ID());

            // Execute the query
            pstate.execute();

            // Update the table
            extractStockData();

            // Clear the text fields
            stockUpdateLevelText.clear();
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Open a new window containing a table of the order history
    public void vehiclesGUI() {

        vehicles.setVehicleOwnerID(customerTable.getSelectionModel().getSelectedItem().getCustomerID());

        // Try to open the order history GUI
        try {

            // Loading the fxml of the order history
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/vehicles.fxml"));

            // Creating the stage to handle of the GUI
            Stage stage = new Stage();

            // Assigning the order history to the current scene
            stage.setScene(new Scene(root));

            // Setting the title of the window
            stage.setTitle("Vehicles");

            // Dont allow the user to resize the window
            stage.setResizable(false);

            // Style of the title bar
            stage.initStyle(StageStyle.UTILITY);

            // Show the login GUI to the user
            stage.show();
        }

        // Catch any errors
        catch (Exception exception) {
            System.err.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Open a new window containing a table of the order history
    public void orderHistoryGUI() {

        // Try to open the order history GUI
        try {

            // Loading the fxml of the order history
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/orderHistory.fxml"));

            // Creating the stage to handle of the GUI
            Stage stage = new Stage();

            // Assigning the order history to the current scene
            stage.setScene(new Scene(root));

            // Setting the title of the window
            stage.setTitle("Order History");

            // Dont allow the user to resize the window
            stage.setResizable(false);

            // Style of the title bar
            stage.initStyle(StageStyle.UTILITY);

            // Show the login GUI to the user
            stage.show();
        }

        // Catch any errors
        catch (Exception exception) {
            System.err.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Ordering the parts from the stock ledger
    public void orderStock() {

        // Try to execute SQL queries
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to send to the database
            PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Stock SET stockLevel = ? WHERE code = ?");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, stockTable.getSelectionModel().getSelectedItem().getStockLevel() - 1);
            pstate.setString(2, stockTable.getSelectionModel().getSelectedItem().getCode());

            // Execute the query
            pstate.execute();

            // Prepare the second query to send to the database
            PreparedStatement pstate2 = state.getConnection().prepareStatement("INSERT INTO OrderHistory (partName, code, manufacturer, vehicleType, year, price, date) VALUES (?, ?, ?, ?, ?, ?, ?)");

            // Calculating the current date and time
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();

            // Fill in all the variables to be sent inside the statement
            pstate2.setString(1, stockTable.getSelectionModel().getSelectedItem().getPartName());
            pstate2.setString(2, stockTable.getSelectionModel().getSelectedItem().getCode());
            pstate2.setString(3, stockTable.getSelectionModel().getSelectedItem().getManufacturer());
            pstate2.setString(4, stockTable.getSelectionModel().getSelectedItem().getVehicleType());
            pstate2.setString(5, stockTable.getSelectionModel().getSelectedItem().getYear());
            pstate2.setString(6, stockTable.getSelectionModel().getSelectedItem().getPrice());
            pstate2.setString(7, dateFormat.format(calendar.getTime()));

            // Execute the query
            pstate2.execute();

            // Update the table
            extractStockData();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database
    public void extractStockData() {

        // Adding Items to the tableView
        ObservableList<database.stock> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM Stock;");

            // Loop through all the results
            while (rs.next()) {

                // Dont add any parts that have less than 0 stock
                if (rs.getInt("stockLevel") > 0) {

                    // Add the information to the items list
                    items.add(new database.stock(
                            rs.getInt("part_ID"),
                            rs.getString("partName"),
                            rs.getString("code"),
                            rs.getString("manufacturer"),
                            rs.getString("vehicleType"),
                            rs.getString("year"),
                            rs.getString("price"),
                            rs.getInt("stockLevel")
                    ));
                }
            }
        }
        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        stockPartColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        stockCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        stockManufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        stockVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        stockYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        stockPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockLevelColumn.setCellValueFactory(new PropertyValueFactory<>("stockLevel"));

        // Adding the data to the table
        stockTable.setItems(items);

        // Change the color of the row depending on the stock level
        stockTable.setRowFactory(tv -> new TableRow<stock>() {
            protected void updateItem(stock item, boolean empty) {

                // Referencing the parent class object
                super.updateItem(item, empty);

                // Check if the row is empty
                if (item == null || empty) {
                    setStyle("");
                }

                // Change the row color to green if the stock is above 20
                else if (item.getStockLevel() >= 20) {
                    setStyle("-fx-background-color: #00FF00;");
                }

                // Change the row color to orange if the stock is below 20 and above 10
                else if (item.getStockLevel() <= 20 && item.getStockLevel() >= 10) {
                    setStyle("-fx-background-color: #FFA500;");
                }

                // Change the row color to red if the stock is below 10
                else if (item.getStockLevel() <= 10) {
                    setStyle("-fx-background-color: #FF0000;");
                }
            }
        });

        // Filtering the data using the text field
        FilteredList<database.stock> filteredList = new FilteredList<>(items, b -> true);

        // Create a listener for the text field with the specific keyword
        stockSearchText.textProperty().addListener((observable, oldKeyword, newKeyword) -> {
            filteredList.setPredicate(stock -> {

                // Check if they keyword is not empty or null
                if (newKeyword.isEmpty()) {
                    return true;
                }

                // Variable to hold the users filter word
                String keyword = newKeyword.toLowerCase();

                // Check if there is a match in parts
                if (stock.getPartName().toLowerCase().contains(keyword)) {
                    return true;
                }

                // Check if there is a match in code
                else if (stock.getCode().toLowerCase().contains(keyword)) {
                    return true;
                }

                // Check if there is a match in manufacturer
                else if (stock.getManufacturer().toLowerCase().contains(keyword)) {
                    return true;
                }

                // Check if there is a match in vehicle type
                else if (stock.getVehicleType().toLowerCase().contains(keyword)) {
                    return true;
                }

                // Check if there is a match in year
                else if (stock.getYear().toLowerCase().contains(keyword)) {
                    return true;
                }

                // No match in the database
                else {
                    return false;
                }
            });
        });

        // Add the data to the sorted list
        SortedList<database.stock> sortedStock = new SortedList<>(filteredList);

        // Binding the sorted results to the table
        sortedStock.comparatorProperty().bind(stockTable.comparatorProperty());

        // Update the filter and the table
        stockTable.setItems(sortedStock);
    }

    // Creating the Invoice
    public void createInvoice() {

        // Generate the mechanic price
        generateMechanicPrice();

        // Generate the parts price
        generatePartsPrice();

        // Calculating all the final variables
        calculateFinale();

        // Try to execute this statements
        try {

        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }

    }

//    // Creating the invoice
//    public void createInvoice() {
//
//        // Generate the mechanic price
//        generateMechanicPrice();
//
//        // Generate the parts price
//        generatePartsPrice();
//
//        // Calculating all the final variables
//        calculateFinale();
//
//        // Try to execute everything and catch any related errors
//        try {
//
//            // Create a statement
//            Statement state = database.database.connection().createStatement();
//
//            // Prepare the query to get the customerID
//            PreparedStatement ps = state.getConnection().prepareStatement("SELECT customerID FROM Jobs WHERE jobID == ?;");
//
//            // Fill in all the variables to be sent inside the statement
//            ps.setInt(1, jobTable.getSelectionModel().getSelectedItem().getJobID());
//
//            // Saving the results to a result set
//            ResultSet rs2 = ps.executeQuery();
//
//            // Prepare the query to get the job role of the user logging in
//            PreparedStatement pstate = state.getConnection().prepareStatement("""
//                    SELECT
//                        Customer.customerID,
//                        Customer.name,
//                        Customer.buildingNumber,
//                        Customer.streetAddress,
//                        Customer.city,
//                        Customer.postcode,
//                        Vehicles.registrationPlate,
//                        Vehicles.vehicleName,
//                        Jobs.descriptionCarried,
//                        JobParts.partName,
//                        JobParts.partNumber,
//                        JobParts.partPrice
//
//                    FROM
//                        Customer,
//                        Vehicles,
//                        Jobs,
//                        JobParts
//
//                    WHERE
//                        Customer.customerID == ? AND Vehicles.vehicleOwnerID == ? AND Jobs.customerID == ?
//            """);
//
//            // Fill in all the variables to be sent inside the statement
//            pstate.setInt(1, rs2.getInt("customerID"));
//            pstate.setInt(2, rs2.getInt("customerID"));
//            pstate.setInt(3, rs2.getInt("customerID"));
//
//            // Save the results from the database to the ResultSet
//            ResultSet rs = pstate.executeQuery();
//
//            // Check if the query exists
//            PreparedStatement statement = state.getConnection().prepareStatement("SELECT 1 FROM Invoice, Jobs WHERE Invoice.customerID = ?;");
//            statement.setInt(1, rs2.getInt("customerID"));
//            ResultSet result = statement.executeQuery();
//
//            // Check if the record already exists
//            if(result.next()) {
//
//                // Generate the report for the completed job
//                report.createReport(rs2.getInt("customerID"), jobTable.getSelectionModel().getSelectedItem().getJobID());
//            }
//
//            // If the record doesn't exist make one
//            else {
//
//                // Prepare the query to get the job role of the user logging in
//                PreparedStatement pstate2 = state.getConnection().prepareStatement("INSERT INTO Invoice (customerID, name, buildingNumber, streetAddress, city, postcode, registrationPlate, vehicleName, descriptionCarried) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
//
//                // Fill in all the variables to be sent inside the statement
//                pstate2.setInt(1, rs.getInt("customerID"));
//                pstate2.setString(2, rs.getString("name"));
//                pstate2.setString(3, rs.getString("buildingNumber"));
//                pstate2.setString(4, rs.getString("streetAddress"));
//                pstate2.setString(5, rs.getString("city"));
//                pstate2.setString(6, rs.getString("postcode"));
//                pstate2.setString(7, rs.getString("registrationPlate"));
//                pstate2.setString(8, rs.getString("vehicleName"));
//                pstate2.setString(9, rs.getString("descriptionCarried"));
//
//                // Execute the query
//                pstate2.execute();
//
//                // Close the connection
//                pstate.close();
//
//                // Generate the report for the completed job
//                report.createReport(rs2.getInt("customerID"), jobTable.getSelectionModel().getSelectedItem().getJobID());
//            }
//
//            // Close the current connection
//            ps.close();
//            rs2.close();
//            pstate.close();
//            rs.close();
//            statement.close();
//            result.close();
//        }
//
//        // Catch any related errors
//        catch (Exception exception) {
//            System.out.println("[ERROR]#~ " + exception.getMessage());
//        }
//    }

    // Create, Edit and remove customer related accounts
    public void createBooking(String booking) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Assigning the datepicker to an actual date format
            LocalDate bookingDate = bookingDatePicker.getValue();

            // Check if the booking type is MOT
            if (motCheckbox.isSelected()) {
                bookingType = "MOT";
                bookingDescriptionText.setText("MOT");
            }

            // Check if the booking type is Service
            else if (serviceCheckbox.isSelected()) {
                bookingType = "Service";
                bookingDescriptionText.setText("Service");
            }

            // If non of the booking type is selected choose repair
            else if (repairCheckbox.isSelected()){
                bookingType = "Repair";
            }

            // If the case meets the switch requirements it will execute it
            switch (booking) {

                // Create a new booking for walk ins
                case "create-walk" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Booking (fullName, mobile, vehicleRegistration, bookingType, bookingDate, descriptionWork, customerType) VALUES (?, ?, ?, ?, ?, ?, ?);");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, bookingNameText.getText());
                    pstate.setString(2, bookingMobileText.getText());
                    pstate.setString(3, bookingVehicleCombo.getSelectionModel().getSelectedItem().toString());
                    pstate.setString(4, bookingType);
                    pstate.setString(5, bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    pstate.setString(6, bookingDescriptionText.getText());
                    pstate.setString(7, "Walk-In");

                    // Execute the query
                    pstate.execute();

                    // Prepare the 2nd query to be sent to the database
                    PreparedStatement pstate2 = state.getConnection().prepareStatement("INSERT INTO Jobs (customerID, status, descriptionWork, estimatedTime, vehicleRegistration, bookingType) VALUES (?, ?, ?, ?, ?, ?);");

                    // Fill in all the variables to be sent inside the 2nd statement
                    pstate2.setString(1, bookingNameText.getText());
                    pstate2.setString(2, "Waiting");
                    pstate2.setString(3, bookingDescriptionText.getText());
                    pstate2.setString(4, "2");
                    pstate2.setString(5, bookingVehicleCombo.getSelectionModel().getSelectedItem());
                    pstate2.setString(6, bookingType);

                    // Execute the 2nd query
                    pstate2.execute();

                    // Update the table
                    extractBookingData();

                    // Clear the text fields
                    clearText();

                    // Close the current connections
                    pstate.close();
                    pstate2.close();
                }

                // Create a new booking
                case "create" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Booking (fullName, mobile, vehicleRegistration, bookingType, bookingDate, descriptionWork) VALUES (?, ?, ?, ?, ?, ?);");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, bookingNameText.getText());
                    pstate.setString(2, bookingMobileText.getText());
                    pstate.setString(3, bookingVehicleCombo.getSelectionModel().getSelectedItem().toString());
                    pstate.setString(4, bookingType);
                    pstate.setString(5, bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    pstate.setString(6, bookingDescriptionText.getText());

                    // Execute the query
                    pstate.execute();

                    // Prepare the query to send to the database
                    PreparedStatement pstate1 = state.getConnection().prepareStatement("SELECT customerID FROM Customer WHERE name == ?");

                    // Fill in the variable to be sent inside the query
                    pstate1.setString(1, bookingNameText.getText());

                    // Save all the results to the result set
                    ResultSet rs = pstate1.executeQuery();

                    // Prepare the 2nd query to be sent to the database
                    PreparedStatement pstate2 = state.getConnection().prepareStatement("INSERT INTO Jobs (customerID, status, descriptionWork, estimatedTime, vehicleRegistration) VALUES (?, ?, ?, ?, ?);");

                    // Fill in all the variables to be sent inside the 2nd statement
                    pstate2.setInt(1, rs.getInt("customerID"));
                    pstate2.setString(2, "Waiting");
                    pstate2.setString(3, bookingDescriptionText.getText());
                    pstate2.setString(4, "2");
                    pstate2.setString(5, bookingVehicleCombo.getSelectionModel().getSelectedItem());

                    // Execute the 2nd query
                    pstate2.execute();

                    // Update the table
                    extractBookingData();

                    // Clear the text fields
                    clearText();

                    // Close the connection
                    pstate1.close();
                    pstate.close();
                    rs.close();
                }

                // Edit a existing booking
                case "edit" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Booking SET fullname = ?, mobile = ?, vehicleRegistration = ?, bookingType = ?, bookingDate = ?, descriptionWork = ? WHERE bookingID == ?;");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, bookingNameText.getText());
                    pstate.setString(2, bookingMobileText.getText());
                    pstate.setString(3, bookingVehicleCombo.getSelectionModel().getSelectedItem().toString());
                    pstate.setString(4, bookingType);
                    pstate.setString(5, bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    pstate.setString(6, customerHomeText.getText());
                    pstate.setString(7, bookingDescriptionText.getText());

                    // Condition on which user is getting their details changed
                    pstate.setInt(8, bookingTable.getSelectionModel().getSelectedItem().getBookingID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractBookingData();

                    // Clear the text fields
                    clearText();

                    // Close the current connection
                    pstate.close();
                }

                // Remove a existing booking
                case "remove" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("DELETE FROM Booking WHERE bookingID == ?;");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setInt(1, bookingTable.getSelectionModel().getSelectedItem().getBookingID());

                    // Execute the query
                    pstate.execute();

                    // Prepare the query to send to the database
                    PreparedStatement pstate2 = state.getConnection().prepareStatement("DELETE FROM Jobs WHERE vehicleRegistration == ?;");

                    // Fill in all the variables to be sent inside the statement
                    pstate2.setString(1, bookingTable.getSelectionModel().getSelectedItem().getVehicleRegistration());

                    // Execute the query
                    pstate2.execute();

                    // Update the table
                    extractBookingData();

                    // Clear the text fields
                    clearText();

                    // Close the current connection
                    pstate.close();
                    pstate2.close();
                }
            }
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database for the booking table
    public void extractBookingData() {

        // Adding Items to the tableView
        ObservableList<database.booking> bookingItems = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM Booking;");

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                bookingItems.add(new database.booking(
                        rs.getInt("bookingID"),
                        rs.getString("fullName"),
                        rs.getString("mobile"),
                        rs.getString("vehicleRegistration"),
                        rs.getString("bookingType"),
                        rs.getString("bookingDate"),
                        rs.getString("descriptionWork"),
                        rs.getString("customerType")
                ));
            }

            // Close the current connection
            rs.close();
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        bookingNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        bookingMobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        bookingVehicleRegistrationColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        bookingTypeColumn.setCellValueFactory(new PropertyValueFactory<>("bookingType"));
        bookingDateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        bookingDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionWork"));
        bookingCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerType"));

        // Adding the data to the table
        bookingTable.setItems(bookingItems);
    }

    // Create, Edit and remove customer related accounts
    public void createCustomer(String customer) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // If the case meets the switch requirements it will execute it
            switch (customer) {

                // Create a new customer account
                case "create" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Customer (name, buildingNumber, streetAddress, city, postcode, home, mobile) VALUES (?, ?, ?, ?, ?, ?, ?)");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, customerNameText.getText());
                    pstate.setString(2, customerBuildingText.getText());
                    pstate.setString(3, customerStreetText.getText());
                    pstate.setString(4, customerCityText.getText());
                    pstate.setString(5, customerPostcodeText.getText());
                    pstate.setString(6, customerHomeText.getText());
                    pstate.setString(7, customerMobileText.getText());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();

                    // Clear the text fields
                    clearText();

                    // Close the current connection
                    pstate.close();
                }

                // Edit a existing customer account
                case "edit" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Customer SET name = ?, buildingNumber = ?, streetAddress = ?, city = ?, postcode = ?, home = ?, mobile = ? WHERE customerID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, customerNameText.getText());
                    pstate.setString(2, customerBuildingText.getText());
                    pstate.setString(3, customerStreetText.getText());
                    pstate.setString(4, customerCityText.getText());
                    pstate.setString(5, customerPostcodeText.getText());
                    pstate.setString(6, customerHomeText.getText());
                    pstate.setString(7, customerMobileText.getText());

                    // Condition on which user is getting their details changed
                    pstate.setInt(8, customerTable.getSelectionModel().getSelectedItem().getCustomerID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();

                    // Clear the text fields
                    clearText();

                    // Close the current connection
                    pstate.close();
                }

                // Remove a existing customer account
                case "remove" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("DELETE FROM Customer WHERE customerID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setInt(1, customerTable.getSelectionModel().getSelectedItem().getCustomerID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();

                    // Clear the text fields
                    clearText();

                    // Close the current connection
                    pstate.close();
                }
            }
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database for the account table
    public void extractCustomerData() {

        // Adding Items to the tableView
        ObservableList<database.customer> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM Customer;");

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                items.add(new database.customer(
                        rs.getInt("customerID"),
                        rs.getString("name"),
                        rs.getString("buildingNumber"),
                        rs.getString("streetAddress"),
                        rs.getString("city"),
                        rs.getString("postcode"),
                        rs.getString("home"),
                        rs.getString("mobile")
                ));
            }

            // Close the current connection
            rs.close();
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerBuildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        customerStreetColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        customerCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerPostcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        customerHomeColumn.setCellValueFactory(new PropertyValueFactory<>("home"));
        customerMobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        // Adding the data to the table
        customerTable.setItems(items);
    }

    // Handle all the actions for creating a customer and employee accounts
    public void handleAccounts() {

        // When the user presses the add account it will create a new account
        customerAddItem.setOnAction(event -> {
            createCustomer("create");
        });

        // When the user presses the edit account it will edit the account
        customerEditItem.setOnAction(event -> {
            createCustomer("edit");
        });

        // When the user presses the remove button on a account It will be deleted from the database
        customerRemoveItem.setOnAction(event -> {
            createCustomer("remove");
        });

        // When the user presses the add account it will create a new account
        bookingAddItem.setOnAction(event -> {
            createBooking("create");
        });

        // When the user presses the edit account it will edit the account
        bookingEditItem.setOnAction(event -> {
            createBooking("edit");
        });

        // When the user presses the remove button on a account It will be deleted from the database
        bookingRemoveItem.setOnAction(event -> {
            createBooking("remove");
        });

        // When the user presses the add part it will create a new part
        stockAddItem.setOnAction(event -> {
            stockParts("create");
        });

        // When the user presses the create button it will create a walk-in booking
        walkBookingButton.setOnAction(event -> {
            createBooking("create-walk");
        });

        // When the user presses the add part it will edit a part
        stockEditItem.setOnAction(event -> {
            stockParts("edit");
        });

        // When the user presses the add part it remove a part
        stockRemoveItem.setOnAction(event -> {
            stockParts("remove");
        });

        // Sets the actions of the make payment button
        makePaymentButton.setOnAction(event -> {

            // Generate the mechanic price
            generateMechanicPrice();

            // Generate the parts price
            generatePartsPrice();

            // Calculating all the final variables
            calculateFinale();

            // Open the payment GUI
            createPayment();
        });
    }

    // Extract data from the database
    public void getCustomerVehicles() {

        // Create a array to hold all the vehicles registration numbers
        ObservableList<String> customerVehicles = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT registrationPlate FROM Vehicles WHERE vehicleOwnerID == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, customerTable.getSelectionModel().getSelectedItem().getCustomerID());

            // Save the results from the database to the ResultSet
            ResultSet rs = pstate.executeQuery();

            // Clear the array before adding more entries
            customerVehicles.clear();

            // Loop through all the results
            while (rs.next()) {

                // Add the registration plates to the array
                customerVehicles.add(rs.getString("registrationPlate"));
            }

            // Adding all the items in the observable list to the combo box
            bookingVehicleCombo.setItems(customerVehicles);

            // Close the current connection
            pstate.close();
            rs.close();
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println();
        }
    }

    // Changing the scenes when the user presses the button
    public void changePane(ActionEvent event) {

        // If the user presses the dashboard button change the scene
        if (event.getSource().toString().contains(dashboardButton.getText())) {

            // Try to execute the code and catch any related errors
            try {

                // Put the pane in front of all the other panes
                dashboardPane.toFront();
            }

            // Catch any errors
            catch (Exception exception) {}
        }

        // If the user presses the account button change the scene
        else if (event.getSource().toString().contains(accountButton.getText())) {

            // Try to execute the code and catch any related errors
            try {

                // Put the pane in front of all the other panes
                accountPane.toFront();

                // Extract all the customer's data into the table
                extractCustomerData();
            }

            // Catch any errors
            catch (Exception exception) {}
        }

        // If the user presses the booking button change the scene
        else if (event.getSource().toString().contains(bookingButton.getText())) {

            // Try to execute the code and catch any related errors
            try {

                // Put the pane in front of all the other panes
                bookingPane.toFront();

                // Extract all the customer's data into the table
                extractBookingData();

                // Extract the customer's vehicles to the combo box
                getCustomerVehicles();
            }

            // Catch any errors
            catch (Exception exception) {}

        }

        // If the user presses the stock control button change the scene
        else if (event.getSource().toString().contains(stockButton.getText())) {

            // Try to execute the code and catch any related errors
            try {

                // Put the pane in front of all the other panes
                stockPane.toFront();

                // Extract all the stock control data into the table
                extractStockData();
            }

            // Catch any errors
            catch (Exception exception) {}
        }

        // If the user presses the job list button change the scene
        else if (event.getSource().toString().contains(jobListButton.getText())) {

            // Try to execute the code and catch any related errors
            try {

                // Put the pane in front of all the other panes
                jobListPane.toFront();

                // Extract all the stock control data into the table
                extractJobData();
            }

            // Catch any errors
            catch (Exception exception) {}
        }
    }

    // Get the account details once user presses the row
    public void selectAccount(MouseEvent event) {

        // Try to transfer data from the selected row to the text fields
        try {

            // Check if the user is pressing on customer table
            if (event.getSource().toString().contains("customerTable")) {

                // Insert the data from the table to the text fields for the customers data
                customerNameText.setText(customerTable.getSelectionModel().getSelectedItem().getName());
                customerBuildingText.setText(customerTable.getSelectionModel().getSelectedItem().getBuildingNumber());
                customerStreetText.setText(customerTable.getSelectionModel().getSelectedItem().getStreetAddress());
                customerMobileText.setText(customerTable.getSelectionModel().getSelectedItem().getMobile());
                customerCityText.setText(customerTable.getSelectionModel().getSelectedItem().getCity());
                customerPostcodeText.setText(customerTable.getSelectionModel().getSelectedItem().getPostcode());
                customerHomeText.setText(customerTable.getSelectionModel().getSelectedItem().getHome());

                bookingNameText.setText(customerTable.getSelectionModel().getSelectedItem().getName());
                bookingBuildingText.setText(customerTable.getSelectionModel().getSelectedItem().getBuildingNumber());
                bookingStreetText.setText(customerTable.getSelectionModel().getSelectedItem().getStreetAddress());
                bookingMobileText.setText(customerTable.getSelectionModel().getSelectedItem().getMobile());
                bookingCityText.setText(customerTable.getSelectionModel().getSelectedItem().getCity());
                bookingPostcodeText.setText(customerTable.getSelectionModel().getSelectedItem().getPostcode());
                bookingHomeText.setText(customerTable.getSelectionModel().getSelectedItem().getHome());
            }

            // Check if the user is pressing on the stock table
            else if (event.getSource().toString().contains("stockTable")) {

                // Insert the data from the table to the text fields for the stock data
                stockPartText.setText(stockTable.getSelectionModel().getSelectedItem().getPartName());
                stockManufacturerText.setText(stockTable.getSelectionModel().getSelectedItem().getManufacturer());
                stockYearText.setText(stockTable.getSelectionModel().getSelectedItem().getYear());
                stockCodeText.setText(stockTable.getSelectionModel().getSelectedItem().getCode());
                stockVehicleText.setText(stockTable.getSelectionModel().getSelectedItem().getVehicleType());
                stockPriceText.setText(stockTable.getSelectionModel().getSelectedItem().getPrice());
                stockLevelText.setText(String.valueOf(stockTable.getSelectionModel().getSelectedItem().getStockLevel()));
            }

            // Check if the user is pressing on the booking table
            else if (event.getSource().toString().contains("bookingTable")) {

                // Create a statement
                Statement state = database.database.connection().createStatement();

                // Prepare the query to send to the database
                PreparedStatement pstate = state.getConnection().prepareStatement("SELECT bookingType FROM Booking WHERE bookingID = ?");

                // Fill in the query
                pstate.setInt(1, bookingTable.getSelectionModel().getSelectedItem().getBookingID());

                // Create a result set to hold all the results
                ResultSet rs = pstate.executeQuery();

                // Insert the data from the table to the text fields for the booking data
                bookingNameText.setText(bookingTable.getSelectionModel().getSelectedItem().getFullName());
                bookingMobileText.setText(bookingTable.getSelectionModel().getSelectedItem().getMobile());

                // Check what booking type the user has
                switch (rs.getString("bookingType")) {

                    // Check if the booking type is MOT
                    case "MOT" -> {
                        motCheckbox.setSelected(true);
                        serviceCheckbox.setSelected(false);
                        repairCheckbox.setSelected(false);
                    }

                    // Check if the booking type is Service
                    case "Service" -> {
                        motCheckbox.setSelected(false);
                        serviceCheckbox.setSelected(true);
                        repairCheckbox.setSelected(false);
                    }

                    // Check if the booking type is Repair
                    case "Repair" -> {
                        motCheckbox.setSelected(false);
                        serviceCheckbox.setSelected(false);
                        repairCheckbox.setSelected(true);
                    }
                }

                // Get the data from the booking table and transfer it to the text fields
                bookingDatePicker.getEditor().setText(bookingTable.getSelectionModel().getSelectedItem().getBookingDate());
                bookingDescriptionText.setText(bookingTable.getSelectionModel().getSelectedItem().getDescriptionWork());
                bookingVehicleCombo.valueProperty().setValue(bookingTable.getSelectionModel().getSelectedItem().getVehicleRegistration());

                // Close the current connection
                pstate.close();
                rs.close();
            }
        }

        // Catch any errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Clear the text when the user presses clear
    public void clearText() {

        // Try to clear all the text fields
        try {

            // Clearing all the text fields
            customerNameText.clear();
            customerBuildingText.clear();
            customerStreetText.clear();
            customerMobileText.clear();
            customerCityText.clear();
            customerPostcodeText.clear();
            customerHomeText.clear();

            // Clearing all the booking text fields
            bookingNameText.clear();
            bookingBuildingText.clear();
            bookingStreetText.clear();
            bookingMobileText.clear();
            bookingCityText.clear();
            bookingPostcodeText.clear();
            bookingHomeText.clear();

            // Clearing all the stock text fields
            stockPartText.clear();
            stockManufacturerText.clear();
            stockYearText.clear();
            stockCodeText.clear();
            stockVehicleText.clear();
            stockPriceText.clear();
            stockLevelText.clear();

            // Clearing all the booking text fields
            bookingNameText.clear();
            bookingBuildingText.clear();
            bookingStreetText.clear();
            bookingCityText.clear();
            bookingPostcodeText.clear();
            bookingHomeText.clear();
            bookingMobileText.clear();

            motCheckbox.setSelected(false);
            serviceCheckbox.setSelected(false);
            repairCheckbox.setSelected(false);

            bookingDatePicker.getEditor().clear();
            bookingDescriptionText.clear();
            bookingVehicleCombo.valueProperty().set("");
        }

        // Catch any errors
        catch (Exception exception) {
        }
    }

    // Logout the user from GUI and go back to the main login screen
    public void logoutAccount() {

        // Use the logout subroutine from the admin java class
        admin.logoutAccount();
    }
}