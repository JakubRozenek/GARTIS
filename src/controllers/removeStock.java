package controllers;

// Importing essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.components.iconlabel.IconTextFieldFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

// Controlling the order history GUI
public class removeStock {

    // Job Parts table from FXML
    @FXML
    private TableView<database.parts> jobPartsTable;

    @FXML
    private TableColumn<database.parts, String> jobPartNameColumn, jobPartNumberColumn;

    @FXML
    private TableColumn<database.parts, Integer> jobPartsIDColumn;


    // Change the jobID depending on the fxml file being executed
    public void chooseFXML(String fxml) {

        // Check what fxml is being executed
        switch (fxml) {

            // If the parameter equal to foreperson
            case "foreperson" -> {
                extractPartsData("foreperson");
            }

            // If the parameter equal to mechanic
            case "mechanic" -> {
                extractPartsData("mechanic");
            }
        }
    }

    // Remove the stock from database and table
    public void jobRemoveParts() {

        // Try to execute SQL queries
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to send to the database
            PreparedStatement pstate = state.getConnection().prepareStatement("DELETE FROM JobParts WHERE jobPartsID == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setInt(1, jobPartsTable.getSelectionModel().getSelectedItem().getJobPartsID());

            // Execute the query
            pstate.execute();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Extract data from the database for the job parts
    public void extractPartsData(String fxml) {

        // Change the jobID depending on who's executing
        switch (fxml) {

            // Check if the fxml is foreperson
            case "foreperson" -> {

                // Try to extract all the data from the SQL table
                try {

                    // Adding Items to the tableView
                    ObservableList<database.parts> items = FXCollections.observableArrayList();

                    // Create a statement
                    Statement state = database.database.connection().createStatement();

                    // Prepare the query to be sent to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("SELECT * FROM JobParts WHERE jobID == ?;");

                    System.out.println("Foreperson JobID: " + controllers.foreperson.getJobID());

                    // Fill in all the variables
                    pstate.setInt(1, controllers.foreperson.getJobID());

                    // Execute the specific query
                    ResultSet rs = pstate.executeQuery();

                    // Loop through all the results
                    while (rs.next()) {

                        // Add the information to the items list
                        items.add(new database.parts(
                                rs.getInt("jobPartsID"),
                                rs.getInt("jobID"),
                                rs.getString("partName"),
                                rs.getString("partNumber")
                        ));
                        System.out.println("JobPartsID: " + rs.getString("jobPartsID") + "\nJobID: " + rs.getString("jobID") + "\nPart Name: " + rs.getString("partName") + "\nPart Number: " +  rs.getString("partNumber") + "\n");
                    }

                    // Assign all items to the matching columns in the table
                    jobPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobPartsID"));
                    jobPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
                    jobPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("partNumber"));

                    // Adding the data to the table
                    jobPartsTable.setItems(items);
                }

                // Catch any SQL related errors
                catch (SQLException exception) {
                    System.out.println("[ERROR]#~ " + exception);
                }
            }

            // Check if the fxml file is mechanic
            case "mechanic" -> {

                // Try to extract all the data from the SQL table
                try {

                    // Adding Items to the tableView
                    ObservableList<database.parts> items = FXCollections.observableArrayList();

                    // Create a statement
                    Statement state = database.database.connection().createStatement();

                    // Prepare the query to be sent to the database
                    PreparedStatement pstate = state.getConnection().prepareStatement("SELECT jobPartsID, partName, partNumber FROM JobParts WHERE jobID == ?;");

                    System.out.println(mechanic.getJobID());

                    // Fill in all the variables
                    pstate.setInt(1, controllers.mechanic.getJobID());

                    // Execute the specific query
                    ResultSet rs = pstate.executeQuery();

                    // Loop through all the results
                    while (rs.next()) {

                        // Add the information to the items list
                        items.add(new database.parts(
                                rs.getInt("jobPartsID"),
                                rs.getInt("jobID"),
                                rs.getString("partName"),
                                rs.getString("partNumber")
                        ));
                    }

                    // Assign all items to the matching columns in the table
                    jobPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("jobPartsID"));
                    jobPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("partName"));
                    jobPartNumberColumn.setCellValueFactory(new PropertyValueFactory<>("partNumber"));

                    // Adding the data to the table
                    jobPartsTable.setItems(items);
                }

                // Catch any SQL related errors
                catch (SQLException exception) {
                    System.out.println("[ERROR]#~ " + exception);
                }
            }
        }
    }
}