package components.admin.user_friends;

public class UserFriendDTO {
    private int userId;
    private String username;
    private int friendCount;
    private String dateOfCreation;

    // Constructor, getters, and setters
    public UserFriendDTO(int userId, String username, int friendCount, String dateOfCreation) {
        this.userId = userId;
        this.username = username;
        this.friendCount = friendCount;
        this.dateOfCreation = dateOfCreation;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(int friendCount) {
        this.friendCount = friendCount;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
