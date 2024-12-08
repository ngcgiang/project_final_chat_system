package components.conversation;

import components.shared.utils.CurrentUser;
import components.user.UserBUS;
import config.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ConversationDAO {
    public ArrayList<ConversationDTO> getConversationList(String username) {
        ArrayList<ConversationDTO> conversationList = new ArrayList<>();

        // Truy vấn SQL để lấy thông tin cuộc hội thoại cho User1ID hoặc User2ID
        String query = """
                SELECT c.ConversationID, c.User1ID, c.User2ID, c.LastMessage, c.LastMessageTime
                FROM conversations c
                JOIN users u ON (c.User1ID = u.UserID OR c.User2ID = u.UserID)
                WHERE u.Username = ?
                ORDER BY c.LastMessageTime DESC;
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            // Thiết lập tham số cho câu lệnh truy vấn
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ConversationDTO conversation = new ConversationDTO();
                    conversation.setConversationID(rs.getInt("ConversationID"));
                    conversation.setUserID(rs.getInt("User1ID"));

                    // Kiểm tra xem User1 hay User2 là bạn của người dùng hiện tại
                    UserBUS userBUS = new UserBUS();
                    if (rs.getInt("User1ID") == userBUS.getAccountInfo(CurrentUser.getInstance().getUsername())
                            .getID()) {
                        conversation.setFriendID(rs.getInt("User2ID"));
                    } else {
                        conversation.setFriendID(rs.getInt("User1ID"));
                    }

                    conversation.setLastMessage(rs.getString("LastMessage"));
                    conversation.setLastMessageTime(rs.getTimestamp("LastMessageTime"));
                    conversationList.add(conversation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conversationList;
    }

    public ArrayList<ConversationDTO> getGroupConversationList(String username) {
        ArrayList<ConversationDTO> conversationList = new ArrayList<>();

        String sql = """
                SELECT gc.ConversationID, gc.GroupID, gc.LastMessage, gc.LastMessageTime, gc.SenderID
                FROM group_conversations gc
                JOIN group_members gm ON gc.GroupID = gm.GroupID
                JOIN group_info g ON gc.GroupID = g.GroupID
                WHERE gm.UserID = (SELECT UserID FROM users WHERE Username = ?)
                ORDER BY gc.LastMessageTime DESC;
                                """;

        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int conversationID = rs.getInt("ConversationID");
                    int groupID = rs.getInt("GroupID");
                    String lastMessage = rs.getString("LastMessage");
                    Timestamp lastMessageTime = rs.getTimestamp("LastMessageTime");
                    int senderID = rs.getInt("SenderID");

                    // Tạo đối tượng ConversationDTO và thêm vào danh sách
                    ConversationDTO conversationDTO = new ConversationDTO(conversationID, senderID, groupID,
                            lastMessage, lastMessageTime);
                    conversationList.add(conversationDTO);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conversationList;
    }

    public boolean addOrUpdateConversation(String username1, String username2, String lastMessage) {
        // Truy vấn để lấy ID của các người dùng
        String queryGetUserIDs = """
                    SELECT UserID FROM users WHERE Username = ?
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement getUserIDsStmt = connection.prepareStatement(queryGetUserIDs)) {

            // Thiết lập tham số vào câu lệnh truy vấn
            getUserIDsStmt.setString(1, username1);
            ResultSet rs1 = getUserIDsStmt.executeQuery();
            int user1ID = -1;
            if (rs1.next()) {
                user1ID = rs1.getInt("UserID");
            }

            getUserIDsStmt.setString(1, username2);
            ResultSet rs2 = getUserIDsStmt.executeQuery();
            int user2ID = -1;
            if (rs2.next()) {
                user2ID = rs2.getInt("UserID");
            }

            // Đảm bảo User1ID nhỏ hơn User2ID
            if (user1ID > user2ID) {
                int temp = user1ID;
                user1ID = user2ID;
                user2ID = temp;
            }

            // Truy vấn để chèn hoặc cập nhật cuộc hội thoại
            String queryInsertOrUpdate = """
                        INSERT INTO conversations (User1ID, User2ID, LastMessage, LastMessageTime)
                        VALUES (?, ?, ?, CURRENT_TIMESTAMP)
                        ON DUPLICATE KEY UPDATE
                            LastMessage = VALUES(LastMessage),
                            LastMessageTime = VALUES(LastMessageTime);
                    """;

            try (PreparedStatement stmt = connection.prepareStatement(queryInsertOrUpdate)) {
                // Thiết lập tham số vào câu lệnh truy vấn
                stmt.setInt(1, user1ID);
                stmt.setInt(2, user2ID);
                stmt.setString(3, lastMessage);

                // Thực thi câu lệnh và kiểm tra kết quả
                return stmt.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addOrUpdateGroupConversation(int senderID, int groupID, String lastMessage) {
        String checkSQL = "SELECT ConversationID FROM group_conversations WHERE GroupID = ?";
        String insertSQL = "INSERT INTO group_conversations (GroupID, LastMessage, LastMessageTime, SenderID) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        String updateSQL = "UPDATE group_conversations SET LastMessage = ?, LastMessageTime = CURRENT_TIMESTAMP, SenderID = ? WHERE GroupID = ?";

        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(checkSQL);
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

            // Kiểm tra xem cuộc hội thoại nhóm đã tồn tại hay chưa
            checkStmt.setInt(1, groupID);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Cập nhật cuộc hội thoại nếu đã tồn tại
                    updateStmt.setString(1, lastMessage);
                    updateStmt.setInt(2, senderID);
                    updateStmt.setInt(3, groupID);
                    return updateStmt.executeUpdate() > 0;
                } else {
                    // Thêm mới cuộc hội thoại nếu chưa tồn tại
                    insertStmt.setInt(1, groupID);
                    insertStmt.setString(2, lastMessage);
                    insertStmt.setInt(3, senderID);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteConversation(String username1, String username2) {
        String queryGetUserIDs = """
                    SELECT UserID FROM users WHERE Username = ?
                """;
        String queryDeleteConversation = """
                    DELETE FROM conversations
                    WHERE User1ID = ? AND User2ID = ?;
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement getUserIDsStmt = connection.prepareStatement(queryGetUserIDs)) {

            // Lấy UserID của username1
            getUserIDsStmt.setString(1, username1);
            ResultSet rs1 = getUserIDsStmt.executeQuery();
            int user1ID = -1;
            if (rs1.next()) {
                user1ID = rs1.getInt("UserID");
            }

            // Lấy UserID của username2
            getUserIDsStmt.setString(1, username2);
            ResultSet rs2 = getUserIDsStmt.executeQuery();
            int user2ID = -1;
            if (rs2.next()) {
                user2ID = rs2.getInt("UserID");
            }

            // Đảm bảo User1ID luôn nhỏ hơn User2ID
            if (user1ID > user2ID) {
                int temp = user1ID;
                user1ID = user2ID;
                user2ID = temp;
            }

            // Thực hiện xóa cuộc hội thoại
            try (PreparedStatement deleteStmt = connection.prepareStatement(queryDeleteConversation)) {
                deleteStmt.setInt(1, user1ID);
                deleteStmt.setInt(2, user2ID);
                return deleteStmt.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteGroupConversation(int groupID) {
        String sql = "DELETE FROM group_conversations WHERE GroupID = ?";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Thiết lập tham số GroupID vào câu lệnh truy vấn
            stmt.setInt(1, groupID);

            // Thực thi câu lệnh DELETE
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào bị xóa không
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
