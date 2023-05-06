package controllers;

// Importing essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

// Controlling the order history GUI
public class stock implements Initializable {

    // Stock table from FXML
    @FXML
    private TableView<database.stock> stockTable;

    @FXML
    private TableColumn<database.stock, String> stockPartColumn, stockCodeColumn, stockManufacturerColumn, stockVehicleColumn, stockYearColumn, stockPriceColumn;

    @FXML
    private TableColumn<database.stock, Integer> stockLevelColumn;

    // Text field from FXML
    @FXML
    private TextField stockSearchText;

    // Referencing the reception class
    reception reception = new reception();

    // Subroutine automatically runs everytime this file gets executed
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Execute the subroutine to get the data from the database to the table view
        extractStockData();
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
        stockTable.setRowFactory(tv -> new TableRow<database.stock>() {
            protected void updateItem(database.stock item, boolean empty) {

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
            PreparedStatement pstate2 = state.getConnection().prepareStatement("INSERT INTO JobParts (jobID, partName, partNumber, partPrice) VALUES (?, ?, ?, ?);");

            // Fill in all the variables to be sent inside the statement
            pstate2.setInt(1, mechanic.getJobID());
            pstate2.setString(2, stockTable.getSelectionModel().getSelectedItem().getPartName());
            pstate2.setString(3, stockTable.getSelectionModel().getSelectedItem().getCode());
            pstate2.setString(4, stockTable.getSelectionModel().getSelectedItem().getPrice());

            // Execute the query
            pstate2.execute();

            // Close the current connection
            pstate.close();
            pstate2.close();

            // Update the table
            extractStockData();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Get the account details once user presses the row
    public void selectAccount(MouseEvent event) {

        // Try to transfer data from the selected row to the text fields
        try {

            // Check if the user is pressing on the stock table
            if (event.getSource().toString().contains("stockTable")) {

                // Insert the data from the table to the text fields for the stock data
                stockPartColumn.setText(stockTable.getSelectionModel().getSelectedItem().getPartName());
                stockCodeColumn.setText(stockTable.getSelectionModel().getSelectedItem().getCode());
                stockManufacturerColumn.setText(stockTable.getSelectionModel().getSelectedItem().getManufacturer());
                stockVehicleColumn.setText(stockTable.getSelectionModel().getSelectedItem().getVehicleType());
                stockYearColumn.setText(stockTable.getSelectionModel().getSelectedItem().getYear());
                stockPriceColumn.setText(stockTable.getSelectionModel().getSelectedItem().getPrice());
            }
        }

        // Catch any errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }
}