package model;

/**
 * Outdoor/travel item with portability.
 */
public class OutdoorItem extends ItemBase {
    private final String portability;

    public OutdoorItem(String name, ItemCondition condition, User owner,
            String portability) {
        super(name, ItemCategory.OUTDOOR, condition, owner);
        this.portability = portability;
    }

    public String getPortability() {
        return portability;
    }

    public String getInfo() {
        return super.getInfo() + String.format(" | Portability: %s",
                portability);
    }
}
