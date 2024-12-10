package components.admin.spam_reports;

import config.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpamReportDAO {
    public List<SpamReportDTO> getSpamReports(String sortBy, String timeFilter, String usernameFilter) {
        List<SpamReportDTO> reports = new ArrayList<>();
        String query = """
                SELECT u.UserID, u.Username, rp.UserID AS ReporterID, sp.ReportTime, sp.Reason, u.Access
                FROM spam_reports sp
                INNER JOIN Users u ON u.UserID = sp.UserID
                INNER JOIN Users rp ON rp.UserID = sp.ReportedBy
                WHERE 1=1
            """;

        // Append filters
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            query += " AND u.Username LIKE ?";
        }

        switch (timeFilter) {
            case "Today" -> query += " AND sp.ReportTime >= CURDATE()";
            case "Last 7 Days" -> query += " AND sp.ReportTime >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
            case "Last 30 Days" -> query += " AND sp.ReportTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        }

        if (sortBy != null) {
            switch (sortBy) {
                case "Sort by Time" -> query += " ORDER BY sp.ReportTime DESC";
                case "Sort by Username" -> query += " ORDER BY u.Username ASC";
            }
        }

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            // Bind parameters
            int paramIndex = 1;
            if (usernameFilter != null && !usernameFilter.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + usernameFilter + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    SpamReportDTO report = new SpamReportDTO();
                    report.setUserId(resultSet.getInt("UserID"));
                    report.setUsername(resultSet.getString("Username"));
                    report.setReportedBy(resultSet.getInt("ReporterID"));
                    report.setReportTime(resultSet.getTimestamp("ReportTime"));
                    report.setReason(resultSet.getString("Reason"));
                    report.setAccess(resultSet.getString("Access"));
                    reports.add(report);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return reports;
    }

    public boolean updateUserAccess(int userId, String newAccess) {
        String query = "UPDATE Users SET Access = ? WHERE UserID = ?";
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setString(1, newAccess);
            preparedStatement.setInt(2, userId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}