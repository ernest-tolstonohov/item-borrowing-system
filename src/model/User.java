package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the booking system.
 */
public class User {
    private static int nextId = 1;

    private final int id;
    private final String name;
    private final List<ItemBase> ownedItems;
    private final List<ItemBase> borrowedItems;

    // constructor
    public User(String name) {
        this.id = nextId++;
        this.name = name;
        this.ownedItems = new ArrayList<>();
        this.borrowedItems = new ArrayList<>();
    }

    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemBase> getOwnedItems() {
        return new ArrayList<>(ownedItems);
    }

    public List<ItemBase> getBorrowedItems() {
        return new ArrayList<>(borrowedItems);
    }

    // Add item to owned list
    public void addOwnedItem(ItemBase item) {
        if (!ownedItems.contains(item)) {
            ownedItems.add(item);
        }
    }

    // Add to borrowed items
    public void borrowItem(ItemBase item) {
        if (!borrowedItems.contains(item)) {
            borrowedItems.add(item);
        }
    }

    // remove item from borrowed list
    public void returnItem(ItemBase item) {
        borrowedItems.remove(item);
    }

    // get user info as string
    public String getInfo() {
        return String.format("User ID: %d | Name: %s | Owns: %d items | Borrowed: %d items",
                id, name, ownedItems.size(), borrowedItems.size());
    }

    // toString
    public String toString() {
        return name;
    }
}
