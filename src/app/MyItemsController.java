package app;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.ItemBase;

import java.util.stream.Collectors;

/**
 * Controller for My Items screen
 * Shows items owned by the current user
 */
public class MyItemsController extends BaseController {

    @FXML
    private Label titleLabel;
    @FXML
    private TableView<ItemRow> itemsTable;

    @FXML
    public void initialize() {
        initializeBase();

        // Set column resize policy to prevent empty column
        itemsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        if (currentUser != null) {
            loadMyItems();
        }

        addActionColumn();
    }

    /**
     * Load items owned by current user
     */
    private void loadMyItems() {
        var items = currentUser.getOwnedItems().stream()
                .map(ItemRow::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        itemsTable.setItems(items);
        titleLabel.setText("My Items (" + items.size() + ")");
    }

    /**
     * Add view button to each row
     */
    private void addActionColumn() {
        @SuppressWarnings("unchecked")
        TableColumn<ItemRow, Void> actionCol = (TableColumn<ItemRow, Void>) itemsTable.getColumns().get(5);

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final HBox pane = new HBox(viewBtn);

            {
                pane.setAlignment(Pos.CENTER);
                viewBtn.setStyle(
                        "-fx-font-size: 11px; -fx-padding: 4 12 4 12; -fx-background-color: #e2e8f0; -fx-text-fill: #4a5568; -fx-background-radius: 4; -fx-cursor: hand;");

                viewBtn.setOnAction(e -> {
                    ItemRow item = getTableView().getItems().get(getIndex());
                    viewItemDetails(item.getItem());
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
        content.append("Status: ").append(item.isBooked() ? "BOOKED" : "AVAILABLE").append("\n");

        if (item.getCurrentBorrower() != null) {
            content.append("Borrowed by: ").append(item.getCurrentBorrower().getName()).append("\n");
        }

        content.append("\nHistory:\n");
        if (item.getHistory().isEmpty()) {
            content.append("No history records yet.");
        } else {
            item.getHistory().forEach(h -> content.append("  - ").append(h.toString()).append("\n"));
        }

        alert.setContentText(content.toString());
        alert.getDialogPane().setMinWidth(400);
        alert.getDialogPane().setMinHeight(300);
        alert.showAndWait();
    }

    @FXML
    protected void showMyItems() {
        initialize();
    }

    public static class ItemRow {
        private final ItemBase item;
        private final SimpleStringProperty statusText;

        public ItemRow(ItemBase item) {
            this.item = item;
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

        public String getStatusText() {
            return statusText.get();
        }
    }
}
