package com.gamehub;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Hyperlink signupLink;

    @FXML
    private void handleLogin() {
        errorLabel.setText("");

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        int userId = DatabaseManager.login(username, password);

        if (userId > 0) {
            UserSession.getInstance().login(userId, username);
            loadHomeScreen();
        } else {
            errorLabel.setText("Invalid username or password");
            passwordField.clear();
        }
    }

    @FXML
    private void handleSignupNavigation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/SignupView.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error loading signup screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadHomeScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/HomeView.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            SceneManager.setHomeScene(scene);
            stage.setScene(scene);
            stage.setTitle("GameHub");
        } catch (Exception e) {
            System.err.println("Error loading home screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
