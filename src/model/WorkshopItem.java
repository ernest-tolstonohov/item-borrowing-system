package model;

/**
 * Workshop item - tools and such.
 */
public class WorkshopItem extends ItemBase {
    private final String toolType;

    public WorkshopItem(String name, ItemCondition condition, User owner,
            String toolType) {
        super(name, ItemCategory.WORKSHOP, condition, owner);
        this.toolType = toolType;
    }

    public String getToolType() {
        return toolType;
    }

    public String getInfo() {
        return super.getInfo() + String.format(" | Type: %s",
                toolType);
    }
}
