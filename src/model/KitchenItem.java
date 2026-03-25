package model;

/**
 * Kitchen item with material property.
 */
public class KitchenItem extends ItemBase {
    private final String material;

    public KitchenItem(String name, ItemCondition condition, User owner,
            String material) {
        super(name, ItemCategory.KITCHEN, condition, owner);
        this.material = material;
    }

    public String getMaterial() {
        return material;
    }

    public String getInfo() {
        return super.getInfo() + String.format(" | Material: %s",
                material);
    }
}
