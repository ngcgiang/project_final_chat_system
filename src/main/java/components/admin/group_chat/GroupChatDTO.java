package components.admin.group_chat;

public class GroupChatDTO {
    private int groupId;
    private String groupName;
    private String createdAt;
    private int amountOfMembers;

    // Constructor
    public GroupChatDTO(int groupId, String groupName, String createdAt, int amountOfMembers) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.createdAt = createdAt;
        this.amountOfMembers = amountOfMembers;
    }

    // Getters and Setters
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAmountOfMembers() {
        return amountOfMembers;
    }

    public void setAmountOfMembers(int amountOfMembers) {
        this.amountOfMembers = amountOfMembers;
    }
}
