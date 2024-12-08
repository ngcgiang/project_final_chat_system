package components.admin.group_chat;
import components.admin.*;
import config.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupChatDAO {

    public List<GroupChatDTO> getAllGroups() throws SQLException {
        List<GroupChatDTO> groupList = new ArrayList<>();
        String query = """
            SELECT g.GroupID, g.GroupName, g.CreatedAt, COUNT(gm.UserID) AS AmountOfMember
            FROM group_info g
            INNER JOIN group_members gm ON gm.GroupID = g.GroupID
            GROUP BY g.GroupID, g.GroupName, g.CreatedAt
            ORDER BY g.GroupID
        """;

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                GroupChatDTO group = new GroupChatDTO(
                    rs.getInt("GroupID"),
                    rs.getString("GroupName"),
                    rs.getString("CreatedAt"),
                    rs.getInt("AmountOfMember")
                );
                groupList.add(group);
            }
        }

        return groupList;
    }

    public List<GroupChatDTO> searchGroups(String searchValue, String orderBy) throws SQLException {
        List<GroupChatDTO> groupList = new ArrayList<>();
        String query = """
            SELECT g.GroupID, g.GroupName, g.CreatedAt, COUNT(gm.UserID) AS AmountOfMember
            FROM group_info g
            INNER JOIN group_members gm ON gm.GroupID = g.GroupID
            WHERE g.GroupName LIKE ?
            GROUP BY g.GroupID, g.GroupName, g.CreatedAt
            ORDER BY %s
        """.formatted(orderBy);

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchValue + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    GroupChatDTO group = new GroupChatDTO(
                        rs.getInt("GroupID"),
                        rs.getString("GroupName"),
                        rs.getString("CreatedAt"),
                        rs.getInt("AmountOfMember")
                    );
                    groupList.add(group);
                }
            }
        }

        return groupList;
    }

     public List<AdminUserDTO> getGroupMembers(int groupID) {
        List<AdminUserDTO> members = new ArrayList<>();
        String query = """
            SELECT u.UserID, u.UserName, u.FullName
            FROM Users u
            INNER JOIN group_members gm ON gm.UserID = u.UserID
            WHERE gm.GroupID = ?
            GROUP BY u.UserID, u.UserName, u.FullName
            ORDER BY u.UserID
            """;
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AdminUserDTO user = new AdminUserDTO();
                user.setUserId(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("UserName"));
                user.setFullName(resultSet.getString("FullName"));
                members.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return members;
    }

    public List<AdminUserDTO> getGroupAdmins(int groupID) {
        List<AdminUserDTO> admins = new ArrayList<>();
        String query = """
            SELECT u.UserID, u.UserName, u.FullName
            FROM Users u
            INNER JOIN group_info g ON g.AdminID = u.UserID
            WHERE g.GroupID = ?
            GROUP BY u.UserID, u.UserName, u.FullName
            ORDER BY u.UserID
            """;
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, groupID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                AdminUserDTO user = new AdminUserDTO();
                user.setUserId(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("UserName"));
                user.setFullName(resultSet.getString("FullName"));
                admins.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return admins;
    }
}
