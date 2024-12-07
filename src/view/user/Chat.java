package view.user;

import components.conversation.ConversationBUS;
import components.message.*;
import components.shared.utils.CurrentUser;
import components.shared.utils.Utilities;
import components.user.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.Highlighter.HighlightPainter;

public class Chat extends JPanel {
    private JPanel panel;
    private JTextArea txtChat;
    private JTextField txtInput;
    private JButton btnSend, btnClearHistory, btnDeleteSelected, btnSearchChat, btnBack;
    private ArrayList<MessageDTO> chatHistory;

    // Các thành phần quản lý nhóm
    private JTextField txtGroupName;
    private JButton btnRenameGroup, btnAddMember, btnAssignAdmin, btnRemoveMember;
    private String currentGroupName = "Chat Group"; // Tên nhóm ban đầu

    public Chat(String receiver, String accessFrom) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Thiết lập phông chữ cho toàn bộ chat
        Font chatFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Khu vực hiển thị tin nhắn
        txtChat = new JTextArea();
        txtChat.setEditable(false);
        txtChat.setLineWrap(true);
        txtChat.setWrapStyleWord(true);
        txtChat.setFont(chatFont);
        JScrollPane scrollPane = new JScrollPane(txtChat);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load messages
        MessageBUS messageBUS = new MessageBUS();
        chatHistory = messageBUS.getMessages(CurrentUser.getInstance().getUsername(), receiver);
        loadMessages(chatHistory, receiver);

        // Khu vực nhập tin nhắn và nút gửi
        JPanel inputPanel = new JPanel(new BorderLayout());
        txtInput = new JTextField();
        txtInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSend = Utilities.createButton("Send");
        txtInput.setPreferredSize(new Dimension(400, 30)); // Kích thước ô nhập tin nhắn
        btnSend.setPreferredSize(new Dimension(140, 30)); // Kích thước nút "Send"

        inputPanel.add(txtInput, BorderLayout.CENTER);
        inputPanel.add(btnSend, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        btnClearHistory = Utilities.createButton("Clear History");
        btnDeleteSelected = Utilities.createButton("Delete Selected Messages");
        btnSearchChat = Utilities.createButton("Search in Chat");
        JPanel optionsPanel = new JPanel();
        optionsPanel.add(btnClearHistory);
        optionsPanel.add(btnDeleteSelected);
        optionsPanel.add(btnSearchChat);
        panel.add(optionsPanel, BorderLayout.NORTH);

        // Thiết lập phông chữ cho các nút
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnSend.setFont(buttonFont);
        btnClearHistory.setFont(buttonFont);
        btnDeleteSelected.setFont(buttonFont);
        btnSearchChat.setFont(buttonFont);

        // Thêm khung quản lý nhóm ở bên phải
        panel.add(createGroupManagementPanel(), BorderLayout.EAST);

        // Hành động gửi tin nhắn
        btnSend.addActionListener(e -> {
            try {
                sendMessage(receiver);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });
        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMessage(receiver);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        // Xoá toàn bộ lịch sử chat
        btnClearHistory.addActionListener(e -> clearChatHistory());

        // Xoá các dòng đã chọn trong lịch sử
        // btnDeleteSelected.addActionListener(e -> deleteSelectedMessages());

        // Tìm kiếm chuỗi trong lịch sử chat với một người
        btnSearchChat.addActionListener(e -> searchInChat());

        btnBack.addActionListener(e -> {
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel(accessFrom);
            }
        });
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

        btnBack = Utilities.createButton("Back");
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(btnBack);

        // Thêm các thành phần vào panel quản lý nhóm
        groupPanel.add(btnRenameGroup);
        groupPanel.add(txtGroupName);
        groupPanel.add(btnAddMember);
        groupPanel.add(btnAssignAdmin);
        groupPanel.add(btnRemoveMember);
        groupPanel.add(backPanel, BorderLayout.SOUTH);

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

    private void loadMessages(ArrayList<MessageDTO> chatHistory, String receiver) {
        UserBUS userBUS = new UserBUS();
        UserDTO user1 = userBUS.getAccountInfo(CurrentUser.getInstance().getUsername());
        UserDTO user2 = userBUS.getAccountInfo(receiver);
        for (int i = 0; i < chatHistory.size(); i++) {
            String displayText = "";
            displayText += chatHistory.get(i).getSenderID() == user1.getID() ? user1.getFullName()
                    : user2.getFullName();
            displayText += ": " + chatHistory.get(i).getContent();
            displayText += "     [" + chatHistory.get(i).getSentAt() + "]";
            txtChat.append(displayText + '\n');
        }
    }

    private void sendMessage(String receiver) throws ParseException {
        String content = txtInput.getText();
        if (!content.trim().isEmpty()) {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

            UserBUS userBUS = new UserBUS();
            UserDTO user1 = userBUS.getAccountInfo(CurrentUser.getInstance().getUsername());
            UserDTO user2 = userBUS.getAccountInfo(receiver);

            String displayMessage = user1.getFullName() + ": " + content + "     [" + timeStamp + "]";
            txtChat.append(displayMessage + "\n");

            Timestamp sendAt = new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timeStamp).getTime());
            MessageDTO message = new MessageDTO(0, user1.getID(), user2.getID(), content, sendAt);

            chatHistory.add(message);
            txtInput.setText("");
            txtChat.setCaretPosition(txtChat.getDocument().getLength());

            String sender = CurrentUser.getInstance().getUsername();
            MessageBUS messageBUS = new MessageBUS();
            messageBUS.saveMessage(sender, receiver, content);

            ConversationBUS conversationBUS = new ConversationBUS();
            conversationBUS.addOrUpdateConversation(sender, receiver, content);
        }
    }

    private void clearChatHistory() {
        txtChat.setText("");
        chatHistory.clear();
        JOptionPane.showMessageDialog(this, "Chat history cleared.");
    }

    // private void deleteSelectedMessages() {
    // String selectedText = txtChat.getSelectedText();
    // if (selectedText != null && !selectedText.isEmpty()) {
    // txtChat.setText(txtChat.getText().replace(selectedText, ""));
    // chatHistory.removeIf(line -> line.contains(selectedText));
    // JOptionPane.showMessageDialog(this, "Selected messages deleted.");
    // } else {
    // JOptionPane.showMessageDialog(this, "Please select text to delete.");
    // }
    // }

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

    public JPanel getPanel() {
        return panel;
    }
}