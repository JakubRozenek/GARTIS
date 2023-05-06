package controllers;

// Importing essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

// Controlling the order history GUI
public class orderHistory implements Initializable {

    // Assigning the table from fxml
    @FXML
    private TableView<database.orderHistory> historyTable;

    // Assigning the string columns from fxml
    @FXML
    private TableColumn<database.orderHistory, String> partNameColumn, codeColumn, manufacturerColumn, vehicleTypeColumn, yearColumn, priceColumn, dateColumn;

    // Subroutine automatically runs everytime this file gets executed
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Load all the data into the table
        extractData();
    }

    // Extract data from the database
    public void extractData() {

        // Adding Items to the tableView
        ObservableList<database.orderHistory> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = (Statement) database.database.connection().createStatement();

            // Execute the specific query
            ResultSet rs = state.executeQuery("SELECT * FROM OrderHistory;");

            // Loop through all the results
            while (rs.next()) {

                // Add the information to the items list
                items.add(new database.orderHistory(
                        rs.getString("part_ID"),
                        rs.getString("partName"),
                        rs.getString("code"),
                        rs.getString("manufacturer"),
                        rs.getString("vehicleType"),
                        rs.getString("year"),
                        rs.getString("price"),
                        rs.getString("date")
                ));
            }
        }

        // Catch any SQL related errors
        catch (SQLException exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

        // Assign all items to the matching columns in the table
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Adding the data to the table
        historyTable.setItems(items);
    }
}