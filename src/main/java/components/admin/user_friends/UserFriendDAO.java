package components.admin.user_friends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DbConnection;

public class UserFriendDAO {
    public List<UserFriendDTO> getAllUserFriends() throws SQLException {
        List<UserFriendDTO> userFriends = new ArrayList<>();

        String query = """
                SELECT u.UserID, u.FullName, COUNT(f.User2ID) AS FriendCount
                FROM Friends f
                INNER JOIN Users u ON f.User1ID = u.UserID
                GROUP BY u.UserID, u.FullName
                ORDER BY u.UserID
                """;
        try (Connection conn = new DbConnection().getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("UserID");
                String fullName = resultSet.getString("FullName");
                int friendCount = resultSet.getInt("FriendCount");
                userFriends.add(new UserFriendDTO(userId, fullName, friendCount, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userFriends;
    }

    public List<UserFriendDTO> getUserFriendsByUserId(int userId) throws SQLException {
        List<UserFriendDTO> userFriends = new ArrayList<>();

        String query = """
                SELECT u.UserID, u.FullName, MIN(f.CreatedAt) AS DateOfCreation
                FROM Friends f
                INNER JOIN Users u ON f.User2ID = u.UserID
                WHERE f.User1ID = ?
                GROUP BY u.UserID, u.FullName
                ORDER BY u.UserID
                """;

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int friendUserId = resultSet.getInt("UserID");
                    String friendFullname = resultSet.getString("FullName");
                    String dateOfCreation = resultSet.getString("DateOfCreation");
                    userFriends.add(new UserFriendDTO(friendUserId, friendFullname, 0, dateOfCreation));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userFriends;
    }

    public List<UserFriendDTO> getFilteredUserFriends(String sortBy, String comparison, String compareTo, String fullName) throws SQLException {
        List<UserFriendDTO> userFriends = new ArrayList<>();
        
        StringBuilder query = new StringBuilder("""
                SELECT u.UserID, u.FullName, COUNT(f.User2ID) AS FriendCount
                FROM Friends f
                INNER JOIN Users u ON f.User1ID = u.UserID
                WHERE 1=1
            """);

        // Dynamically adding WHERE clauses based on parameters
        if (fullName != null && !fullName.isEmpty()) {
            query.append(" AND u.FullName LIKE ?");
        }

        query.append(" GROUP BY u.UserID, u.FullName");

        if (comparison != null && !comparison.isEmpty() && compareTo != null && !compareTo.isEmpty()) {
            query.append(" HAVING FriendCount ");
            switch (comparison) {
                case "Equal to":
                    query.append("= ?");
                    break;
                case "Greater than":
                    query.append("> ?");
                    break;
                case "Less than":
                    query.append("< ?");
                    break;
            }
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            query.append(" ORDER BY u.").append(sortBy);
        }

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            if (fullName != null && !fullName.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + fullName + "%");
            }

            if (comparison != null && !comparison.isEmpty() && compareTo != null && !compareTo.isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(compareTo));
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int userId = resultSet.getInt("UserID");
                    String fullNameResult = resultSet.getString("FullName");
                    int friendCount = resultSet.getInt("FriendCount");
                    userFriends.add(new UserFriendDTO(userId, fullNameResult, friendCount, null));
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userFriends;
    }
}
