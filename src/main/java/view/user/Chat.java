package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import components.shared.utils.CurrentUser;
import components.shared.utils.Utilities;
import components.user.UserBUS;
import components.user.UserDTO;
import components.user.conversation.ConversationBUS;
import components.user.message.MessageBUS;
import components.user.message.MessageDTO;

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

    // Bộ đếm thời gian
    private Timer timer;

    public Chat(String receiver, String accessFrom) {
        startPolling(receiver);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Thiết lập phông chữ cho toàn bộ chat
        Font chatFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Khu vực hiển thị tin nhắn
        txtChat = new JTextArea();
        txtChat.setEditable(true);
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

        btnBack = Utilities.createButton("Back");
        JPanel backPanel = new JPanel();
        backPanel.add(btnBack);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backPanel, BorderLayout.WEST);
        topPanel.add(optionsPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Thiết lập phông chữ cho các nút
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnSend.setFont(buttonFont);
        btnClearHistory.setFont(buttonFont);
        btnDeleteSelected.setFont(buttonFont);
        btnSearchChat.setFont(buttonFont);

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
        btnClearHistory.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to clear all messages");
            if (confirm == JOptionPane.YES_OPTION) {
                clearChatHistory(receiver);
            }
        });

        // Xoá các dòng đã chọn trong lịch sử
        btnDeleteSelected.addActionListener(e -> deleteSelectedMessages(receiver));

        // Tìm kiếm chuỗi trong lịch sử chat với một người
        btnSearchChat.addActionListener(e -> searchInChat());

        btnBack.addActionListener(e -> {
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel(accessFrom);
            }
        });
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDate = sdf.format(chatHistory.get(i).getSentAt());
            displayText += "     [" + formattedDate + "]";
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

    private void clearChatHistory(String receiver) {
        txtChat.setText("");
        chatHistory.clear();
        MessageBUS messageBUS = new MessageBUS();
        ConversationBUS conversationBUS = new ConversationBUS();
        messageBUS.deleteAllMessages(CurrentUser.getInstance().getUsername(), receiver);
        conversationBUS.deleteConversation(CurrentUser.getInstance().getUsername(), receiver);
        JOptionPane.showMessageDialog(this, "Chat history cleared!");
    }

    private void deleteSelectedMessages(String receiver) {
        // Lấy vị trí con trỏ trong văn bản
        int caretPosition = txtChat.getCaretPosition();

        String chatContent = txtChat.getText();

        // Tìm vị trí đầu và cuối của dòng chứa con trỏ
        int lineStart = chatContent.lastIndexOf("\n", caretPosition);
        if (lineStart == caretPosition) {
            caretPosition--;

        }
        lineStart = chatContent.lastIndexOf("\n", caretPosition) + 1;
        int lineEnd = chatContent.indexOf("\n", caretPosition);

        int count = 0;
        for (int i = 0; i < lineStart; i++) {
            if (chatContent.charAt(i) == '\n') {
                count++;
            }
        }

        String lineToDelete = chatContent.substring(lineStart, lineEnd);

        if (lineToDelete != null && !lineToDelete.isEmpty()) {
            String newText = chatContent.replace(lineToDelete + "\n", "");
            txtChat.setText(newText);
        }

        MessageBUS messageBUS = new MessageBUS();
        messageBUS.deleteMessage(chatHistory.get(count).getMessageID());
        chatHistory.remove(count);
        ConversationBUS conversationBUS = new ConversationBUS();
        if (chatHistory.isEmpty()) {
            conversationBUS.deleteConversation(CurrentUser.getInstance().getUsername(), receiver);
        } else {
            conversationBUS.addOrUpdateConversation(CurrentUser.getInstance().getUsername(), receiver,
                    chatHistory.get(chatHistory.size() - 1).getContent());
        }
    }

    private int currentSearchIndex = -1;
    private String lastSearchQuery = "";

    private void searchInChat() {
        // Nếu không có chuỗi tìm kiếm trước đó hoặc thay đổi chuỗi tìm kiếm
        String searchQuery = JOptionPane.showInputDialog(this, "Enter text to search:", lastSearchQuery);

        Highlighter highlighter = txtChat.getHighlighter();
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

        if (searchQuery == null || searchQuery.isEmpty()) {
            highlighter.removeAllHighlights();
            return;
        }

        String chatContent = txtChat.getText();
        String regex = "\\s\\[.*?\\]$";
        String[] lines = chatContent.split("\n"); // Tách từng dòng chat
        StringBuilder cleanedContent = new StringBuilder();

        for (String line : lines) {
            String cleanedLine = line.replaceAll(regex, "").trim();
            cleanedContent.append(cleanedLine).append("                       \n");
        }
        chatContent = cleanedContent.toString().trim();

        // Chuyển nội dung và chuỗi tìm kiếm về chữ thường
        String lowerCaseContent = chatContent.toLowerCase();
        String lowerCaseQuery = searchQuery.toLowerCase();

        // Nếu chuỗi tìm kiếm thay đổi, đặt lại chỉ số tìm kiếm
        if (!lowerCaseQuery.equals(lastSearchQuery.toLowerCase())) {
            currentSearchIndex = lowerCaseContent.length(); // Bắt đầu từ cuối
            highlighter.removeAllHighlights(); // Xóa tất cả các highlight cũ
            lastSearchQuery = searchQuery;
        }

        int index = lowerCaseContent.lastIndexOf(lowerCaseQuery, currentSearchIndex - 1);

        // Nếu không tìm thấy, quay lại từ cuối
        if (index < 0) {
            index = lowerCaseContent.lastIndexOf(lowerCaseQuery); // Tìm từ cuối
        }

        // Nếu tìm thấy, di chuyển đến đoạn khớp và highlight
        if (index >= 0) {
            try {
                currentSearchIndex = index;
                txtChat.setCaretPosition(index); // Đặt con trỏ tại vị trí tìm thấy
                highlighter.removeAllHighlights(); // Xóa highlight cũ
                highlighter.addHighlight(index, index + searchQuery.length(), painter); // Highlight đoạn khớp
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No matches found for: " + searchQuery);
            currentSearchIndex = lowerCaseContent.length(); // Đặt lại vị trí tìm kiếm
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void startPolling(String receiver) {
        MessageBUS messageBUS = new MessageBUS();
        timer = new Timer(2000, e -> checkForNewMessages(receiver, messageBUS)); // Polling mỗi 2 giây
        timer.start();
    }

    private void checkForNewMessages(String receiver, MessageBUS messageBUS) {
        ArrayList<MessageDTO> newMessages = messageBUS.getNewMessages(CurrentUser.getInstance().getUsername(),
                receiver);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (MessageDTO message : newMessages) {
            UserBUS userBUS = new UserBUS();
            if (message.getSenderID() == userBUS.getAccountInfo(CurrentUser.getInstance().getUsername()).getID())
                return;

            UserDTO currentUser = userBUS.getAccountInfo(CurrentUser.getInstance().getUsername());
            UserDTO anotherUser = userBUS.getAccountInfo(receiver);

            String senderName = message.getSenderID() == currentUser.getID() ? currentUser.getFullName()
                    : anotherUser.getFullName();

            // Format thời gian
            String formattedDate = sdf.format(message.getSentAt());

            String formattedMessage = senderName + ": " + message.getContent() + "     [" + formattedDate + "]";
            txtChat.append(formattedMessage + "\n");
            chatHistory.add(message);
        }
    }

}