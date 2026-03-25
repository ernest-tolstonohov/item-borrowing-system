package app;

import factory.ItemFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.*;
import service.BookingManager;

/**
 * Controller for Add Item screen
 * Demonstrates dynamic UI updates based on category selection
 */
public class AddItemController extends BaseController {

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<ItemCategory> categoryCombo;
    @FXML
    private ComboBox<ItemCondition> conditionCombo;
    @FXML
    private VBox specialPropertyBox;
    @FXML
    private Label specialPropertyLabel;
    @FXML
    private TextField specialPropertyField;
    @FXML
    private Label specialPropertyHint;
    @FXML
    private Label errorLabel;

    private BookingManager bookingManager;

    @FXML
    public void initialize() {
        initializeBase();
        bookingManager = Main.getBookingManager();

        // Setup combo boxes
        setupComboBoxes();

        // Add listener to category selection to update special property field
        categoryCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSpecialPropertyField(newVal);
        });

        errorLabel.setText("");
    }

    /**
     * Setup category and condition dropdowns
     */
    private void setupComboBoxes() {
        // Category dropdown
        categoryCombo.getItems().addAll(ItemCategory.values());

        // Condition dropdown
        conditionCombo.getItems().addAll(ItemCondition.values());
    }

    /**
     * Update special property field based on selected category
     * Demonstrates dynamic UI behavior
     */
    private void updateSpecialPropertyField(ItemCategory category) {
        if (category == null) {
            specialPropertyBox.setVisible(false);
            specialPropertyBox.setManaged(false);
            return;
        }

        specialPropertyBox.setVisible(true);
        specialPropertyBox.setManaged(true);

        switch (category) {
            case KITCHEN:
                specialPropertyLabel.setText("Material *");
                specialPropertyField.setPromptText("e.g., Stainless Steel, Cast Iron, Plastic");
                specialPropertyHint.setText("What material is this kitchen item made of?");
                break;
            case GARDEN:
                specialPropertyLabel.setText("Season *");
                specialPropertyField.setPromptText("e.g., Spring, Summer, All seasons");
                specialPropertyHint.setText("Which season is this garden item best for?");
                break;
            case WORKSHOP:
                specialPropertyLabel.setText("Tool Type *");
                specialPropertyField.setPromptText("e.g., Hand Tool, Power Tool, Measuring Tool");
                specialPropertyHint.setText("What type of workshop tool is this?");
                break;
            case OUTDOOR:
                specialPropertyLabel.setText("Portability *");
                specialPropertyField.setPromptText("e.g., Pocket, Backpack, Vehicle, Non-portable");
                specialPropertyHint.setText("How portable is this outdoor item?");
                break;
        }
    }

    /**
     * Handle add item button click
     * Validates input and creates item using Factory pattern
     */
    @FXML
    private void handleAddItem() {
        errorLabel.setText("");

        // Validate inputs
        String name = nameField.getText().trim();
        ItemCategory category = categoryCombo.getValue();
        ItemCondition condition = conditionCombo.getValue();
        String specialProperty = specialPropertyField.getText().trim();

        if (name.isEmpty()) {
            errorLabel.setText("Please enter an item name");
            return;
        }

        if (category == null) {
            errorLabel.setText("Please select a category");
            return;
        }

        if (condition == null) {
            errorLabel.setText("Please select a condition");
            return;
        }

        if (specialProperty.isEmpty()) {
            errorLabel.setText("Please fill in the special property field");
            return;
        }

        // Create item using Factory pattern
        ItemBase newItem = createItemByCategory(name, category, condition, specialProperty);

        if (newItem != null) {
            // Register item in system
            bookingManager.registerItem(newItem);

            // Show success dialog
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText("Item Added Successfully!");
            success.setContentText("Your item '" + name
                    + "' has been added to your collection.\nYou can view it in 'My Items' section.");
            success.getDialogPane().setMinWidth(380);
            success.showAndWait();

            // Navigate to My Items
            showMyItems();
        }
    }

    /**
     * Create item using Factory pattern based on category
     * Demonstrates proper use of Factory pattern for object creation
     */
    private ItemBase createItemByCategory(String name, ItemCategory category,
            ItemCondition condition, String specialProperty) {
        switch (category) {
            case KITCHEN:
                return ItemFactory.createKitchenItem(name, condition, currentUser, specialProperty);
            case GARDEN:
                return ItemFactory.createGardenItem(name, condition, currentUser, specialProperty);
            case WORKSHOP:
                return ItemFactory.createWorkshopItem(name, condition, currentUser, specialProperty);
            case OUTDOOR:
                return ItemFactory.createOutdoorItem(name, condition, currentUser, specialProperty);
            default:
                errorLabel.setText("Unknown category");
                return null;
        }
    }

    @FXML
    protected void showAddItem() {
        initialize();
    }
}
