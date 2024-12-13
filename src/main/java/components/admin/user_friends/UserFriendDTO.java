package components.admin.user_friends;

public class UserFriendDTO {
    private int userId;
    private String username;
    private int friendCount;
    private String dateOfCreation;
    private String fullName;

    // Constructor, getters, and setters
    public UserFriendDTO(int userId, String fullName, int friendCount, String dateOfCreation) {
        this.userId = userId;
        this.username = fullName;
        this.friendCount = friendCount;
        this.dateOfCreation = dateOfCreation;
        this.fullName = fullName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
