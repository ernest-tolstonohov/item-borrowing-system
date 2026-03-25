package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import service.BookingManager;
import factory.ItemFactory;

/**
 * Main application class for Library of Stuff
 * This is the entry point for the JavaFX application
 */
public class Main extends Application {

    // Singleton instance of BookingManager - shared across all controllers
    private static BookingManager bookingManager;
    private static User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize the booking system with sample data
        initializeData();

        // Load the login screen
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("Library of Stuff");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }

    /**
     * Initialize sample data for the application
     * Creates users and items to demonstrate functionality
     */
    private void initializeData() {
        bookingManager = new BookingManager();

        // Create users
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        bookingManager.registerUser(alice);
        bookingManager.registerUser(bob);
        bookingManager.registerUser(charlie);

        // Create items using Factory pattern
        // Alice's items
        ItemBase pot = ItemFactory.createKitchenItem(
                "Pot", ItemCondition.GOOD, alice, "Stainless Steel");
        ItemBase shovel = ItemFactory.createGardenItem(
                "Shovel", ItemCondition.FAIR, alice, "Spring");
        ItemBase rake = ItemFactory.createGardenItem(
                "Rake", ItemCondition.NEW, alice, "All seasons");

        // Bob's items
        ItemBase hammer = ItemFactory.createWorkshopItem(
                "Hammer", ItemCondition.NEW, bob, "Hand Tool");
        ItemBase flashlight = ItemFactory.createOutdoorItem(
                "Flashlight", ItemCondition.GOOD, bob, "Pocket");
        ItemBase drill = ItemFactory.createWorkshopItem(
                "Drill", ItemCondition.GOOD, bob, "Power Tool");

        // Charlie's items
        ItemBase saw = ItemFactory.createWorkshopItem(
                "Saw", ItemCondition.DAMAGED, charlie, "Hand Tool");
        ItemBase pan = ItemFactory.createKitchenItem(
                "Pan", ItemCondition.POOR, charlie, "Cast Iron");
        ItemBase tent = ItemFactory.createOutdoorItem(
                "Tent", ItemCondition.GOOD, charlie, "Backpack");

        // Register all items
        bookingManager.registerItem(pot);
        bookingManager.registerItem(shovel);
        bookingManager.registerItem(rake);
        bookingManager.registerItem(hammer);
        bookingManager.registerItem(flashlight);
        bookingManager.registerItem(drill);
        bookingManager.registerItem(saw);
        bookingManager.registerItem(pan);
        bookingManager.registerItem(tent);

        System.out.println("Sample data initialized: 3 users, 9 items created");
    }

    /**
     * Get the singleton BookingManager instance
     */
    public static BookingManager getBookingManager() {
        return bookingManager;
    }

    /**
     * Get the current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set the current logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}