package controllers;

// Importing all the essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.main;

import java.io.File;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

// Subroutine controlling the GUI of the admin
public class admin implements Initializable {

    // Different Pane Scenes from FXML
    @FXML
    private Pane dashboardPane, accountPane, employeePane, databasePane;

    // Menu Buttons from FXML
    @FXML
    private Button dashboardButton, accountButton, employeeButton, databaseButton;

    // Employee table from FXML
    @FXML
    private TableView<database.employee> employeeTable;

    @FXML
    private TableColumn<database.employee, String> employeeNameColumn, employeeJobRoleColumn, employeeUsernameColumn, employeePasswordColumn;

    // Customer table from FXML
    @FXML
    private TableView<database.customer> customerTable;

    @FXML
    private TableColumn<database.customer, String> customerNameColumn, buildingColumn, streetColumn, cityColumn, postcodeColumn, homeColumn, mobileColumn;

    // Text fields from FXML
    @FXML
    private TextField nameText, buildingText, streetText, mobileText, cityText, postcodeText, homeText, fullNameText, usernameText, passwordText;

    // Menu items from FXML
    @FXML
    private MenuItem customerAddItem, customerEditItem, customerRemoveItem, employeeAddItem, employeeEditItem, employeeRemoveItem;

    // Split menu from FXML
    @FXML
    private SplitMenuButton accountSplitMenu;

    // Combo box from FXML
    @FXML
    private ComboBox<String> jobRoleCombo;

    // Variable holding the username of the user that is currently logged in
    private static String username;

    // Subroutine automatically runs everytime this file gets executed
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // When the user starts the program he will go to the dashboard
        dashboardPane.toFront();

        // Handle all the action events for customer
        handleAccounts();

        // Set the account split menu text according to the name of the user that is logged in
        accountSplitMenu.setText(getUsername());

        // Adding items to the combo box for the employee scene
        jobRoleCombo.getItems().addAll(
                "Administrator",
                "Foreman",
                "Franchisee",
                "Mechanic",
                "Receptionist"
        );
    }

    // Create, Edit and remove customer related accounts
    public void createCustomer(String customer) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            switch (customer) {

                // Create a new customer account
                case "create" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Customer (name, buildingNumber, streetAddress, city, postcode, home, mobile) VALUES (?, ?, ?, ?, ?, ?, ?)");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, nameText.getText());
                    pstate.setString(2, buildingText.getText());
                    pstate.setString(3, streetText.getText());
                    pstate.setString(4, cityText.getText());
                    pstate.setString(5, postcodeText.getText());
                    pstate.setString(6, homeText.getText());
                    pstate.setString(7, mobileText.getText());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();
                }

                // Edit a existing customer account
                case "edit" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Customer SET name = ?, buildingNumber = ?, streetAddress = ?, city = ?, postcode = ?, home = ?, mobile = ? WHERE customerID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, nameText.getText());
                    pstate.setString(2, buildingText.getText());
                    pstate.setString(3, streetText.getText());
                    pstate.setString(4, cityText.getText());
                    pstate.setString(5, postcodeText.getText());
                    pstate.setString(6, homeText.getText());
                    pstate.setString(7, mobileText.getText());

                    // Condition on which user is getting their details changed
                    pstate.setInt(8, customerTable.getSelectionModel().getSelectedItem().getCustomerID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();
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
                }
            }
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database
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
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingNumber"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("streetAddress"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        postcodeColumn.setCellValueFactory(new PropertyValueFactory<>("postcode"));
        homeColumn.setCellValueFactory(new PropertyValueFactory<>("home"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        // Adding the data to the table
        customerTable.setItems(items);
    }

    // Create, Edit and Remove employee accounts
    public void createEmployee(String employee) {

        // Try to execute everything and catch any related errors
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            switch (employee) {

                // Create a new employee account
                case "create" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("INSERT INTO Employee (fullName, jobRole, username, password) VALUES (?, ?, ?, ?)");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, fullNameText.getText());
                    pstate.setString(2, jobRoleCombo.getValue());
                    pstate.setString(3, usernameText.getText());
                    pstate.setString(4, passwordText.getText());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractEmployeeData();
                }

                // Edit a existing employee account
                case "edit" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Employee SET fullName = ?, jobRole = ?, username = ?, password = ? WHERE employeeID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setString(1, fullNameText.getText());
                    pstate.setString(2, jobRoleCombo.getValue());
                    pstate.setString(3, usernameText.getText());
                    pstate.setString(4, passwordText.getText());

                    // Condition on which user is getting their details changed
                    pstate.setInt(5, employeeTable.getSelectionModel().getSelectedItem().getEmployeeID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractEmployeeData();
                }

                // Remove a existing customer account
                case "remove" -> {

                    // Prepare the query to send to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("DELETE FROM Employee WHERE employeeID = ?");

                    // Fill in all the variables to be sent inside the statement
                    pstate.setInt(1, employeeTable.getSelectionModel().getSelectedItem().getEmployeeID());

                    // Execute the query
                    pstate.execute();

                    // Update the table
                    extractCustomerData();
                }
            }
        }

        // Catch any related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database
    public void extractEmployeeData() {

        // Adding Items to the tableView
        ObservableList<database.employee> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM Employee;");

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                items.add(new database.employee(
                        rs.getInt("employeeID"),
                        rs.getString("fullName"),
                        rs.getString("jobRole"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        employeeJobRoleColumn.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        employeeUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        employeePasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        // Adding the data to the table
        employeeTable.setItems(items);
    }

    // Create a backup of the database
    public void createBackup() {

        // Try to save a backup of the database
        try {

            // Location of the original file
            File file = new File("src/database/database.db");

            // Assign file chooser library to a variable
            FileChooser fileChooser = new FileChooser();

            // Setting the title of the window
            fileChooser.setTitle("Save");

            // Setting the default name for the save file
            fileChooser.setInitialFileName("databaseBACKUP.db");

            // Setting the directory inside of the database folder
            fileChooser.setInitialDirectory(new File(String.valueOf(file.getParentFile())));

            // Filtering so you can only see the database files
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database Files", "*.db"));

            // Run the file chooser
            fileChooser.showSaveDialog(new Stage());
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception);
        }
    }

    // Restore the database to the old database
    public void restoreDatabase() {

    }

    // Get the account details once user presses the row
    public void selectAccount(MouseEvent event) {

        // Try to transfer data from the selected row to the text fields
        try {

            // Check if the user is pressing on customer table
            if (event.getSource().toString().contains("customerTable")) {

                // Insert the data from the table to the text fields for the customers data
                nameText.setText(customerTable.getSelectionModel().getSelectedItem().getName());
                buildingText.setText(customerTable.getSelectionModel().getSelectedItem().getBuildingNumber());
                streetText.setText(customerTable.getSelectionModel().getSelectedItem().getStreetAddress());
                mobileText.setText(customerTable.getSelectionModel().getSelectedItem().getMobile());
                cityText.setText(customerTable.getSelectionModel().getSelectedItem().getCity());
                postcodeText.setText(customerTable.getSelectionModel().getSelectedItem().getPostcode());
                homeText.setText(customerTable.getSelectionModel().getSelectedItem().getHome());
            }

            // If the table is not the customer table do the employee table instead
            else {

                // Insert the data from the table to the text fields for the employees data
                fullNameText.setText(employeeTable.getSelectionModel().getSelectedItem().getFullName());
                jobRoleCombo.getSelectionModel().select(employeeTable.getSelectionModel().getSelectedItem().getJobRole());
                usernameText.setText(employeeTable.getSelectionModel().getSelectedItem().getUsername());
                passwordText.setText(employeeTable.getSelectionModel().getSelectedItem().getPassword());
            }
        }

        // Catch any errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
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
        employeeAddItem.setOnAction(event -> {
            createEmployee("create");
        });

        // When the user presses the edit account it will edit the account
        employeeEditItem.setOnAction(event -> {
            createEmployee("edit");
        });

        // When the user presses the remove button on a account It will be deleted from the database
        employeeRemoveItem.setOnAction(event -> {
            createEmployee("remove");
        });
    }

    // Changing the scenes when the user presses the button
    public void changePane(ActionEvent event) {

        // If the user presses the dashboard button change the scene
        if (event.getSource().toString().contains(dashboardButton.getText())) {

            // Put the pane in front of all the other panes
            dashboardPane.toFront();
        }

        // If the user presses the account button change the scene
        else if (event.getSource().toString().contains(accountButton.getText())) {

            // Put the pane in front of all the other panes
            accountPane.toFront();

            // Extract all the customer's data into the table
            extractCustomerData();
        }

        // If the user presses the account button change the scene
        else if (event.getSource().toString().contains(employeeButton.getText())) {

            // Put the pane in front of all the other panes
            employeePane.toFront();

            // Extract all the customer's data into the table
            extractEmployeeData();
        }

        // If the user presses the account button change the scene
        else if (event.getSource().toString().contains(databaseButton.getText())) {

            // Put the pane in front of all the other panes
            databasePane.toFront();
        }
    }

    // Logout the user from GUI and go back to the main login screen
    public void logoutAccount() {

        // Try to logout and catch any errors
        try {

            // Create a alert for logging out
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

            // Text of the alert
            alert.setTitle("Logout");
            alert.setHeaderText("Your about to be logged out!");
            alert.setContentText("Do you want to log out?");

            // Check if the user presses ok to logout
            if (alert.showAndWait().get() == ButtonType.OK) {

                // Change the scene to the login scene
                main main = new main();
                main.changeScenes("/fxml/login.fxml","Login", 600, 400);
            }
        }

        // Catch any errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Getter for the username that is currently logged in
    public static String getUsername() {
        return username;
    }

    // Setter for the login class to set the username
    public void setUsername(String username) {
        admin.username = username;
    }
}