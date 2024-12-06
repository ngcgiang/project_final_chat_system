package components.admin.group_chat;
import components.admin.*;
import java.sql.SQLException;
import java.util.List;

public class GroupChatBUS {
    private GroupChatDAO groupChatDAO;

    public GroupChatBUS() {
        this.groupChatDAO = new GroupChatDAO();
    }

    public List<GroupChatDTO> getAllGroups() {
        try {
            return groupChatDAO.getAllGroups();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching all groups.", e);
        }
    }

    public List<GroupChatDTO> searchGroups(String searchValue, String orderBy) {
        try {
            return groupChatDAO.searchGroups(searchValue, orderBy);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error searching groups.", e);
        }
    }

    public List<AdminUserDTO> getGroupMembers(int groupID) {
        return groupChatDAO.getGroupMembers(groupID);
    }

    public List<AdminUserDTO> getGroupAdmins(int groupID) {
        return groupChatDAO.getGroupAdmins(groupID);
    }
}
