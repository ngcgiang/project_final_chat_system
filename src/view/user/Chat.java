package view.user;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.Highlighter.HighlightPainter;

import components.shared.utils.Utilities;

public class Chat extends JPanel {
    private JTextArea txtChat;
    private JTextField txtInput;
    private JButton btnSend, btnClearHistory, btnDeleteSelected, btnSearchChat;
    private ArrayList<String> chatHistory;

    // Các thành phần quản lý nhóm
    private JTextField txtGroupName;
    private JButton btnRenameGroup, btnAddMember, btnAssignAdmin, btnRemoveMember;
    private String currentGroupName = "Chat Group"; // Tên nhóm ban đầu

    public Chat() {
        chatHistory = new ArrayList<>();
        setLayout(new BorderLayout());

        // Thiết lập phông chữ cho toàn bộ chat
        Font chatFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Khu vực hiển thị tin nhắn
        txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);
        txtChat.setFont(chatFont);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        add(scrollPane, BorderLayout.CENTER);

        // Khu vực nhập tin nhắn và nút gửi
        JPanel inputPanel = new JPanel(new BorderLayout());
        txtInput = new JTextField();
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Phông chữ cho ô nhập tin nhắn
        btnSend = Utilities.createButton("Send");
        btnClearHistory = Utilities.createButton("Clear History");
        btnDeleteSelected = Utilities.createButton("Delete Selected Messages");
        btnSearchChat = Utilities.createButton("Search in Chat");

        inputPanel.add(txtInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.add(btnClearHistory);
        optionsPanel.add(btnDeleteSelected);
        optionsPanel.add(btnSearchChat);
        add(optionsPanel, BorderLayout.NORTH);

        // Thiết lập phông chữ cho các nút
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnSend.setFont(buttonFont);
        btnClearHistory.setFont(buttonFont);
        btnDeleteSelected.setFont(buttonFont);
        btnSearchChat.setFont(buttonFont);

        // Thêm khung quản lý nhóm ở bên phải
        add(createGroupManagementPanel(), BorderLayout.EAST);

        // Hành động gửi tin nhắn
        btnSend.addActionListener(e -> sendMessage());
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        // Xoá toàn bộ lịch sử chat
        btnClearHistory.addActionListener(e -> clearChatHistory());

        // Xoá các dòng đã chọn trong lịch sử
        btnDeleteSelected.addActionListener(e -> deleteSelectedMessages());

        // Tìm kiếm chuỗi trong lịch sử chat với một người
        btnSearchChat.addActionListener(e -> searchInChat());
    }

    private JPanel createGroupManagementPanel() {
        JPanel groupPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        groupPanel.setBorder(BorderFactory.createTitledBorder("Group Management"));

        // Đổi tên nhóm
        txtGroupName = new JTextField(currentGroupName); // Tên nhóm ban đầu
        txtGroupName.setVisible(false);
        btnRenameGroup = Utilities.createButton("Rename Group");
        btnRenameGroup.addActionListener(e -> renameGroup());

        // Thêm thành viên
        btnAddMember = Utilities.createButton("Add Member");
        btnAddMember.addActionListener(e -> addMember());

        // Gán quyền admin
        btnAssignAdmin = Utilities.createButton("Assign Admin");
        btnAssignAdmin.addActionListener(e -> assignAdmin());

        // Xoá thành viên (chỉ dành cho admin)
        btnRemoveMember = Utilities.createButton("Remove Member");
        btnRemoveMember.addActionListener(e -> removeMember());

        // Thêm các thành phần vào panel quản lý nhóm
        groupPanel.add(btnRenameGroup);
        groupPanel.add(txtGroupName);
        groupPanel.add(btnAddMember);
        groupPanel.add(btnAssignAdmin);
        groupPanel.add(btnRemoveMember);

        return groupPanel;
    }

    private void renameGroup() {
        // Hiển thị ô nhập tên nhóm khi nhấn "Rename Group"
        txtGroupName.setVisible(true);
        // Mở một cửa sổ yêu cầu người dùng nhập tên nhóm mới
        int option = JOptionPane.showConfirmDialog(this, txtGroupName, "Enter new group name",
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newName = txtGroupName.getText().trim();
            if (!newName.isEmpty()) {
                currentGroupName = newName; // Cập nhật tên nhóm
                txtGroupName.setVisible(false); // Ẩn lại ô nhập tên nhóm
                setGroupName(currentGroupName); // Cập nhật tên nhóm trên cửa sổ
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid group name.");
            }
        }
    }

    private void setGroupName(String groupName) {
        // Cập nhật tên nhóm trên thanh tiêu đề của cửa sổ
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null && window instanceof JFrame) {
            ((JFrame) window).setTitle(groupName); // Cập nhật tên cửa sổ
        }
    }

    private void addMember() {
        String memberName = JOptionPane.showInputDialog(this, "Enter username to add:");
        if (memberName != null && !memberName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Added " + memberName + " to the group.");
        }
    }

    private void assignAdmin() {
        String memberName = JOptionPane.showInputDialog(this, "Enter username to assign as admin:");
        if (memberName != null && !memberName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, memberName + " is now an admin.");
        }
    }

    private void removeMember() {
        String memberName = JOptionPane.showInputDialog(this, "Enter username to remove:");
        if (memberName != null && !memberName.trim().isEmpty()) {
            // Kiểm tra quyền admin trước khi xóa thành viên
            JOptionPane.showMessageDialog(this, "Removed " + memberName + " from the group.");
        }
    }

    private void sendMessage() {
        String message = txtInput.getText();
        if (!message.trim().isEmpty()) {
            String timeStamp = new SimpleDateFormat("HH:mm").format(new Date());
            String displayMessage = "Me: " + message + "     [" + timeStamp + "]";
            txtChat.append(displayMessage + "\n");
            chatHistory.add(displayMessage); // Lưu vào lịch sử chat
            txtInput.setText("");
            txtChat.setCaretPosition(txtChat.getDocument().getLength());
        }
    }

    private void clearChatHistory() {
        txtChat.setText("");
        chatHistory.clear();
        JOptionPane.showMessageDialog(this, "Chat history cleared.");
    }

    private void deleteSelectedMessages() {
        String selectedText = txtChat.getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            txtChat.setText(txtChat.getText().replace(selectedText, ""));
            chatHistory.removeIf(line -> line.contains(selectedText));
            JOptionPane.showMessageDialog(this, "Selected messages deleted.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select text to delete.");
        }
    }

    private void searchInChat() {
        String searchQuery = JOptionPane.showInputDialog(this, "Enter text to search:");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            Highlighter highlighter = txtChat.getHighlighter();
            highlighter.removeAllHighlights();
            HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

            String chatContent = txtChat.getText();
            int index = chatContent.indexOf(searchQuery);
            while (index >= 0) {
                try {
                    highlighter.addHighlight(index, index + searchQuery.length(), painter);
                    index = chatContent.indexOf(searchQuery, index + searchQuery.length());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
