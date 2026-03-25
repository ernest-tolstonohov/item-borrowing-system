package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.ItemBase;
import service.BookingManager;

import java.util.stream.Collectors;

/**
 * Controller for Borrowed Items screen
 * Shows items currently borrowed by the user
 */
public class BorrowedController extends BaseController {

    @FXML
    private Label titleLabel;
    @FXML
    private TableView<ItemRow> itemsTable;

    private BookingManager bookingManager;

    @FXML
    public void initialize() {
        initializeBase();
        bookingManager = Main.getBookingManager();

        // Set column resize policy to prevent empty column
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        if (currentUser != null) {
            loadBorrowedItems();
        }

        addActionColumn();
    }

    /**
     * Load borrowed items using functional approach
     */
    private void loadBorrowedItems() {
        var items = currentUser.getBorrowedItems().stream()
                .map(ItemRow::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        itemsTable.setItems(items);
        titleLabel.setText("Items I'm Borrowing (" + items.size() + ")");
    }

    /**
     * Add action buttons (View and Return)
     */
    private void addActionColumn() {
        @SuppressWarnings("unchecked")
        TableColumn<ItemRow, Void> actionCol = (TableColumn<ItemRow, Void>) itemsTable.getColumns().get(4);

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button returnBtn = new Button("Return");
            private final HBox pane = new HBox(5, viewBtn, returnBtn);

            {
                pane.setAlignment(Pos.CENTER);
                viewBtn.setStyle(
                        "-fx-font-size: 11px; -fx-padding: 4 8 4 8; -fx-background-color: #e2e8f0; -fx-text-fill: #4a5568; -fx-background-radius: 4; -fx-cursor: hand;");
                returnBtn.setStyle(
                        "-fx-font-size: 11px; -fx-padding: 4 8 4 8; -fx-background-color: #f56565; -fx-text-fill: white; -fx-background-radius: 4; -fx-cursor: hand;");

                viewBtn.setOnAction(e -> {
                    ItemRow item = getTableView().getItems().get(getIndex());
                    viewItemDetails(item.getItem());
                });

                returnBtn.setOnAction(e -> {
                    ItemRow item = getTableView().getItems().get(getIndex());
                    returnItem(item.getItem());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void viewItemDetails(ItemBase item) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Details");
        alert.setHeaderText(item.getName());

        StringBuilder content = new StringBuilder();
        content.append("ID: ").append(item.getId()).append("\n");
        content.append("Category: ").append(item.getCategory()).append("\n");
        content.append("Condition: ").append(item.getCondition()).append("\n");
        content.append("Owner: ").append(item.getOwner().getName()).append("\n");
        content.append("Status: BORROWED BY YOU\n");

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    /**
     * Return an item
     */
    private void returnItem(ItemBase item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Return");
        confirm.setHeaderText("Return " + item.getName() + "?");
        confirm.setContentText("Owner: " + item.getOwner().getName());
        confirm.getDialogPane().setMinWidth(350);

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = bookingManager.releaseItem(currentUser, item);

                Alert result = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
                result.setTitle(success ? "Success" : "Error");
                result.setHeaderText(success ? "Item Returned Successfully!" : "Return Failed");
                result.setContentText(
                        success ? item.getName()
                                + " has been returned successfully.\nIt is now available for others to borrow."
                                : "Failed to return item");
                result.getDialogPane().setMinWidth(380);
                result.showAndWait();

                // Refresh
                loadBorrowedItems();
            }
        });
    }

    @FXML
    protected void showBorrowed() {
        initialize();
    }

    public static class ItemRow {
        private final ItemBase item;
        private final SimpleStringProperty ownerName;

        public ItemRow(ItemBase item) {
            this.item = item;
            this.ownerName = new SimpleStringProperty(item.getOwner().getName());
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

        public String getOwnerName() {
            return ownerName.get();
        }
    }
}
