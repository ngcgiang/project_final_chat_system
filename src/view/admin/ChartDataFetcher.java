import java.sql.*;

public class ChartDataFetcher {
    // Get data by year
    public static int[] fetchUserCountsByMonth(int year) {
        String query = "SELECT MONTH(CreatedAt) AS Month, COUNT(UserID) AS NewRegistrations "
                     + "FROM Users "
                     + "WHERE YEAR(CreatedAt) = ? "
                     + "GROUP BY MONTH(CreatedAt) "
                     + "ORDER BY Month ASC";

        int[] userCounts = new int[12]; // Mặc định có 12 tháng (0-11 cho các tháng)

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameter for year
            preparedStatement.setInt(1, year);

            // Execute query
            ResultSet rs = preparedStatement.executeQuery();

            // Process results
            while (rs.next()) {
                int month = rs.getInt("Month"); // Tháng (1-12)
                int count = rs.getInt("NewRegistrations"); // Số đăng ký mới
                userCounts[month - 1] = count; // Chuyển về chỉ số mảng (0-11)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userCounts;
    }
}
