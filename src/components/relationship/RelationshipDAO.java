package components.relationship;

import components.shared.utils.DbConnection;
import config.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RelationshipDAO {
    public boolean addFriendRequest(String username1, String username2) {
        String query = """
                INSERT INTO friend_requests (SenderID, ReceiverID)
                VALUES (
                    (SELECT UserID FROM users WHERE Username = ?),
                    (SELECT UserID FROM users WHERE Username = ?)
                );
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeFriendRequest(String username1, String username2) {
        String query = """
                DELETE FROM friend_requests WHERE SenderID = (SELECT UserID FROM users WHERE Username = ?) AND ReceiverID = (SELECT UserID FROM users WHERE Username = ?);
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addFriendship(String username1, String username2) {
        String query = """
                INSERT INTO friends (User1ID, User2ID)
                VALUES (
                    (SELECT UserID FROM users WHERE Username = ?),
                    (SELECT UserID FROM users WHERE Username = ?)
                );
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
