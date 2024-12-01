package view.user;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class FriendList {
    private JPanel panel;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JRadioButton rbOnline, rbOffline;

    public FriendList() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Thanh header
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        // Thêm 2 stick box cho trạng thái
        rbOnline = new JRadioButton("Online");
        rbOffline = new JRadioButton("Offline");
        searchPanel.add(rbOnline);
        searchPanel.add(rbOffline);

        // Tạo nút "Friend Request"
        JButton btnFriendRequest = Utilities.createButton("Friend Request");
        searchPanel.add(btnFriendRequest);

        // Tạo nút "Create" và thêm nó vào góc phải
        JButton btnCreate = Utilities.createButton("Create", 100, 30);
        searchPanel.add(btnCreate);

        // Tạo bảng hiển thị danh sách bạn bè
        String[] columns = { "Name", "Status", "Chat", "Action" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2)
                    return new ChatRender();
                if (column == 3)
                    return new ActionRenderer();
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2)
                    return new ChatEditor();
                if (column == 3)
                    return new ActionEditor();
                return super.getCellEditor(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 || column == 3;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        table.setRowHeight(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(350);

        // Thêm dữ liệu mẫu vào bảng
        addSampleData();

        // Thêm tìm kiếm vào bảng
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });

        // Thiết lập hành động cho các stick box
        rbOnline.addActionListener(e -> filterByStatus());
        rbOffline.addActionListener(e -> filterByStatus());

        // Nút "Back"
        JButton btnBack = Utilities.createButton("Back", 100, 30);
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(btnBack);

        // Thêm các thành phần vào panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(backPanel, BorderLayout.SOUTH);

        // Thiết lập hành động khi nhấn nút "Friend Request"
        btnFriendRequest.addActionListener(e -> {
            // Tìm JFrame chứa "Friend List" và yêu cầu chuyển đổi sang giao diện "Friend
            // Request"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Friend Request");
            }
        });

        // Thiết lập hành động khi nhấn nút "Back"
        btnBack.addActionListener(e -> {
            // Tìm JFrame chứa "Friend List" và yêu cầu chuyển đổi sang giao diện
            // "Management"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });

        // Thiết lập hành động khi nhấn nút "Create"
        btnCreate.addActionListener(e -> {
            // Tạo cửa sổ cho việc tạo nhóm chat
            JFrame createGroupFrame = new JFrame("Create New Chat Group");
            createGroupFrame.setSize(600, 400);
            createGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            createGroupFrame.setLocationRelativeTo(null);

            // Tạo bảng hiển thị danh sách bạn bè
            DefaultTableModel createGroupTableModel = new DefaultTableModel(new Object[] { "Select", "Name", "Status" },
                    0);
            JTable createGroupTable = new JTable(createGroupTableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 0;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    if (columnIndex == 0) {
                        return Boolean.class; // Cột Select sẽ chứa checkbox
                    }
                    return super.getColumnClass(columnIndex);
                }
            };

            // Thêm dữ liệu vào bảng
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String name = (String) tableModel.getValueAt(i, 0); // Lấy tên từ cột "Name"
                String status = (String) tableModel.getValueAt(i, 1); // Lấy trạng thái từ cột "Status"
                createGroupTableModel.addRow(new Object[] { false, name, status });
            }

            // Thêm bảng vào của sổ
            JScrollPane scrollPane = new JScrollPane(createGroupTable);
            createGroupFrame.add(scrollPane, BorderLayout.CENTER);

            // Thêm nút "Create Group"
            JButton btnCreateGroup = Utilities.createButton("Create Group");
            btnCreateGroup.addActionListener(evt -> {
                // Lấy danh sách những người bạn đã chọn
                StringBuilder selectedFriends = new StringBuilder();
                for (int i = 0; i < createGroupTable.getRowCount(); i++) {
                    Boolean isSelected = (Boolean) createGroupTable.getValueAt(i, 0); // Cột Select chứa checkbox
                    if (isSelected) {
                        selectedFriends.append(createGroupTable.getValueAt(i, 1)).append(", "); // Cột Name chứa tên
                                                                                                // người bạn
                    }
                }

                // Kiểm tra xem có người bạn nào được chọn không
                if (selectedFriends.length() > 0) {
                    String friendName = selectedFriends.toString();
                    friendName = friendName.substring(0, friendName.length() - 2); // Loại bỏ dấu phẩy cuối cùng

                    // Tạo JFrame mới cho cửa sổ chat
                    JFrame chatFrame = new JFrame(friendName);
                    chatFrame.setSize(600, 400);
                    chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    chatFrame.setLocationRelativeTo(null);

                    // Thêm panel Chat vào JFrame mới
                    Chat chatPanel = new Chat();
                    chatFrame.add(chatPanel);

                    // Hiển thị cửa sổ chat
                    chatFrame.setVisible(true);

                    // Đóng cửa sổ "Create New Chat Group"
                    createGroupFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(createGroupFrame,
                            "Please select at least one friend to create a group.");
                }
            });

            // Thêm nút vào cửa sổ
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            bottomPanel.add(btnCreateGroup);
            createGroupFrame.add(bottomPanel, BorderLayout.SOUTH);

            // Hiển thị cửa sổ
            createGroupFrame.setVisible(true);
        });
    }

    private void filterTable() {
        String text = txtSearch.getText();
        if (text.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
        }
    }

    private void filterByStatus() {
        if (rbOnline.isSelected() && rbOffline.isSelected()) {
            rowSorter.setRowFilter(RowFilter.orFilter(Arrays.asList(
                    RowFilter.regexFilter("Online", 1),
                    RowFilter.regexFilter("Offline", 1))));
        } else if (rbOnline.isSelected()) {
            rowSorter.setRowFilter(RowFilter.regexFilter("Online", 1));
        } else if (rbOffline.isSelected()) {
            rowSorter.setRowFilter(RowFilter.regexFilter("Offline", 1));
        } else {
            rowSorter.setRowFilter(RowFilter.orFilter(Arrays.asList(
                    RowFilter.regexFilter("Online", 1),
                    RowFilter.regexFilter("Offline", 1))));
        }
    }

    private void addSampleData() {
        Object[][] data = {
                { "Alice", "Online" }, { "Bob", "Offline" }, { "Charlie", "Online" },
                { "David", "Offline" },
                { "Eve", "Online" }, { "Frank", "Offline" }, { "Grace", "Online" },
                { "Hank", "Offline" },
                { "Ivy", "Online" }, { "Jack", "Offline" }, { "Kara", "Online" },
                { "Liam", "Offline" },
                { "Mona", "Online" }, { "Nate", "Offline" }, { "Olivia", "Online" },
                { "Paul", "Offline" },
                { "Quinn", "Online" }, { "Rose", "Offline" }, { "Steve", "Online" },
                { "Tina", "Offline" }
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private class ChatRender extends JPanel implements TableCellRenderer {
        public ChatRender() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton btnChat = Utilities.createButton("Chat", 100, 30);
            add(btnChat);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    private class ChatEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private JButton btnChat;
        private int editingRow;

        public ChatEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnChat = Utilities.createButton("Chat", 100, 30);

            btnChat.addActionListener(this);

            panel.add(btnChat);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            editingRow = row;
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String friendName = (String) tableModel.getValueAt(editingRow, 0); // Get the friend's name

            // Create a new JFrame for the chat window
            JFrame chatFrame = new JFrame(friendName);
            chatFrame.setSize(600, 400);
            chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chatFrame.setLocationRelativeTo(null);

            // Add the Chat panel to the new frame
            Chat chatPanel = new Chat();
            chatFrame.add(chatPanel);

            // Show the new chat window
            chatFrame.setVisible(true);

            // Stop editing the cell
            fireEditingStopped();
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    private class ActionRenderer extends JPanel implements TableCellRenderer {
        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton btnReport = Utilities.createButton("Report", 100, 30);
            JButton btnUnfriend = Utilities.createButton("Unfriend", 100, 30);
            JButton btnBlock = Utilities.createButton("Block", 100, 30);
            add(btnReport);
            add(btnUnfriend);
            add(btnBlock);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    private class ActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private JButton btnReport, btnUnfriend, btnBlock;
        private int editingRow;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnReport = Utilities.createButton("Report", 100, 30);
            btnUnfriend = Utilities.createButton("Unfriend", 100, 30);
            btnBlock = Utilities.createButton("Block", 100, 30);

            btnReport.addActionListener(this);
            btnUnfriend.addActionListener(this);
            btnBlock.addActionListener(this);

            panel.add(btnReport);
            panel.add(btnUnfriend);
            panel.add(btnBlock);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            editingRow = row;
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = (String) tableModel.getValueAt(editingRow, 0);
            if (e.getSource() == btnReport) {
                JOptionPane.showMessageDialog(panel, "Reported " + name);
            } else if (e.getSource() == btnUnfriend) {
                int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to unfriend " + name + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(editingRow);
                    JOptionPane.showMessageDialog(panel, name + " has been unfriended.");
                }
            } else if (e.getSource() == btnBlock) {
                int confirm = JOptionPane.showConfirmDialog(panel,
                        "Are you sure you want to block and unfriend " + name + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(editingRow);
                    JOptionPane.showMessageDialog(panel, name + " has been blocked and unfriended.");
                }
            }
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}