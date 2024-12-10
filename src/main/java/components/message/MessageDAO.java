package components.message;

import config.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MessageDAO {
    public boolean saveMessage(String sender, String receiver, String content) {
        String query = """
                INSERT INTO messages (SenderID, ReceiverID, Content)
                VALUES (
                    (SELECT UserID FROM users WHERE Username = ?),
                    (SELECT UserID FROM users WHERE Username = ?),
                    ?
                );
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, content);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveGroupMessage(int senderID, int groupID, String content) {
        String sql = "INSERT INTO group_messages (GroupID, SenderID, Content) VALUES (?, ?, ?)";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho câu lệnh SQL
            stmt.setInt(1, groupID);
            stmt.setInt(2, senderID);
            stmt.setString(3, content);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Trả về true nếu lưu thành công
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi
    }

    public ArrayList<MessageDTO> getMessages(String username1, String username2) {
        ArrayList<MessageDTO> messages = new ArrayList<>();
        String query = """
                SELECT
                    m.MessageID,
                    m.SenderID,
                    m.ReceiverID,
                    m.Content,
                    m.SentAt
                FROM messages m
                WHERE
                    (m.SenderID = (SELECT UserID FROM users WHERE Username = ?) AND
                     m.ReceiverID = (SELECT UserID FROM users WHERE Username = ?))
                    OR
                    (m.SenderID = (SELECT UserID FROM users WHERE Username = ?) AND
                     m.ReceiverID = (SELECT UserID FROM users WHERE Username = ?))
                ORDER BY m.SentAt ASC;
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            // Gán giá trị tham số cho PreparedStatement
            stmt.setString(1, username1); // Sender = username1
            stmt.setString(2, username2); // Receiver = username2
            stmt.setString(3, username2); // Sender = username2
            stmt.setString(4, username1); // Receiver = username1

            // Thực thi truy vấn
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Tạo một đối tượng MessageDTO
                    MessageDTO message = new MessageDTO();
                    message.setMessageID(rs.getInt("MessageID"));
                    message.setSenderID(rs.getInt("SenderID"));
                    message.setReceiverID(rs.getInt("ReceiverID"));
                    message.setContent(rs.getString("Content"));
                    message.setSentAt(rs.getTimestamp("SentAt"));

                    // Thêm vào danh sách
                    messages.add(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return messages;
    }

    public ArrayList<MessageDTO> getGroupMessages(int groupID) {
        String sql = "SELECT MessageID, SenderID, Content, SentAt FROM group_messages WHERE GroupID = ?";
        ArrayList<MessageDTO> messageList = new ArrayList<>();

        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập giá trị cho tham số truy vấn
            stmt.setInt(1, groupID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Lấy dữ liệu từ từng hàng
                    int messageID = rs.getInt("MessageID");
                    int senderID = rs.getInt("SenderID");
                    String content = rs.getString("Content");
                    Timestamp sentAt = rs.getTimestamp("SentAt");

                    // Tạo đối tượng MessageDTO
                    MessageDTO message = new MessageDTO(messageID, senderID, groupID, content, sentAt);
                    messageList.add(message);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageList; // Trả về danh sách tin nhắn
    }

    public boolean deleteMessage(int messageID) {
        String sql = "DELETE FROM messages WHERE MessageID = ?";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Thiết lập tham số GroupID vào câu lệnh truy vấn
            stmt.setInt(1, messageID);

            // Thực thi câu lệnh DELETE
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào bị xóa không
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllMessages(String username1, String username2) {
        String sql = "DELETE FROM messages WHERE (SenderID = (SELECT UserID FROM users WHERE Username = ?) AND ReceiverID = (SELECT UserID FROM users WHERE Username = ?)) OR (SenderID = (SELECT UserID FROM users WHERE Username = ?) AND ReceiverID = (SELECT UserID FROM users WHERE Username = ?))";

        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username1);
            stmt.setString(2, username2);
            stmt.setString(3, username2);
            stmt.setString(4, username1);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteGroupMessage(int messageID) {
        String sql = "DELETE FROM group_messages WHERE MessageID = ?";

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Thiết lập tham số GroupID vào câu lệnh truy vấn
            stmt.setInt(1, messageID);

            // Thực thi câu lệnh DELETE
            int rowsAffected = stmt.executeUpdate();

            // Kiểm tra xem có dòng nào bị xóa không
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllGroupMessages(int groupID) {
        String sql = "DELETE FROM group_messages WHERE GroupID = ?";

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
