package components.user.relationship;

import java.sql.Connection;
import java.sql.PreparedStatement;

import config.DbConnection;

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

    public boolean removeFriendship(String username1, String username2) {
        String query = """
                DELETE FROM friends WHERE (User1ID = (SELECT UserID FROM users WHERE Username = ?) AND User2ID = (SELECT UserID FROM users WHERE Username = ?)) OR (User2ID = (SELECT UserID FROM users WHERE Username = ?) AND User1ID = (SELECT UserID FROM users WHERE Username = ?));
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username1);
            stmt.setString(2, username2);
            stmt.setString(3, username1);
            stmt.setString(4, username2);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addBlockingRelationship(String username1, String username2) {
        String query = """
                INSERT INTO block_list (BlockerID, BlockedID)
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

    public boolean addReport(String accused, String accuser, String reason) {
        String query = """
                INSERT INTO spam_reports (UserID, ReportedBy, Reason)
                VALUES (
                    (SELECT UserID FROM users WHERE Username = ?),
                    (SELECT UserID FROM users WHERE Username = ?),
                    ?
                );
                """;
        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accused);
            stmt.setString(2, accuser);
            stmt.setString(3, reason);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
