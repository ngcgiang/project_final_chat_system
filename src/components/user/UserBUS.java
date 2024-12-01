package components.user;

import components.shared.utils.*;

public class UserBUS {
    private UserDAO userDAO;

    public UserBUS() {
        this.userDAO = new UserDAO();
    }

    // Thêm nhà cung cấp vào cơ sở dữ liệu
    public ResponseDTO register(String username, String password, String confirmPassword) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        boolean usernameExists = userDAO.checkUsernameExists(username);

        if (usernameExists) {
            return new ResponseDTO(false, "Username already exists. Please choose another one!");
        }

        if (username.isEmpty()) {
            return new ResponseDTO(false, "Please enter username!");
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return new ResponseDTO(false, "Please enter password!");
        }

        if (!password.equals(confirmPassword)) {
            return new ResponseDTO(false, "Passwords do not match. Please try again!");
        }

        UserDTO newUser = new UserDTO(username, password);
        ResponseDTO result = userDAO.addUser(newUser);

        return result;
    }

    // Thêm nhà cung cấp vào cơ sở dữ liệu
    public ResponseDTO login(String username, String password) {
        if (username.isEmpty()) {
            return new ResponseDTO(false, "Please enter username!");
        }

        if (password.isEmpty()) {
            return new ResponseDTO(false, "Please enter password!");
        }

        boolean usernameExists = userDAO.checkUsernameExists(username);
        if (!usernameExists) {
            return new ResponseDTO(false, "Username is not valid!");
        }

        ResponseDTO result = userDAO.checkPassword(username, password);

        return result;
    }
}
