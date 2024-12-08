package components.admin;

import components.shared.utils.Response;
import java.util.List;

public class AdminUserBUS {
    private final AdminUserDAO userDAO;

    public AdminUserBUS() {
        userDAO = new AdminUserDAO();
    }

    public boolean isUsernameExist(String username) {
        return userDAO.checkUsernameExists(username);
    }

    /**
     * Kiểm tra thông tin đăng nhập (username + password) hợp lệ.
     */
    public Response login(String username, String password) {
        // Kiểm tra xem username có tồn tại hay không
        if (!isUsernameExist(username)) {
            return new Response(false, "Username does not exist.");
        }

        // Kiểm tra mật khẩu
        Response response = userDAO.checkPassword(username, password);

        if (response.isSuccess()) {
            // Kiểm tra xem có phải là admin hay không
            if (userDAO.isAdmin(username)) {
                return new Response(true, "Login successful as admin.");
            } else {
                return new Response(false, "You are not authorized as an admin.");
            }
        }

        return response;
    }

    public List<AdminUserDTO> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<AdminUserDTO> getUsers(String searchValue, String filterColumn) {
        return userDAO.reloadUserData(searchValue, filterColumn);
    }

    public List<AdminUserDTO> getNewUsers(String sortBy, String time, String username){
        return userDAO.loadNewUserData(sortBy, time, username);
    }

    public boolean updateUser(AdminUserDTO user) {
        // Business logic, e.g., validation, can be added here
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        return userDAO.updateUser(user);
    }

    public int[] getUserCountsByMonth(int year) {
        return userDAO.fetchUserCountsByMonth(year);
    }    

    public boolean deleteUser(int userId) {
        // Add additional checks if needed
        return userDAO.deleteUser(userId);
    }

    public String getUserAccess(int userId) {
        return userDAO.getUserAccess(userId);
    }

    public boolean lockOrUnlockUser(int userId) {
        String currentAccess = userDAO.getUserAccess(userId);
        if (currentAccess == null) {
            return false; // User not found
        }

        String newAccess = currentAccess.equals("Yes") ? "No" : "Yes";
        return userDAO.updateUserAccess(userId, newAccess);
    }

    public boolean updatePassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return false; // Logic kiểm tra nghiệp vụ
        }

        // Gọi DAO để cập nhật
        return userDAO.updatePassword(userId, newPassword);
    }
}
