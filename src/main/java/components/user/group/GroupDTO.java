package components.user.group;

public class GroupDTO {
    private int groupID; // Mã nhóm
    private String groupName; // Tên nhóm
    private int adminID; // Quản trị viên nhóm

    // Constructor không tham số
    public GroupDTO() {
    }

    // Constructor có tham số
    public GroupDTO(String groupName, int adminID) {
        this.groupName = groupName;
        this.adminID = adminID;
    }

    // Getter và Setter cho groupID
    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    // Getter và Setter cho groupName
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    // Getter và Setter cho adminID
    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "groupID=" + groupID +
                ", groupName='" + groupName + '\'' +
                ", adminID=" + adminID +
                '}';
    }
}
