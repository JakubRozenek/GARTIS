package main;

// Importing the modules
import javafx.application.Application;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Subroutine that executes all the essential variables
public class main extends Application {

    // New stage for the updated scene
    private static Stage newStage;

    @Override
    public void start(Stage stage) throws Exception {

        // Execute the subroutine to check if database exists
        database.database.checkDatabase();

        // Assigning the new stage
        newStage = stage;

        // Loading the fxml of the main login
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

        // Set the title bar
        stage.setTitle("Login");

        // Style of the title bar
        stage.initStyle(StageStyle.UTILITY);

        // Assigning the login to the current scene
        stage.setScene(new Scene(root, 600, 360));

        // Show the login GUI to the user
        stage.show();
    }

    // Subroutine for changing the scenes
    public void changeScenes(String fxml, String title, Integer width, Integer height) throws IOException {

        // Loading the fxml of the new scene
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));

        // Assigning the login to the current scene
        newStage.getScene().setRoot(pane);

        // Set the title bar
        newStage.setTitle(title);

        // Assigning the Width x Height of the new GUI
        newStage.setWidth(width);
        newStage.setHeight(height);

        // Dont allow the user to resize the window
        newStage.setResizable(false);
    }

    // Launching the GUI as soon you execute the file
    public static void main(String[] args) {

        // Launch the program with the specific arguments
        launch(args);
    }
}
