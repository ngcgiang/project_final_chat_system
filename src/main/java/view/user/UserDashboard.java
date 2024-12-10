package view.user;

import components.group.GroupDTO;
import components.shared.utils.CurrentUser;
import components.user.UserBUS;
import java.awt.*;
import java.awt.event.*;
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

        // Thêm lắng nghe sự kiện đóng cửa sổ
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Gọi logout trước khi thoát
                String username = CurrentUser.getInstance().getUsername();

                UserBUS userBUS = new UserBUS();
                userBUS.logout(username);
            }
        });

        // Đặt vị trí cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);

        // Khởi tạo CardLayout và JPanel chính
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Tạo các giao diện khác
        Login loginGUI = new Login();
        Register registerGUI = new Register();

        // Thêm các trang vào CardLayout
        mainPanel.add(loginGUI.getPanel(), "Login"); // Trang đăng nhập
        mainPanel.add(registerGUI.getPanel(), "Register"); // Trang đăng ký

        // Thêm mainPanel vào JFrame
        add(mainPanel);

        // Hiển thị cửa sổ JFrame
        setVisible(true);
    }

    public void switchPanel(String accessFrom, GroupDTO groupDTO) {
        GroupChat groupChatGUI = new GroupChat(accessFrom, groupDTO);
        mainPanel.add(groupChatGUI.getPanel(), groupDTO.getGroupName());
        setTitle(groupDTO.getGroupName());
        cardLayout.show(mainPanel, groupDTO.getGroupName());
    }

    public void switchPanel(String accessFrom, String username, String fullName) {
        Chat chatGUI = new Chat(username, accessFrom);
        mainPanel.add(chatGUI.getPanel(), fullName);
        setTitle(fullName);
        cardLayout.show(mainPanel, fullName);
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
                Management managementGUI = new Management();
                mainPanel.add(managementGUI.getPanel(), "Management"); // Trang quản lý tài khoản
                setTitle("Management - User");
                break;
            case "Update Account Info":
                UpdateAccountInfo updateAccountInfoGUI = new UpdateAccountInfo();
                mainPanel.add(updateAccountInfoGUI.getPanel(), "Update Account Info"); // Trang cập nhật thông tin tài
                                                                                       // khoản
                setTitle("Update Account Info - User");
                break;
            case "Reset Password":
                ResetPassword resetPasswordGUI = new ResetPassword();
                mainPanel.add(resetPasswordGUI.getPanel(), "Reset Password"); // Trang reset mật khẩu
                setTitle("Reset Password - User");
                break;
            case "Update Password":
                UpdatePassword updatePasswordGUI = new UpdatePassword();
                mainPanel.add(updatePasswordGUI.getPanel(), "Update Password"); // Trang cập nhật mật khẩu
                setTitle("Update Password - User");
                break;
            case "Friend List":
                FriendList friendListGUI = new FriendList();
                mainPanel.add(friendListGUI.getPanel(), "Friend List"); // Trang danh sách bạn bè
                setTitle("Friend List - User");
                break;
            case "Friend Request":
                FriendRequest friendRequestGUI = new FriendRequest();
                mainPanel.add(friendRequestGUI.getPanel(), "Friend Request"); // Trang danh sách yêu cầu kết bạn
                setTitle("Friend Request - User");
                break;
            case "Message":
                Message messageGUI = new Message();
                mainPanel.add(messageGUI.getPanel(), "Message");
                setTitle("Message - User");
                break;
            case "Find User":
                FindUser findUserGUI = new FindUser();
                mainPanel.add(findUserGUI.getPanel(), "Find User");
                setTitle("Find User - User");
                break;
        }
        cardLayout.show(mainPanel, panelName);
    }

    // Phương thức main để chạy chương trình
    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> new UserDashboard()); // Khởi tạo cửa sổ UserDashboard
    // }
}
