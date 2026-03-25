package model;

/**
 * History record for item borrows/returns.
 */
public class ItemHistoryRecord {
    private final String userName;
    private final String action;

    public ItemHistoryRecord(String userName, String action) {
        this.userName = userName;
        this.action = action;
    }

    public String getUserName() {
        return userName;
    }

    public String getAction() {
        return action;
    }

    // string representation
    public String toString() {
        return action + " " + userName;
    }
}
