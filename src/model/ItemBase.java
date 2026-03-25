package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all items that can be booked.
 */
public abstract class ItemBase {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final ItemCategory category;
    private ItemCondition condition;
    private boolean isBooked;
    private User owner;
    private User currentBorrower;
    private final List<ItemHistoryRecord> history;

    // Constructor
    public ItemBase(String name, ItemCategory category, ItemCondition condition, User owner) {
        this.id = nextId++;
        this.name = name;
        this.category = category;
        this.condition = condition;
        this.owner = owner;
        this.isBooked = false;
        this.currentBorrower = null;
        this.history = new ArrayList<>();
    }

    // getters for basic properties
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }

    // booking status check
    public boolean isBooked() {
        return isBooked;
    }

    public User getOwner() {
        return owner;
    }

    public User getCurrentBorrower() {
        return currentBorrower;
    }

    public List<ItemHistoryRecord> getHistory() {
        return new ArrayList<>(history);
    }

    // check if item can be booked (not booked and not damaged)
    public boolean isBookable() {
        return !isBooked && condition != ItemCondition.DAMAGED;
    }

    // mark item as borrowed by user
    public void markAsBorrowed(User borrower) {
        this.isBooked = true;
        this.currentBorrower = borrower;
        addHistoryRecord(new ItemHistoryRecord(borrower.getName(), "borrowed by"));
    }

    // mark as returned
    public void markAsReturned(User returner) {
        this.isBooked = false;
        this.currentBorrower = null;
        addHistoryRecord(new ItemHistoryRecord(returner.getName(), "returned by"));
    }

    // add history record
    public void addHistoryRecord(ItemHistoryRecord record) {
        history.add(record);
    }

    // Returns item info string
    public String getInfo() {
        String status = isBooked ? "BOOKED by " + currentBorrower.getName() : "AVAILABLE";
        return String.format("ID: %d | %s | Category: %s | Condition: %s | Owner: %s | Status: %s",
                id, name, category, condition, owner.getName(), status);
    }

    // print complete item history
    public void printHistory() {
        System.out.println("\n=== History for: " + name + " ===");
        if (history.isEmpty()) {
            System.out.println("No history records yet.");
        } else {
            for (ItemHistoryRecord record : history) {
                System.out.println("  - " + record);
            }
        }
    }
}
