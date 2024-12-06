package components.admin.login_history;

import config.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryDAO {
    public List<LoginHistoryDTO> getAllLoginHistory() {
        String query = """
                SELECT ua.LoginTime, u.UserID, u.Username, u.FullName
                FROM user_activities ua
                JOIN Users u ON ua.UserID = u.UserID
                ORDER BY ua.LoginTime DESC
                """;
        List<LoginHistoryDTO> loginHistories = new ArrayList<>();

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LoginHistoryDTO history = new LoginHistoryDTO();
                history.setLoginTime(resultSet.getString("LoginTime"));
                history.setUserId(resultSet.getInt("UserID"));
                history.setUsername(resultSet.getString("Username"));
                history.setFullName(resultSet.getString("FullName"));
                loginHistories.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loginHistories;
    }

    public List<LoginHistoryDTO> getUserLoginHistory(int userId) {
        String query = """
                SELECT LoginTime, LogoutTime, ActivityType
                FROM user_activities
                WHERE UserID = ?
                ORDER BY LoginTime DESC
                """;
        List<LoginHistoryDTO> loginHistories = new ArrayList<>();

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LoginHistoryDTO history = new LoginHistoryDTO();
                    history.setLoginTime(resultSet.getString("LoginTime"));
                    history.setLogoutTime(resultSet.getString("LogoutTime"));
                    history.setActivityType(resultSet.getString("ActivityType"));
                    loginHistories.add(history);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loginHistories;
    }
}
