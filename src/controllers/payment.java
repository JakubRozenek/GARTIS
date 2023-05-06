package controllers;

// Importing essential modules
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

// Controlling the payment GUI class
public class payment implements Initializable {

    // Buttons from the FXML
    @FXML
    private Button makePaymentButton;

    // Text fields from the FXML
    @FXML
    private TextField totalPrice;

    // Allowing the total price to be rounded down
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    // Subroutine automatically runs everytime this file gets executed
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Set the rounding format
        decimalFormat.setRoundingMode(RoundingMode.DOWN);

        // Set the total price text field as the price calculated
        totalPrice.setText(decimalFormat.format(controllers.reception.grandTotal()));
    }

    // Creating the payment
    public void makePayment() {

        // Creating the alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Title of the alert
        alert.setTitle("Payment Successful!");

        // Context of the alert
        alert.setContentText("Payment has been successfully");

        // Show the alert that they payment has been successful
        alert.showAndWait();

        // Close the current window
        exitApplication();
    }

    // Cancel the payment
    public void cancelPayment() {

        // Creating the alert dialog
        Alert alert = new Alert(Alert.AlertType.WARNING);

        // Title of the alert
        alert.setTitle("Payment Cancelled!");

        // Context of the alert
        alert.setContentText("Payment has been cancelled");

        // Show the alert that they payment has been successful
        alert.showAndWait();

        // Close the current window
        exitApplication();

    }

    // Close the current stage
    public void exitApplication() {

        // Get the current stage of the window
        Stage stage = (Stage) makePaymentButton.getScene().getWindow();

        // Close the current stage
        stage.close();
    }
}
