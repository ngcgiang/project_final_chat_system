package components.admin;

import components.shared.utils.Response;
import config.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminUserDAO {

    // Phương thức kiểm tra xem username đã tồn tại trong cơ sở dữ liệu chưa
    public boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE Username = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

     // Phương thức kiểm tra password được nhập vào có khớp với password của username
    // trong database không
    public Response checkPassword(String username, String password) {
        String query = "SELECT Password FROM users WHERE Username = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("Password");
                if (password.equals(dbPassword)) {
                    return new Response(true, "Login successful!");
                }
            }

            return new Response(false, "Wrong password!");
        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }

    public boolean isAdmin(String username) {
        String query = """
                SELECT a.UserID
                FROM Administrators a
                Join Users u ON a.UserID = u.UserID
                WHERE u.Username = ? 
            """;
        try (Connection conn = new DbConnection().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AdminUserDTO> getAllUsers() {
        List<AdminUserDTO> userList = new ArrayList<>();
        String query = "SELECT * FROM Users";

        try (Connection conn = new DbConnection().getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                AdminUserDTO user = new AdminUserDTO(
                    resultSet.getInt("UserID"),
                    resultSet.getString("Username"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Address"),
                    resultSet.getDate("DateOfBirth"),
                    resultSet.getString("Gender"),
                    resultSet.getString("Email"),
                    resultSet.getString("Status"),
                    resultSet.getTimestamp("createdAt")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    //reload if has changes or search 
    public List<AdminUserDTO> reloadUserData(String searchValue, String filterColumn) {
        List<AdminUserDTO> userList = new ArrayList<>();
        String query = "SELECT * FROM Users";

        // If search criteria are provided, add WHERE clause
        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null) {
            query += " WHERE " + filterColumn + " LIKE ?";
        }

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            // Set the search value if provided
            if (searchValue != null && !searchValue.isEmpty() && filterColumn != null) {
                statement.setString(1, "%" + searchValue + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                AdminUserDTO user = new AdminUserDTO();
                user.setUserId(resultSet.getInt("UserID"));
                user.setUsername(resultSet.getString("Username"));
                user.setFullName(resultSet.getString("FullName"));
                user.setAddress(resultSet.getString("Address"));
                user.setDateOfBirth(resultSet.getDate("DateOfBirth"));
                user.setGender(resultSet.getString("Gender"));
                user.setEmail(resultSet.getString("Email"));
                user.setStatus(resultSet.getString("Status"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public List<AdminUserDTO> loadNewUserData(String sortBy, String time, String username) {
        List<AdminUserDTO> newUserList = new ArrayList<>();
        StringBuilder query = new StringBuilder("""
                SELECT u.UserID, u.Username, u.CreatedAt
                FROM Users u
                WHERE 1=1
            """);
    
        // Append WHERE clause for username search
        if (username != null && !username.isEmpty()) {
            query.append(" AND u.Username LIKE ?");
        }
    
        // Append WHERE clause for time filter
        if (time != null) {
            switch (time) {
                case "Today" -> query.append(" AND u.CreatedAt >= CURDATE()");
                case "Last 7 Days" -> query.append(" AND u.CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
                case "Last 30 Days" -> query.append(" AND u.CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)");
                case "All" -> { /* No additional condition */ }
                default -> throw new IllegalArgumentException("Invalid time value: " + time);
            }
        }
    
        // Append ORDER BY clause for sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "Registration time" -> query.append(" ORDER BY u.CreatedAt DESC");
                case "Username" -> query.append(" ORDER BY u.Username");
                default -> throw new IllegalArgumentException("Invalid sortBy value: " + sortBy);
            }
        }
    
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query.toString())) {
    
            // Set parameters for username search
            int paramIndex = 1;
            if (username != null && !username.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + username + "%");
            }
    
            // Execute query and fetch results
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    AdminUserDTO user = new AdminUserDTO();
                    user.setUserId(resultSet.getInt("UserID"));
                    user.setUsername(resultSet.getString("Username"));
                    user.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
                    newUserList.add(user);
                }
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return newUserList;
    }

    public int[] fetchUserCountsByMonth(int year) {
        String query = """
                SELECT MONTH(CreatedAt) AS Month, COUNT(UserID) AS NewRegistrations
                FROM Users
                WHERE YEAR(CreatedAt) = ?
                GROUP BY MONTH(CreatedAt)
                ORDER BY Month ASC
            """;
    
        int[] userCounts = new int[12]; // Mặc định có 12 tháng (0-11 cho các tháng), khởi tạo giá trị 0
    
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
    
            // Set parameter cho năm cần thống kê
            preparedStatement.setInt(1, year);
    
            // Thực thi câu truy vấn
            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Xử lý kết quả trả về
                while (rs.next()) {
                    int month = rs.getInt("Month"); // Lấy tháng (1-12)
                    int count = rs.getInt("NewRegistrations"); // Lấy số lượng đăng ký mới
                    userCounts[month - 1] = count; // Đưa giá trị vào mảng, chuyển về chỉ số (0-11)
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            // Nếu có log, có thể thêm ghi log thay vì chỉ in lỗi ra console
        }
    
        return userCounts;
    }
    
    
    public boolean updateUser(AdminUserDTO user) {
        String query = "UPDATE Users SET FullName = ?, Address = ?, DateOfBirth = ?, " +
                       "Gender = ?, Email = ?, Status = ? WHERE UserID = ?";
    
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
    
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getAddress());
    
            // Chuyển đổi java.util.Date sang java.sql.Date
            statement.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
    
            statement.setString(4, user.getGender());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getStatus());
            statement.setInt(7, user.getUserId());
    
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE UserID = ?";

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUserAccess(int userId) {
        String query = "SELECT Access FROM Users WHERE UserID = ?";
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Access");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found or an error occurred
    }

    public boolean updateUserAccess(int userId, String newAccess) {
        String query = "UPDATE Users SET Access = ? WHERE UserID = ?";
        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, newAccess);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Return false if update failed
    }

    public boolean updatePassword(int userId, String newPassword) {
        String query = "UPDATE Users SET Password = ? WHERE UserID = ?";

        try (Connection conn = new DbConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, newPassword);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
