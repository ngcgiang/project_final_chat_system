package components.admin;

import java.util.List;

public class AdminUserBUS {
    private final AdminUserDAO userDAO;

    public AdminUserBUS() {
        userDAO = new AdminUserDAO();
    }

    public List<AdminUserDTO> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(AdminUserDTO user) {
        // Business logic, e.g., validation, can be added here
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be empty");
        }
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {
        // Add additional checks if needed
        return userDAO.deleteUser(userId);
    }
}
