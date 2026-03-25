package app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

/**
 * Base controller with common navigation methods
 * All controllers extend this to avoid code duplication
 */
public abstract class BaseController {

    @FXML
    protected Label usernameLabel;

    protected User currentUser;

    /**
     * Initialize username label - call this from child initialize methods
     */
    protected void initializeBase() {
        currentUser = Main.getCurrentUser();
        if (currentUser != null && usernameLabel != null) {
            usernameLabel.setText("Logged in as: " + currentUser.getName());
        }
    }

    // Navigation methods
    @FXML
    protected void handleLogout() {
        navigateTo("login.fxml", "Library of Stuff - Login");
    }

    @FXML
    protected void showBrowse() {
        navigateTo("browse.fxml", "Library of Stuff - Browse Items");
    }

    @FXML
    protected void showMyItems() {
        navigateTo("my-items.fxml", "Library of Stuff - My Items");
    }

    @FXML
    protected void showBorrowed() {
        navigateTo("borrowed.fxml", "Library of Stuff - My Borrowed");
    }

    @FXML
    protected void showAddItem() {
        navigateTo("add-item.fxml", "Library of Stuff - Add Item");
    }

    /**
     * Navigate to a new page while preserving window size
     */
    protected void navigateTo(String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(new Scene(root));
            stage.setWidth(width);
            stage.setHeight(height);
            stage.setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
