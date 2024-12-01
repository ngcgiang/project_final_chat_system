package view.user;

import java.awt.*;
import javax.swing.*;

public class UserDashboard extends JFrame {
    private CardLayout cardLayout; // Khai báo CardLayout để quản lý các trang
    private JPanel mainPanel; // Panel chính chứa các trang

    public UserDashboard() {
        // Đặt tiêu đề cho cửa sổ
        setTitle("Login - User");

        // Đặt kích thước cho cửa sổ
        setSize(800, 600);

        // Đặt hành động khi đóng cửa sổ là thoát chương trình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Đặt vị trí cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);

        // Khởi tạo CardLayout và JPanel chính
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Tạo các giao diện khác
        Login loginGUI = new Login();
        Register registerGUI = new Register();
        Management managementGUI = new Management();
        UpdateAccountInfo updateAccountInfoGUI = new UpdateAccountInfo();
        ResetPassword resetPasswordGUI = new ResetPassword();
        UpdatePassword updatePasswordGUI = new UpdatePassword();
        FriendList friendListGUI = new FriendList();
        FriendRequest friendRequestGUI = new FriendRequest();
        Message messageGUI = new Message();
        FindUser findUserGUI = new FindUser();

        // Thêm các trang vào CardLayout
        mainPanel.add(loginGUI.getPanel(), "Login"); // Trang đăng nhập
        mainPanel.add(registerGUI.getPanel(), "Register"); // Trang đăng ký
        mainPanel.add(managementGUI.getPanel(), "Management"); // Trang quản lý tài khoản
        mainPanel.add(updateAccountInfoGUI.getPanel(), "Update Account Info"); // Trang cập nhật thông tin tài khoản
        mainPanel.add(resetPasswordGUI.getPanel(), "Reset Password"); // Trang reset mật khẩu
        mainPanel.add(updatePasswordGUI.getPanel(), "Update Password"); // Trang cập nhật mật khẩu
        mainPanel.add(friendListGUI.getPanel(), "Friend List"); // Trang danh sách bạn bè
        mainPanel.add(friendRequestGUI.getPanel(), "Friend Request"); // Trang danh sách yêu cầu kết bạn
        mainPanel.add(messageGUI.getPanel(), "Message");
        mainPanel.add(findUserGUI.getPanel(), "Find User");

        // Thêm mainPanel vào JFrame
        add(mainPanel);

        // Hiển thị cửa sổ JFrame
        setVisible(true);
    }

    public void switchPanel(String panelName) {
        switch (panelName) {
            case "Login":
                setTitle("Login - User");
                break;
            case "Register":
                setTitle("Register - User");
                break;
            case "Management":
                setTitle("Management - User");
                break;
            case "Update Account Info":
                setTitle("Update Account Info - User");
                break;
            case "Reset Password":
                setTitle("Reset Password - User");
                break;
            case "Update Password":
                setTitle("Update Password - User");
                break;
            case "Friend List":
                setTitle("Friend List - User");
                break;
            case "Friend Request":
                setTitle("Friend Request - User");
                break;
            case "Message":
                setTitle("Message - User");
                break;
            case "Find User":
                setTitle("Find User - User");
                break;
            default:
                setTitle("User Dashboard - User");
        }
        cardLayout.show(mainPanel, panelName);
    }

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard()); // Khởi tạo cửa sổ UserDashboard
    }
}
