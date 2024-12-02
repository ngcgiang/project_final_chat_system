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
    public Response addUser(UserDTO user) {
        String query = "INSERT INTO users (Username, Password) VALUES (?, ?)";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0) ? new Response(true, "Registration successful!")
                    : new Response(false, "Registration failure!");
        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
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

    public UserDTO getOne(String username) {
        String query = "SELECT Password, FullName, Address, Email, Phone, Gender, DateOfBirth FROM users WHERE Username = ?";
        UserDTO user = null;

        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    // Tạo đối tượng UserDTO và ánh xạ dữ liệu từ ResultSet
                    user = new UserDTO();
                    user.setUsername(username);
                    user.setPassword(rs.getString("Password"));
                    user.setFullName(rs.getString("FullName"));
                    user.setAddress(rs.getString("Address"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhone(rs.getString("Phone"));
                    user.setGender(rs.getString("Gender"));
                    user.setDob(rs.getDate("DateOfBirth"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public Response updateOne(String username, String password, String fullName, String address, java.util.Date dob,
            String email,
            String phone,
            String gender) {
        String query = "UPDATE users SET Password = ?, FullName = ?, Address = ?, DateOfBirth = ?, Email = ?, Phone = ?, Gender = ? WHERE Username = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, fullName);
            stmt.setString(3, address);
            stmt.setDate(4, new java.sql.Date(dob.getTime()));
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setString(7, gender);
            stmt.setString(8, username);

            return stmt.executeUpdate() > 0 ? new Response(true, "Update sucessful!")
                    : new Response(false, "Update failure!");
        } catch (SQLException e) {
            return new Response(false, e.getMessage());
        }
    }
}
