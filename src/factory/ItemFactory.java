package factory;

import model.*;

/**
 * Factory for creating items.
 */
public class ItemFactory {

    // Create item by category with default properties
    public static ItemBase createItem(ItemCategory category, String name,
            ItemCondition condition, User owner) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        switch (category) {
            case KITCHEN:
                return new KitchenItem(name, condition, owner,
                        "Stainless Steel");
            case GARDEN:
                return new GardenItem(name, condition, owner,
                        "All seasons");
            case WORKSHOP:
                return new WorkshopItem(name, condition, owner,
                        "Hand Tool");
            case OUTDOOR:
                return new OutdoorItem(name, condition, owner,
                        "Portable");
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
    }

    // kitchen items
    public static KitchenItem createKitchenItem(String name, ItemCondition condition,
            User owner, String material) {
        return new KitchenItem(name, condition, owner, material);
    }

    // for garden items
    public static GardenItem createGardenItem(String name, ItemCondition condition,
            User owner, String season) {
        return new GardenItem(name, condition, owner, season);
    }

    // workshop tools
    public static WorkshopItem createWorkshopItem(String name, ItemCondition condition,
            User owner, String toolType) {
        return new WorkshopItem(name, condition, owner, toolType);
    }

    // Create outdoor item
    public static OutdoorItem createOutdoorItem(String name, ItemCondition condition,
            User owner, String portability) {
        return new OutdoorItem(name, condition, owner, portability);
    }
}
