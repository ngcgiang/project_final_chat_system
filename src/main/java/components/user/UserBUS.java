package components.user;

import components.shared.utils.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class UserBUS {
    private UserDAO userDAO;

    public UserBUS() {
        this.userDAO = new UserDAO();
    }

    public String getUsernameByID(int userId) {
        return userDAO.getUsernameById(userId);
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

        if (userDAO.getOne(username).getStatus().equals("Online")) {
            return new Response(false, "Someone else is accessing the account!");
        }
        Response result = userDAO.checkPassword(username, password);
        if (result.isSuccess()) {
            CurrentUser.getInstance().setUsername(username);
            userDAO.setStatus(username, "Online");
        }

        return result;
    }

    public void logout(String username) {
        userDAO.setStatus(username, "Offline");
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

    public void resetPassword(String username, String newPassword) {
        UserDTO user = userDAO.getOne(username);
        userDAO.updateOne(username, newPassword, user.getFullName(), user.getAddress(), user.getDob(), user.getEmail(),
                user.getPhone(), user.getGender());
    }

    public Object[][] getFriendList(String username) {
        ArrayList<UserDTO> friendList = userDAO.getFriendList(username);

        Object[][] friendListObject = new Object[friendList.size()][3];

        for (int i = 0; i < friendList.size(); i++) {
            UserDTO friend = friendList.get(i);
            friendListObject[i][0] = friend.getUsername();
            friendListObject[i][1] = friend.getFullName();
            friendListObject[i][2] = friend.getStatus();
        }

        return friendListObject;
    }

    public Object[][] getUserList(String username) {
        Map<UserDTO, String> userMap = userDAO.getUserList(username);

        Object[][] userListObject = new Object[userMap.size()][3];

        // Lặp qua từng entry trong userMap
        int i = 0;
        for (Map.Entry<UserDTO, String> entry : userMap.entrySet()) {
            UserDTO friend = entry.getKey();
            String status = entry.getValue();

            userListObject[i][0] = friend.getUsername();
            userListObject[i][1] = friend.getFullName();
            userListObject[i][2] = status;

            i++;
        }

        return userListObject;
    }

    public Object[][] getFriendRequestList(String username) {
        ArrayList<UserDTO> friendRequestList = userDAO.getFriendRequestList(username);

        Object[][] friendRequestListObject = new Object[friendRequestList.size()][2];

        for (int i = 0; i < friendRequestList.size(); i++) {
            UserDTO friend = friendRequestList.get(i);
            friendRequestListObject[i][0] = friend.getUsername();
            friendRequestListObject[i][1] = friend.getFullName();
        }

        return friendRequestListObject;
    }
}
