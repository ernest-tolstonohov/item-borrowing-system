import model.*;
import service.BookingManager;
import factory.ItemFactory;

/**
 * Main app for testing the booking system.
 */
public class App {

    public static void main(String[] args) {
        // Initialize the booking manager
        BookingManager bookingManager = new BookingManager();

        // Create users
        System.out.println("\n=== Creating Users ===");
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        bookingManager.registerUser(alice);
        bookingManager.registerUser(bob);
        bookingManager.registerUser(charlie);

        // Create items using the Factory pattern
        System.out.println("\n=== Creating Items (via Factory Pattern) ===");

        // Alice's items
        ItemBase pot = ItemFactory.createKitchenItem(
                "Pot", ItemCondition.GOOD, alice,
                "Metal");

        ItemBase shovel = ItemFactory.createGardenItem(
                "Shovel", ItemCondition.FAIR, alice,
                "Spring");

        // Bob's items
        ItemBase hammer = ItemFactory.createWorkshopItem(
                "Hammer", ItemCondition.NEW, bob,
                "Hand Tool");

        ItemBase flashlight = ItemFactory.createOutdoorItem(
                "Flashlight", ItemCondition.GOOD, bob,
                "Pocket");

        // Charlie's items
        ItemBase brokenSaw = ItemFactory.createWorkshopItem(
                "Saw", ItemCondition.DAMAGED, charlie,
                "Hand Tool");

        ItemBase pan = ItemFactory.createKitchenItem(
                "Pan", ItemCondition.POOR, charlie,
                "Steel");

        // Register all items
        bookingManager.registerItem(pot);
        bookingManager.registerItem(shovel);
        bookingManager.registerItem(hammer);
        bookingManager.registerItem(flashlight);
        bookingManager.registerItem(brokenSaw);
        bookingManager.registerItem(pan);

        // Demonstration scenarios
        System.out.println("\n");
        System.out.println("BOOKING DEMONSTRATIONS");
        System.out.println("----------------------");

        // Scenario 1: Successful booking
        System.out.println("\n[SCENARIO 1: Successful Booking]");
        bookingManager.bookItem(bob, pot);

        // Scenario 2: Successful booking of POOR condition item
        System.out.println("\n[SCENARIO 2: Booking Item in POOR Condition]");
        bookingManager.bookItem(alice, pan);

        // Scenario 3: Failed booking - item already booked
        System.out.println("\n[SCENARIO 3: Attempting to Book Already Booked Item]");
        bookingManager.bookItem(charlie, pot);

        // Scenario 4: Failed booking - item is damaged
        System.out.println("\n[SCENARIO 4: Attempting to Book DAMAGED Item]");
        bookingManager.bookItem(alice, brokenSaw);

        // Scenario 5: Successful booking of another item
        System.out.println("\n[SCENARIO 5: Another Successful Booking]");
        bookingManager.bookItem(charlie, flashlight);

        // Scenario 6: Return an item
        System.out.println("\n[SCENARIO 6: Returning a Borrowed Item]");
        bookingManager.releaseItem(bob, pot);

        // Scenario 7: Book the now-available item
        System.out.println("\n[SCENARIO 7: Booking Previously Returned Item]");
        bookingManager.bookItem(charlie, pot);

        // Display item histories
        System.out.println("\n");
        System.out.println("ITEM HISTORIES");
        System.out.println("--------------");

        pot.printHistory();
        pan.printHistory();
        flashlight.printHistory();
        brokenSaw.printHistory();

        // Display final system status
        bookingManager.printSystemStatus();

        // Display availability summary
        bookingManager.printAvailabilitySummary();
    }
}
