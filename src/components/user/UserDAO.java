package components.user;

import components.shared.utils.*;
import config.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public ArrayList<UserDTO> getFriendList(String username) {
        String query = """
                SELECT u.FullName, u.Status
                FROM users u
                JOIN friends f ON u.UserID = f.User2ID
                WHERE f.User1ID = (SELECT UserID FROM users where Username = ?); """;
        ArrayList<UserDTO> friendList = new ArrayList<>();
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO friend = new UserDTO();
                friend.setFullName(rs.getString("FullName"));
                friend.setStatus(rs.getString("Status"));
                friendList.add(friend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return friendList;
    }

    public Map<UserDTO, String> getUserList(String username) {
        String query = """
                SELECT
                    u.Username,
                    u.FullName,
                    CASE
                        WHEN f.FriendshipID IS NOT NULL THEN 3
                        WHEN f_r_2.RequestID IS NOT NULL THEN 2
                        WHEN f_r.RequestID IS NOT NULL THEN 1
                        ELSE 0
                    END AS Status
                FROM users u
                LEFT JOIN friends f
                    ON (u.UserID = f.User1ID AND f.User2ID = (SELECT UserID FROM users WHERE Username = ?))
                    OR (u.UserID = f.User2ID AND f.User1ID = (SELECT UserID FROM users WHERE Username = ?))
                LEFT JOIN friend_requests f_r
                    ON (u.UserID = f_r.ReceiverID AND f_r.SenderID = (SELECT UserID FROM users WHERE Username = ?))
                LEFT JOIN friend_requests f_r_2
                    ON (u.UserID = f_r_2.SenderID AND f_r_2.ReceiverID = (SELECT UserID FROM users WHERE Username = ?))
                WHERE u.UserID NOT IN (
                    SELECT b.BlockerID
                    FROM block_list b
                    WHERE b.BlockedID = (SELECT UserID FROM users WHERE Username = ?)
                )
                AND u.UserID != (SELECT UserID FROM users WHERE Username = ?);
                                """;

        Map<UserDTO, String> userMap = new HashMap<>();

        try (Connection connection = new DbConnection().getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 1; i < 7; i++) {
                stmt.setString(i, username);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UserDTO user = new UserDTO();
                user.setUsername(rs.getString(1));
                user.setFullName(rs.getString(2));
                int status = rs.getInt(3);

                userMap.put(user, String.valueOf(status));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userMap;
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

    public void setStatus(String username, String status) {
        String query = "UPDATE users SET Status = ? WHERE Username = ?";
        try (Connection conn = new DbConnection().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, (status.equals("Online") ? "Online" : "Offline"));
            stmt.setString(2, username);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
