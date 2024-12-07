package components.admin.user_activity;

public class UserActivityDTO {
    private int userID;
    private String userName;
    private int sessionsCount;
    private int uniqueUsersMessaged;
    private int uniqueGroupsMessaged;
    private java.util.Date createdAt;

    // Constructor
    public UserActivityDTO() {}

    // Getter và Setter
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSessionsCount() {
        return sessionsCount;
    }

    public void setSessionsCount(int sessionsCount) {
        this.sessionsCount = sessionsCount;
    }

    public int getUniqueUsersMessaged() {
        return uniqueUsersMessaged;
    }

    public void setUniqueUsersMessaged(int uniqueUsersMessaged) {
        this.uniqueUsersMessaged = uniqueUsersMessaged;
    }

    public int getUniqueGroupsMessaged() {
        return uniqueGroupsMessaged;
    }

    public void setUniqueGroupsMessaged(int uniqueGroupsMessaged) {
        this.uniqueGroupsMessaged = uniqueGroupsMessaged;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }
}
