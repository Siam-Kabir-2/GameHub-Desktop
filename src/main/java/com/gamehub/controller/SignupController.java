package com.gamehub.controller;

import com.gamehub.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignupController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button signupButton;

    @FXML
    private void handleSignup() {
        errorLabel.setText("");

        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            return;
        }

        if (username.contains(" ")) {
            errorLabel.setText("Username cannot contain spaces");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            confirmPasswordField.clear();
            return;
        }

        int userId = DatabaseManager.signUp(username, password);

        if (userId > 0) {
            showSuccessAlert("Success!", "Account created successfully!\nYou can now login with your credentials.");
            handleLoginNavigation();
        } else if (userId == -1) {
            errorLabel.setText("Username already taken. Please choose another.");
            usernameField.selectAll();
            usernameField.requestFocus();
        } else {
            errorLabel.setText("Registration failed. Please try again later.");
        }
    }

    @FXML
    private void handleLoginNavigation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/LoginView.fxml"));
            Stage stage = (Stage) signupButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading login screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
