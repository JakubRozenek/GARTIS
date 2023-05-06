package controllers;

// Importing essential modules
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import java.sql.PreparedStatement;
import javafx.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.fxml.FXML;
import main.main;

// Class controlling the login GUI
public class login {

    // Assigning the text fields from fxml
    @FXML
    private TextField usernameText;

    // Assigning the password field from fxml
    @FXML
    private PasswordField passwordText;

    // Assigning the button from fxml
    @FXML
    private Button loginButton;

    // Referencing the main java class
    main main = new main();

    // Referencing the admin java class
    admin admin = new admin();

    // Subroutine to check the credentials of the users logging in
    public void checkCredentials(ActionEvent event) {

        // Try to extract all the data from the SQL table
        try {

            // Create a statement
            Statement state = database.database.connection().createStatement();

            // Prepare the query to get the job role of the user logging in
            PreparedStatement pstate = state.getConnection().prepareStatement("SELECT * FROM Employee WHERE username == ?;");

            // Fill in all the variables to be sent inside the statement
            pstate.setString(1, usernameText.getText());

            // Save the results from the database to the ResultSet
            ResultSet rs = pstate.executeQuery();

            // Results of the user's full name
            String fullName = rs.getString("fullName");

            // Results of the job role the user has
            String username = rs.getString("username");

            // Results of the password
            String password = rs.getString("password");

            // Results of the job role
            String jobRole = rs.getString("jobRole");

            // Check if the username and password match with the ones in the database
            if (usernameText.getText().equals(username) || passwordText.getText().equals(password)) {

                // Get the current username that the user has logged in with
                admin.setUsername(fullName);

                // Check what job role the user has and execute the GUI
                switch (jobRole) {

                    // If the user is an admin launch the admin GUI
                    case "Administrator" -> {

                        // Change the current scene to the admin GUI
                        main.changeScenes("/fxml/admin.fxml", "Admin", 1200, 759);

                        // Close the current connection
                        pstate.close();
                    }

                    // If the user is an receptionist launch the receptionist GUI
                    case "Receptionist" -> {

                        // Change the current scene to the reception GUI
                        main.changeScenes("/fxml/reception.fxml", "Reception", 1200, 759);

                        // Close the current connection
                        pstate.close();
                    }

                    // If the user is an franchisee launch the franchisee GUI
                    case "Franchisee" -> {

                        // Change the current scene to the franchisee GUI
                        main.changeScenes("/fxml/franchisee.fxml", "Franchisee", 1200, 759);

                        // Close the current connection
                        pstate.close();
                    }

                    // If the user is an foreman launch the foreman GUI
                    case "Foreman" -> {

                        // Change the current scene to the foreman GUI
                        main.changeScenes("/fxml/foreperson.fxml", "Foreman", 1200, 759);

                        // Close the current connection
                        pstate.close();
                    }

                    // If the user is an mechanic launch the mechanic GUI
                    case "Mechanic" -> {

                        // Change the current scene to the mechanic GUI
                        main.changeScenes("/fxml/mechanic.fxml", "Mechanic", 1200, 759);

                        // Close the current connection
                        pstate.close();
                    }
                }
            }
        }

        // Catch any SQL related errors
        catch (Exception exception) {
            System.out.println("[ERROR]#~ " + exception.getMessage());
        }
    }
}