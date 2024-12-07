package components.message;

import config.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

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
}
