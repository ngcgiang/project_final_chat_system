package components.conversation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import config.DbConnection;

public class ConversationDAO {
    public ArrayList<ConversationDTO> getConversationList(String username) {
        ArrayList<ConversationDTO> conversationList = new ArrayList<>();
        String query = """
                SELECT c.ConversationID, c.UserID, c.FriendID, c.LastMessage, c.LastMessageTime
                FROM conversations c
                JOIN users u ON c.UserID = u.UserID
                WHERE u.Username = ?
                ORDER BY c.LastMessageTime DESC;
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ConversationDTO conversation = new ConversationDTO();
                    conversation.setConversationID(rs.getInt("ConversationID"));
                    conversation.setUserID(rs.getInt("UserID"));
                    conversation.setFriendID(rs.getInt("FriendID"));
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

    public boolean addOrUpdateConversation(String username1, String username2, String lastMessage) {
        String queryInsertOrUpdate = """
                    INSERT INTO conversations (UserID, FriendID, LastMessage, LastMessageTime)
                    VALUES (
                        (SELECT UserID FROM users WHERE Username = ?),
                        (SELECT UserID FROM users WHERE Username = ?),
                        ?, CURRENT_TIMESTAMP
                    )
                    ON DUPLICATE KEY UPDATE
                        LastMessage = VALUES(LastMessage),
                        LastMessageTime = VALUES(LastMessageTime);
                """;

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(queryInsertOrUpdate)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            stmt.setString(3, lastMessage);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
