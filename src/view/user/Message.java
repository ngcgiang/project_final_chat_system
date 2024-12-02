package view.user;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import components.shared.utils.Utilities;

public class Message {
    private JPanel panel;
    private JList<Conversation> conversationList;
    private DefaultListModel<Conversation> listModel;

    public Message() {
        // Tạo panel chính
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Tạo thanh tìm kiếm
        JTextField txtSearch = new JTextField(20);
        JPanel headPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headPanel.add(txtSearch);
        panel.add(headPanel, BorderLayout.NORTH);

        // Tạo danh sách các cuộc hội thoại
        listModel = new DefaultListModel<>();
        conversationList = new JList<>(listModel);
        conversationList.setCellRenderer(new ConversationRenderer());
        conversationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Thêm dữ liệu mẫu
        addSampleConversations();

        // Thêm DocumentListener vào ô tìm kiếm
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterConversations(txtSearch.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterConversations(txtSearch.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterConversations(txtSearch.getText());
            }
        });

        // Sự kiện khi nhấp vào cuộc hội thoại
        conversationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Conversation selectedConversation = conversationList.getSelectedValue();
                if (selectedConversation != null) {
                    openChatWindow(selectedConversation.getFriendName());
                    conversationList.clearSelection();
                }
            }
        });

        // Cuộn danh sách
        JScrollPane scrollPane = new JScrollPane(conversationList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút "Back"
        JButton btnBack = Utilities.createButton("Back", 100, 30);
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(btnBack);
        panel.add(backPanel, BorderLayout.SOUTH);

        // Thiết lập hành động khi nhấn nút "Back"
        btnBack.addActionListener(e -> {
            // Tìm JFrame chứa "Message" và yêu cầu chuyển đổi sang giao diện
            // "Management"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });
    }

    // Phương thức lọc các cuộc hội thoại dựa trên từ khóa tìm kiếm
    private void filterConversations(String query) {
        DefaultListModel<Conversation> filteredModel = new DefaultListModel<>();

        for (int i = 0; i < listModel.getSize(); i++) {
            Conversation conversation = listModel.getElementAt(i);
            if (conversation.getFriendName().toLowerCase().contains(query.toLowerCase())
                    || conversation.getLastMessage().toLowerCase().contains(query.toLowerCase())) {
                filteredModel.addElement(conversation);
            }
        }

        conversationList.setModel(filteredModel);

        // Nếu không có từ khóa tìm kiếm, hiển thị lại tất cả cuộc hội thoại
        if (query.isEmpty()) {
            conversationList.setModel(listModel);
        }
    }

    // Phương thức để mở cửa sổ chat với bạn bè
    private void openChatWindow(String friendName) {
        JFrame chatFrame = new JFrame(friendName);
        chatFrame.setSize(600, 400);
        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatFrame.setLocationRelativeTo(null);

        Chat chatPanel = new Chat(); // Chat là giao diện chat của bạn
        chatFrame.add(chatPanel);

        chatFrame.setVisible(true);
    }

    // Thêm dữ liệu mẫu vào danh sách cuộc hội thoại
    private void addSampleConversations() {
        listModel.addElement(new Conversation("Alice", "Hello! How are you?", "10:45 AM"));
        listModel.addElement(new Conversation("Bob", "Let's meet up tomorrow.", "Yesterday"));
        listModel.addElement(new Conversation("Charlie", "Can you send me the files?", "10:10 AM"));
        listModel.addElement(new Conversation("David", "Good morning!", "2 days ago"));
        listModel.addElement(new Conversation("Eve", "Happy Birthday!", "3 days ago"));
    }

    // Getter cho panel chính
    public JPanel getPanel() {
        return panel;
    }

    // Class để lưu thông tin cuộc hội thoại
    private static class Conversation {
        private String friendName;
        private String lastMessage;
        private String time;

        public Conversation(String friendName, String lastMessage, String time) {
            this.friendName = friendName;
            this.lastMessage = lastMessage;
            this.time = time;
        }

        public String getFriendName() {
            return friendName;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public String getTime() {
            return time;
        }
    }

    // Renderer tùy chỉnh để hiển thị từng cuộc hội thoại trong danh sách
    private static class ConversationRenderer extends JPanel implements ListCellRenderer<Conversation> {
        private JLabel lblName;
        private JLabel lblLastMessage;
        private JLabel lblTime;

        public ConversationRenderer() {
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            lblName = new JLabel();
            lblName.setFont(new Font("Arial", Font.BOLD, 14));

            lblLastMessage = new JLabel();
            lblLastMessage.setForeground(Color.GRAY);

            lblTime = new JLabel();
            lblTime.setFont(new Font("Arial", Font.PLAIN, 10));
            lblTime.setForeground(Color.GRAY);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setOpaque(false);
            mainPanel.add(lblName, BorderLayout.NORTH);
            mainPanel.add(lblLastMessage, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            add(lblTime, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Conversation> list, Conversation conversation,
                int index, boolean isSelected, boolean cellHasFocus) {
            lblName.setText(conversation.getFriendName());
            lblLastMessage.setText(conversation.getLastMessage());
            lblTime.setText(conversation.getTime());

            // Hiển thị màu nền khi được chọn
            if (isSelected) {
                setBackground(new Color(220, 230, 240));
            } else {
                setBackground(Color.WHITE);
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Vẽ đường phân cách ở dưới cùng của mỗi ô
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
        }
    }
}
