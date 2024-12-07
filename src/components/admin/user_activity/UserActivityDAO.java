package components.admin.user_activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DbConnection;

public class UserActivityDAO {

    public List<UserActivityDTO> fetchUserActivities(String sortBy, String dateRange, String comparison, String compareTo, String username) {
        List<UserActivityDTO> userActivities = new ArrayList<>();

        StringBuilder query = new StringBuilder("""
            SELECT 
                u.UserID, 
                u.UserName, 
                COUNT(DISTINCT DATE(ua.LoginTime)) AS SessionsCount, 
                COUNT(DISTINCT m.ReceiverID) AS UniqueUsersMessaged,
                COUNT(DISTINCT gm.GroupID) AS UniqueGroupsMessaged,
                DATE(u.CreatedAt) AS CreatedAt
            FROM 
                Users u
            LEFT JOIN 
                user_activities ua ON u.UserID = ua.UserID
            LEFT JOIN 
                Messages m ON u.UserID = m.SenderID
            LEFT JOIN 
                group_messages gm ON u.UserID = gm.SenderID
            WHERE 1=1
        """);

        // Dynamically add WHERE clauses
        if (username != null && !username.trim().isEmpty()) {
            query.append(" AND u.UserName LIKE ?");
        }

        // Filter by date range
        switch (dateRange) {
            case "Today":
                query.append(" AND ua.LoginTime >= CURDATE()");
                break;
            case "Last 7 Days":
                query.append(" AND ua.LoginTime >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
                break;
            case "Last 30 Days":
                query.append(" AND ua.LoginTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)");
                break;
            case "All":
            default:
                break;
        }

        // Add GROUP BY clause
        query.append(" GROUP BY u.UserID, u.UserName");

        // Add HAVING clause
        if (comparison != null && !comparison.trim().isEmpty() && compareTo != null && !compareTo.trim().isEmpty()) {
            query.append(" HAVING SessionsCount ");
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
                default:
                    throw new IllegalArgumentException("Invalid comparison operator: " + comparison);
            }
        }

        // Add ORDER BY clause
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy) {
                case "Date Creation":
                    query.append(" ORDER BY CreatedAt DESC");
                    break;
                case "Username":
                    query.append(" ORDER BY u.UserName ASC");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy value: " + sortBy);
            }
        }

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;

            // Set parameters
            if (username != null && !username.trim().isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + username.trim() + "%");
            }

            if (comparison != null && !comparison.trim().isEmpty() && compareTo != null && !compareTo.trim().isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(compareTo.trim()));
            }

            ResultSet resultSet = preparedStatement.executeQuery();

            // Map ResultSet to DTO
            while (resultSet.next()) {
                UserActivityDTO dto = new UserActivityDTO();
                dto.setUserID(resultSet.getInt("UserID"));
                dto.setUserName(resultSet.getString("UserName"));
                dto.setSessionsCount(resultSet.getInt("SessionsCount"));
                dto.setUniqueUsersMessaged(resultSet.getInt("UniqueUsersMessaged"));
                dto.setUniqueGroupsMessaged(resultSet.getInt("UniqueGroupsMessaged"));
                dto.setCreatedAt(resultSet.getDate("CreatedAt"));
                userActivities.add(dto);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userActivities;
    }

    // Get data by year
    public static int[] fetchUserOnlineCountsByMonth(int year) {
        String query = """
            SELECT MONTH(LoginTime) AS Month, COUNT(ActivityID) AS SessionsCount
            FROM user_activities
            WHERE YEAR(LoginTime) = ?
            GROUP BY MONTH(LoginTime)
            ORDER BY Month ASC
        """;

        int[] userCounts = new int[12]; // Mặc định có 12 tháng (0-11 cho các tháng)

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Set parameter for year
            preparedStatement.setInt(1, year);

            // Execute query
            ResultSet rs = preparedStatement.executeQuery();

            // Process results
            while (rs.next()) {
                int month = rs.getInt("Month"); // Tháng (1-12)
                int count = rs.getInt("SessionsCount"); // Số lượt hoạt động
                userCounts[month - 1] = count; // Chuyển về chỉ số mảng (0-11)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userCounts;
    }
}
