package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.BookingManager;

/**
 * Controller for the login screen
 * Handles user authentication and registration
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    private BookingManager bookingManager;

    /**
     * Initialize the controller
     * Called automatically by JavaFX after FXML is loaded
     */
    @FXML
    public void initialize() {
        bookingManager = Main.getBookingManager();
        errorLabel.setText(""); // Hide error label initially
    }

    /**
     * Handle login button click
     * Validates username and logs in or registers the user
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();

        // Validate username
        if (username.isEmpty()) {
            errorLabel.setText("Please enter a username");
            return;
        }

        // Find or create user
        User user = findOrCreateUser(username);
        Main.setCurrentUser(user);

        // Navigate to browse items
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("browse.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setTitle("Library of Stuff - Browse Items");
        } catch (Exception e) {
            errorLabel.setText("Error loading browse page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Find existing user or create new one
     * Implements simple user registration
     */
    private User findOrCreateUser(String username) {
        // Try to find existing user using functional programming approach
        return bookingManager.getUsers().stream()
                .filter(u -> u.getName().equalsIgnoreCase(username))
                .findFirst()
                .orElseGet(() -> {
                    // User not found, create new one
                    User newUser = new User(username);
                    bookingManager.registerUser(newUser);
                    return newUser;
                });
    }
}
