package components.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DbConnection;
import java.sql.Statement;
import java.util.ArrayList;

public class GroupDAO {
    public GroupDTO getGroupInfo(int groupID) {
        String sql = "SELECT GroupID, GroupName, AdminID FROM group_info WHERE GroupID = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    GroupDTO group = new GroupDTO();
                    group.setGroupID(rs.getInt("GroupID"));
                    group.setGroupName(rs.getString("GroupName"));
                    group.setAdminID(rs.getInt("AdminID"));
                    return group;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addGroupInfo(GroupDTO groupDTO) {
        String sql = "INSERT INTO group_info (GroupName, AdminID) VALUES (?, ?)";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, groupDTO.getGroupName());
            stmt.setInt(2, groupDTO.getAdminID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Lấy GroupID được tạo tự động
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không thành công
    }

    public boolean addGroupMember(int groupID, String username) {
        // Truy vấn lấy UserID từ username
        String query = "INSERT INTO group_members (GroupID, UserID) VALUES (?, (SELECT UserID FROM users WHERE Username = ?))";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement insertMemberStmt = connection.prepareStatement(query);) {

            insertMemberStmt.setInt(1, groupID);
            insertMemberStmt.setString(2, username);

            int rowsAffected = insertMemberStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Nếu có lỗi xảy ra
    }

    public boolean removeGroupMember(int groupID, String username) {
        // Truy vấn lấy UserID từ username
        String query = "DELETE FROM group_members WHERE GroupID = ? AND UserID = (SELECT UserID FROM users WHERE Username = ?)";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setInt(1, groupID);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Nếu có lỗi xảy ra
    }

    public boolean addGroupMembers(int groupID, ArrayList<Integer> memberIDList) {
        String sql = "INSERT INTO group_members (GroupID, UserID) VALUES (?, ?)";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            int affectedRows = 0;
            for (int userID : memberIDList) {
                stmt.setInt(1, groupID); // Gán GroupID
                stmt.setInt(2, userID); // Gán UserID
                affectedRows += stmt.executeUpdate() > 0 ? 1 : 0; // Thực thi câu lệnh INSERT
            }
            return affectedRows > 0; // Nếu tất cả thành viên được thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu gặp lỗi
    }

    public boolean updateGroupInfo(GroupDTO groupDTO) {
        // Câu lệnh SQL để cập nhật thông tin nhóm
        String sql = "UPDATE group_info SET GroupName = ?, AdminID = ? WHERE GroupID = ?";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Thiết lập các tham số vào câu lệnh truy vấn
            stmt.setString(1, groupDTO.getGroupName()); // Tên nhóm mới
            stmt.setInt(2, groupDTO.getAdminID()); // ID của quản trị viên mới
            stmt.setInt(3, groupDTO.getGroupID()); // Mã nhóm cần cập nhật

            // Thực thi câu lệnh UPDATE
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào bị ảnh hưởng không
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Integer> getGroupMemberList(int groupID) {
        ArrayList<Integer> memberList = new ArrayList<>();
        String query = "SELECT UserID FROM group_members WHERE GroupID = ?";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, groupID); // Gán GroupID vào câu lệnh SQL
            ResultSet rs = stmt.executeQuery(); // Thực thi câu lệnh truy vấn

            while (rs.next()) {
                int userID = rs.getInt("UserID"); // Lấy UserID của thành viên
                memberList.add(userID); // Thêm vào danh sách
            }

        } catch (Exception e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }
        return memberList; // Trả về danh sách thành viên
    }

}
