package components.user;

import components.shared.utils.*;
import java.util.Date;

public class UserBUS {
    private UserDAO userDAO;

    public UserBUS() {
        this.userDAO = new UserDAO();
    }

    // Thêm nhà cung cấp vào cơ sở dữ liệu
    public Response register(String username, String password, String confirmPassword) {
        // Kiểm tra xem tên đăng nhập đã tồn tại chưa
        boolean usernameExists = userDAO.checkUsernameExists(username);

        if (usernameExists) {
            return new Response(false, "Username already exists. Please choose another one!");
        }

        if (username.isEmpty()) {
            return new Response(false, "Please enter username!");
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return new Response(false, "Please enter password!");
        }

        if (!password.equals(confirmPassword)) {
            return new Response(false, "Passwords do not match. Please try again!");
        }

        UserDTO newUser = new UserDTO(username, password);
        Response result = userDAO.addUser(newUser);

        return result;
    }

    // Thêm nhà cung cấp vào cơ sở dữ liệu
    public Response login(String username, String password) {
        if (username.isEmpty()) {
            return new Response(false, "Please enter username!");
        }

        if (password.isEmpty()) {
            return new Response(false, "Please enter password!");
        }

        boolean usernameExists = userDAO.checkUsernameExists(username);
        if (!usernameExists) {
            return new Response(false, "Username is not valid!");
        }

        Response result = userDAO.checkPassword(username, password);
        if (result.isSuccess()) {
            CurrentUser.getInstance().setUsername(username);
        }

        return result;
    }

    public UserDTO getAccountInfo(String username) {
        return userDAO.getOne(username);
    }

    public Response updateAccountInfo(String username, String fullName, String address, Date dob, String email,
            String phone, String gender) {

        UserDTO user = userDAO.getOne(username);
        // Gọi UserDAO để thực hiện cập nhật
        Response result = userDAO.updateOne(username, user.getPassword(), fullName, address, dob, email, phone,
                gender);

        if (result.isSuccess()) {
            return new Response(true, "Update account infomation successful!");
        }
        return new Response(false, result.getMessage());
    }

    public Response updatePassword(String username, String oldPassword, String newPassword,
            String confirmNewPassword) {
        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            return new Response(false, "Please enter password!");
        }

        UserDTO user = userDAO.getOne(username);
        if (!user.getPassword().equals(oldPassword)) {
            return new Response(false, "Wrong password!");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return new Response(false, "Passwords do not match. Please try again!");
        }

        userDAO.updateOne(username, newPassword, user.getFullName(), user.getAddress(), user.getDob(), user.getEmail(),
                user.getPhone(), user.getGender());

        return new Response(true, "Password updated!");
    }
}
