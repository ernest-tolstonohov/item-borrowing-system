package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.*;
import service.BookingManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for Browse Items screen
 * Implements search and filter functionality using functional programming
 */
public class BrowseController extends BaseController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private ComboBox<String> conditionFilter;
    @FXML
    private TableView<ItemRow> itemsTable;

    private BookingManager bookingManager;
    private ObservableList<ItemRow> allItems;

    /**
     * Initialize the browse screen
     */
    @FXML
    public void initialize() {
        initializeBase();
        bookingManager = Main.getBookingManager();

        // Set column resize policy to prevent empty column
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Setup filters
        setupFilters();

        // Load items
        loadItems();

        // Setup listeners for real-time filtering using lambda expressions
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        conditionFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        // Add action buttons to table
        addActionColumn();
    }

    /**
     * Setup filter dropdowns
     */
    private void setupFilters() {
        // Category filter
        List<String> categories = Arrays.asList("All Categories", "KITCHEN", "GARDEN", "WORKSHOP", "OUTDOOR");
        categoryFilter.setItems(FXCollections.observableArrayList(categories));
        categoryFilter.setValue("All Categories");

        // Condition filter
        List<String> conditions = Arrays.asList("All Conditions", "NEW", "GOOD", "FAIR", "POOR");
        conditionFilter.setItems(FXCollections.observableArrayList(conditions));
        conditionFilter.setValue("All Conditions");
    }

    /**
     * Load all items from booking manager
     * Converts ItemBase objects to ItemRow objects for table display
     */
    private void loadItems() {
        // Use stream to convert items to table rows (functional programming)
        allItems = bookingManager.getItems().stream()
                .map(ItemRow::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        itemsTable.setItems(allItems);
    }

    /**
     * Apply filters using functional programming
     * Demonstrates use of streams and lambda expressions
     */
    private void applyFilters() {
        String search = searchField.getText().toLowerCase();
        String category = categoryFilter.getValue();
        String condition = conditionFilter.getValue();

        // Filter using streams and predicates
        List<ItemRow> filtered = allItems.stream()
                .filter(item -> {
                    // Search filter
                    if (!search.isEmpty() && !item.getName().toLowerCase().contains(search)) {
                        return false;
                    }
                    // Category filter
                    if (!"All Categories".equals(category) && !item.getCategory().equals(category)) {
                        return false;
                    }
                    // Condition filter
                    if (!"All Conditions".equals(condition) && !item.getCondition().equals(condition)) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        itemsTable.setItems(FXCollections.observableArrayList(filtered));
        itemsTable.refresh(); 
    }

    /**
     * Clear all filters
     */
    @FXML
    private void clearFilters() {
        searchField.clear();
        categoryFilter.setValue("All Categories");
        conditionFilter.setValue("All Conditions");
    }

    /**
     * Add action buttons to table (View, Book)
     */
    private void addActionColumn() {
        @SuppressWarnings("unchecked")
        TableColumn<ItemRow, Void> actionCol = (TableColumn<ItemRow, Void>) itemsTable.getColumns().get(6);

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button bookBtn = new Button("Book");
            private final HBox pane = new HBox(5, viewBtn, bookBtn);

            {
                pane.setAlignment(Pos.CENTER);
                viewBtn.setStyle(
                        "-fx-font-size: 11px; -fx-padding: 4 8 4 8; -fx-background-color: #e2e8f0; -fx-text-fill: #4a5568; -fx-background-radius: 4; -fx-cursor: hand;");
                bookBtn.setStyle(
                        "-fx-font-size: 11px; -fx-padding: 4 8 4 8; -fx-background-color: #667eea; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");

                viewBtn.setOnAction(e -> {
                    ItemRow item = getTableView().getItems().get(getIndex());
                    viewItemDetails(item.getItem());
                });

                bookBtn.setOnAction(e -> {
                    ItemRow item = getTableView().getItems().get(getIndex());
                    bookItem(item.getItem());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    ItemRow row = getTableView().getItems().get(getIndex());
                    // Disable book button if item is not bookable
                    bookBtn.setDisable(!row.getItem().isBookable() || row.getItem().getOwner().equals(currentUser));
                    setGraphic(pane);
                }
            }
        });
    }

    /**
     * View item details 
     */
    private void viewItemDetails(ItemBase item) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Details");
        alert.setHeaderText(item.getName());
        
        StringBuilder content = new StringBuilder();
        content.append("ID: ").append(item.getId()).append("\n");
        content.append("Category: ").append(item.getCategory()).append("\n");
        content.append("Condition: ").append(item.getCondition()).append("\n");
        content.append("Owner: ").append(item.getOwner().getName()).append("\n");
        content.append("Status: ").append(item.isBooked() ? "BOOKED" : "AVAILABLE").append("\n");
        
        if (item.getCurrentBorrower() != null) {
            content.append("Borrowed by: ").append(item.getCurrentBorrower().getName()).append("\n");
        }
        
        alert.setContentText(content.toString());
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }

    /**
     * Book an item 
     */
    private void bookItem(ItemBase item) {
        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Booking");
        confirm.setHeaderText("Book " + item.getName() + "?");
        confirm.setContentText("Owner: " + item.getOwner().getName() + "\nCondition: " + item.getCondition());
        confirm.getDialogPane().setMinWidth(350);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = bookingManager.bookItem(currentUser, item);

                Alert result = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                result.setTitle(success ? "Success" : "Error");
                result.setHeaderText(success ? "Item Booked Successfully!" : "Booking Failed");
                result.setContentText(success 
                    ? "You have successfully borrowed " + item.getName() + ".\nYou can view it in 'My Borrowed' section."
                    : "Cannot book this item.\nIt may be damaged, already booked, or it's your own item.");
                result.getDialogPane().setMinWidth(380);
                result.showAndWait();

                // Refresh table
                loadItems();
                applyFilters();
            }
        });
    }

    @FXML
    protected void showBrowse() {
        initialize();
    }

    /**
     * Inner class to represent a row in the table
     * Uses JavaFX properties for automatic UI updates
     */
    public static class ItemRow {
        private final ItemBase item;
        private final SimpleStringProperty ownerName;
        private final SimpleStringProperty statusText;

        public ItemRow(ItemBase item) {
            this.item = item;
            this.ownerName = new SimpleStringProperty(item.getOwner().getName());
            this.statusText = new SimpleStringProperty(
                    item.isBooked() ? "Booked by " + item.getCurrentBorrower().getName() : "Available");
        }

        public ItemBase getItem() {
            return item;
        }

        public int getId() {
            return item.getId();
        }

        public String getName() {
            return item.getName();
        }

        public String getCategory() {
            return item.getCategory().toString();
        }

        public String getCondition() {
            return item.getCondition().toString();
        }

        public String getOwnerName() {
            return ownerName.get();
        }

        public String getStatusText() {
            return statusText.get();
        }
    }
}
