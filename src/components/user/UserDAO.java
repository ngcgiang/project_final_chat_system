package components.user;

import components.shared.utils.*;
import config.*;
import java.sql.*;

public class UserDAO {
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

    // Phương thức thêm User vào db
    public ResponseDTO addUser(UserDTO user) {
        String query = "INSERT INTO users (Username, Password) VALUES (?, ?)";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0) ? new ResponseDTO(true, "Registration successful!")
                    : new ResponseDTO(false, "Registration failure!");
        } catch (SQLException e) {
            return new ResponseDTO(false, e.getMessage());
        }
    }

    // Phương thức kiểm tra password được nhập vào có khớp với password của username
    // trong database không
    public ResponseDTO checkPassword(String username, String password) {
        String query = "SELECT Password FROM users WHERE Username = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("Password");
                if (password.equals(dbPassword)) {
                    return new ResponseDTO(true, "Login successful!");
                }
            }

            return new ResponseDTO(false, "Wrong password!");
        } catch (SQLException e) {
            return new ResponseDTO(false, e.getMessage());
        }
    }
}
