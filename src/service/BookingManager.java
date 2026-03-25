package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main manager for handling bookings.
 */
public class BookingManager {
    private final List<User> users;
    private final List<ItemBase> items;

    public BookingManager() {
        this.users = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    // register a user in the system
    public void registerUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            System.out.println("User registered: " + user.getName());
        }
    }

    // Register item
    public void registerItem(ItemBase item) {
        if (!items.contains(item)) {
            items.add(item);
            item.getOwner().addOwnedItem(item);
            System.out.println("Item registered: " + item.getName() + " (owned by "
                    + item.getOwner().getName() + ")");
        }
    }

    // Try to book an item for a user
    public boolean bookItem(User user, ItemBase item) {
        System.out.println("\n--- Booking Request ---");
        System.out.println("User: " + user.getName() + " | Item: " + item.getName());

        // damaged items can't be booked
        if (item.getCondition() == ItemCondition.DAMAGED) {
            System.out.println("Sorry, this item is damaged and can't be booked right now.");
            System.out.println("Contact the owner or pick something else.");
            return false;
        }

        // already booked?
        if (item.isBooked()) {
            System.out.println("Sorry, this item is unavailable.");
            System.out.println("Currently borrowed by " + item.getCurrentBorrower().getName());
            System.out.println("Try again later or pick another item.");
            return false;
        }

        // can't book your own stuff
        if (item.getOwner().equals(user)) {
            System.out.println("You cannot book your own item.");
            return false;
        }

        // all good, book it
        item.markAsBorrowed(user);
        user.borrowItem(item);
        System.out.println("Success! " + user.getName()
                + " borrowed " + item.getName());
        return true;
    }

    // return an item back
    public boolean releaseItem(User user, ItemBase item) {
        System.out.println("\n--- Return Request ---");
        System.out.println("User: " + user.getName() + " | Item: " + item.getName());

        if (!item.isBooked()) {
            System.out.println("This item is not currently booked.");
            return false;
        }

        if (!item.getCurrentBorrower().equals(user)) {
            System.out.println("You cannot return an item you haven't borrowed.");
            return false;
        }

        // do the return
        item.markAsReturned(user);
        user.returnItem(item);
        System.out.println("Returned! " + item.getName()
                + " is available now.");
        return true;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    public List<ItemBase> getItems() {
        return new ArrayList<>(items);
    }

    // Print full status report
    public void printSystemStatus() {
        System.out.println("\n");
        System.out.println("SYSTEM STATUS");
        System.out.println("-------------");

        // Print users
        System.out.println("\n--- REGISTERED USERS (" + users.size() + ") ---");
        for (User user : users) {
            System.out.println(user.getInfo());
            if (!user.getBorrowedItems().isEmpty()) {
                System.out.println("  Currently borrowing:");
                for (ItemBase item : user.getBorrowedItems()) {
                    System.out.println("    • " + item.getName());
                }
            }
        }

        // Print items
        System.out.println("\n--- REGISTERED ITEMS (" + items.size() + ") ---");
        for (ItemBase item : items) {
            System.out.println("\n" + item.getInfo());
        }

        System.out.println("\n---------");
    }

    /**
     * Prints a summary of available and booked items.
     */
    public void printAvailabilitySummary() {
        int availableCount = 0;
        int bookedCount = 0;

        for (ItemBase item : items) {
            if (item.isBooked()) {
                bookedCount++;
            } else {
                availableCount++;
            }
        }

        System.out.println("\n--- Availability Summary ---");
        System.out.println("Total items: " + items.size());
        System.out.println("Available: " + availableCount);
        System.out.println("Booked: " + bookedCount);
    }
}
