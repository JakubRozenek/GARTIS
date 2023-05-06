package controllers;

// Importing all the essential modules
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

// Subroutine controlling the GUI of the mechanic
public class mechanic implements Initializable {

    // Pane from FXML
    @FXML
    private Pane dashboardPane, jobPane;

    // Buttons from FXML
    @FXML
    private Button dashboardButton, jobButton;

    // Split menu from FXML
    @FXML
    private SplitMenuButton accountSplitMenu;

    // Tables from FXML
    @FXML
    private TableView<database.jobs> jobTable;

    @FXML
    private TableColumn<database.jobs, String> jobStatusColumn, jobDescriptionColumn, jobEstimatedColumn, jobActualColumn, jobMechanicColumn, jobVehicleRegistrationColumn, jobDescriptionCompletedColumn;

    @FXML
    private TableColumn<database.jobs, Integer> jobIDColumn;

    // Combobox from FXML
    @FXML
    private ComboBox<String> jobStatusCombo;

    // Text Area from FXML
    @FXML
    private TextArea workDoneText;

    @FXML
    private TextField actualTimeText;

    // Variable to hold the jobID for adding parts
    private static Integer jobID;

    // Referencing the main java class
    admin admin = new admin();

    // Referencing the remove stock class
    removeStock removeStock = new removeStock();

    // Subroutine automatically runs everytime this file gets executed
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // When the user starts the program he will go to the dashboard
        dashboardPane.toFront();

        // Set the account split menu text according to the name of the user that is logged in
        accountSplitMenu.setText(controllers.admin.getUsername());

        // Extract all the data from the database to the stock table
        extractJobData();

        // Status of the job
        jobStatusCombo.getItems().addAll(
                "Started",
                "In-Progress",
                "Completed"
        );

        // Creating an action that will perform when choosing the job status
        jobStatusCombo.setOnAction(event -> {

            // Try to extract all the data from the SQL table
            try {

                // Create a statement
                Statement state = database.database.connection().createStatement();

                // Prepare the query to send to the database
                PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Jobs SET status = ? WHERE jobID = ?");

                // Fill in all the variables to be sent inside the statement
                pstate.setString(1, jobStatusCombo.getSelectionModel().getSelectedItem().toString());
                pstate.setInt(2, jobTable.getSelectionModel().getSelectedItem().getJobID());

                // Execute the query
                pstate.execute();

                // Update the table
                extractJobData();
            }

            // Catch any SQL related errors
            catch (Exception exception) {
                System.out.println("[ERROR]#~ " + exception.getMessage());
                jobStatusCombo.valueProperty().set(null);
            }
        });
    }

    // Show only the jobs active to a specific mechanic
    public void activeJob() {

        // Adding Items to the tableView
        ObservableList<database.jobs> items = FXCollections.observableArrayList();

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to get the job role of the user logging in
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT * FROM Jobs WHERE mechanic == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setString(1, controllers.admin.getUsername());

            // Save the results from the database to the ResultSet
            ResultSet rs = pstate.executeQuery();

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

            // Close the current connection
            pstate.close();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
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

    // Update the Job's information
    public void updateJob() {

        // Try to update the job's information
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to send to the database
            PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Jobs SET descriptionCarried = ?, actualTime = ?, status = ? WHERE jobID = ?");

            // Fill in all the variables to be sent inside the statement
            pstate.setString(1, workDoneText.getText());
            pstate.setString(2, actualTimeText.getText());
            pstate.setString(3, jobStatusCombo.getItems().get(2));
            pstate.setInt(4, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Execute the query
            pstate.execute();

            // Update the table
            extractJobData();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception);
        }
    }

    // Accept the job as the mechanic
    public void acceptJob() {

        // Try to accept the job using the mechanics login
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to send to the database
            PreparedStatement pstate = state.getConnection().prepareStatement("UPDATE Jobs SET mechanic = ?, status = ? WHERE jobID = ?");

            // Fill in all the variables to be sent inside the statement
            pstate.setString(1, controllers.admin.getUsername());
            pstate.setString(2, jobStatusCombo.getItems().get(0));
            pstate.setInt(3, jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Execute the query
            pstate.execute();

            // Update the table to only show jobs assigned to specific mechanic
            activeJob();
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception);
        }

    }

    // Open a new window containing a table of the parts history that is linked to the specific job
    public void removePartsGUI() {

        // Try to open the stock GUI
        try {

            // Set the job ID for adding the parts to the job
            removeStock.chooseFXML("mechanic");

            // Loading the fxml of the order history
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/removeStock.fxml"));

            // Creating the stage to handle of the GUI
            Stage stage = new Stage();

            // Assigning the order history to the current scene
            stage.setScene(new Scene(root));

            // Setting the title of the window
            stage.setTitle("Add Parts");

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

    // Open a new window containing a table of the stock ledger
    public void addPartsGUI() {

        // Try to open the stock GUI
        try {

            // Set the job ID for adding the parts to the job
            setJobID(jobTable.getSelectionModel().getSelectedItem().getJobID());

            // Loading the fxml of the order history
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/addStock.fxml"));

            // Creating the stage to handle of the GUI
            Stage stage = new Stage();

            // Assigning the order history to the current scene
            stage.setScene(new Scene(root));

            // Setting the title of the window
            stage.setTitle("Add Parts");

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

    // Changing the scenes when the user presses the button
    public void changePane(ActionEvent event) {

        // If the user presses the dashboard button change the scene
        if (event.getSource().toString().contains(dashboardButton.getText())) {

            // Put the pane in front of all the other panes
            dashboardPane.toFront();
        }

        // If the user presses the account button change the scene
        else if (event.getSource().toString().contains(jobButton.getText())) {

            // Put the pane in front of all the other panes
            jobPane.toFront();

            // Extract all the data from the database to the stock table
            extractJobData();
        }
    }

    // Get the account details once user presses the row
    public void selectAccount(MouseEvent event) {

        // Try to transfer data from the selected row to the text fields
        try {

            // Check if the user is pressing on customer table
            if (event.getSource().toString().contains("jobTable")) {

                // Insert the data from the table to the text fields for the customers data
                workDoneText.setText(jobTable.getSelectionModel().getSelectedItem().getDescriptionCarried());
                actualTimeText.setText(jobTable.getSelectionModel().getSelectedItem().getActualTime());

                // Save the job id when the user selects the item
                setJobID(jobTable.getSelectionModel().getSelectedItem().getJobID());
            }
        }

        // Catch any errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }

    // Logout the user from GUI and go back to the main login screen
    public void logoutAccount() {

        // Use the logout subroutine from the admin java class
        admin.logoutAccount();
    }

    // Getter for the username that is currently logged in
    public static Integer getJobID() {
        return jobID;
    }

    // Setter for the login class to set the username
    public void setJobID(Integer jobID) {
        mechanic.jobID = jobID;
    }
}
