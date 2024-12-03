package components.admin;

import config.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminUserDAO {

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
                    resultSet.getString("Status")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
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

}
