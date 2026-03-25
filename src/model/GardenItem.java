package model;

/**
 * Garden item with season info.
 */
public class GardenItem extends ItemBase {
    private final String season;

    public GardenItem(String name, ItemCondition condition, User owner,
            String season) {
        super(name, ItemCategory.GARDEN, condition, owner);
        this.season = season;
    }

    public String getSeason() {
        return season;
    }

    public String getInfo() {
        return super.getInfo() + String.format(" | Season: %s",
                season);
    }
}
