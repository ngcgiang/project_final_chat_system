package view.user;

import components.relationship.RelationshipBUS;
import components.shared.utils.CurrentUser;
import components.shared.utils.Utilities;
import components.user.UserBUS;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class FindUser extends JPanel {
    private JPanel panel;
    private JTextField txtSearch;
    private JTable table;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private DefaultTableModel tableModel;

    public FindUser() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Khu vực tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);

        searchPanel.add(txtSearch);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Khu vực hiển thị kết quả tìm kiếm
        String[] columnNames = { "Username", "Name", "Action" };
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2)
                    return new AddRenderer();
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2)
                    return new AddEditor();
                return super.getCellEditor(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 2) {
                    int modelRow = table.convertRowIndexToModel(row);
                    String status = (String) tableModel.getValueAt(modelRow, tableModel.getColumnCount() - 1);
                    return !"3".equals(status);
                }
                return false;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        table.setRowHeight(35);

        // Thêm dữ liệu mẫu vào bảng
        addSampleUser();

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

        // Thanh cuộn
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút "Back"
        JButton btnBack = Utilities.createButton("Back", 100, 30);
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backPanel.add(btnBack);
        panel.add(backPanel, BorderLayout.SOUTH);

        // Thiết lập hành động khi nhấn nút "Back"
        btnBack.addActionListener(e -> {
            // Tìm JFrame chứa "Find User" và yêu cầu chuyển đổi sang giao diện
            // "Management"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });
    }

    private void addSampleUser() {
        Object[][] users = new UserBUS().getUserList(CurrentUser.getInstance().getUsername());

        for (Object[] user : users) {
            tableModel.addRow(user);
        }
    }

    private void filterTable() {
        String text = txtSearch.getText();
        if (text.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.orFilter(Arrays.asList(
                    RowFilter.regexFilter("(?i)" + text, 0),
                    RowFilter.regexFilter("(?i)" + text, 1))));
        }
    }

    private class AddRenderer extends JPanel implements TableCellRenderer {
        private JButton btnAction;

        public AddRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnAction = Utilities.createButton("", 100, 30);
            add(btnAction);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object status, boolean isSelected,
                boolean hasFocus,
                int row, int column) {

            status = (String) table.getValueAt(row, 2);

            if ("3".equals(status)) {
                btnAction.setText("Friends");
                btnAction.setEnabled(false);
            } else {
                String btnText = "";
                switch ((String) status) {
                    case "2":
                        btnText = "Respond";
                        break;
                    case "1":
                        btnText = "Cancel";
                        break;
                    case "0":
                        btnText = "Add friend";
                        break;
                }
                btnAction.setText(btnText);
                btnAction.setEnabled(true);
            }

            return this; // Trả về panel chứa nút
        }
    }

    private class AddEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private JButton btnAction;
        private int modelRow;
        Object status;

        public AddEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnAction = Utilities.createButton("", 100, 30);

            btnAction.addActionListener(this);

            panel.add(btnAction);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            modelRow = table.convertRowIndexToModel(row);

            // Kiểm tra trạng thái của dòng này (cột cuối cùng)
            status = (String) tableModel.getValueAt(modelRow, 2);

            if ("3".equals(status)) {
                btnAction.setText("Friends");
                btnAction.setEnabled(false);
            } else {
                String btnText = "";
                switch ((String) status) {
                    case "2":
                        btnText = "Respond";
                        break;
                    case "1":
                        btnText = "Cancel";
                        break;
                    case "0":
                        btnText = "Add friend";
                        break;
                }
                btnAction.setText(btnText);
                btnAction.setEnabled(true);
            }
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = (String) tableModel.getValueAt(modelRow, 1); // Lấy tên từ cột 1
            if (e.getSource() == btnAction) {
                String username2 = (String) tableModel.getValueAt(modelRow, 0);
                String username1 = CurrentUser.getInstance().getUsername();
                RelationshipBUS relationshipBUS = new RelationshipBUS();

                String notification = "";
                boolean flag = true;

                // Respond
                if ("2".equals(status)) {
                    String[] options = { "Accept", "Reject", "Cancel" };
                    int confirm = JOptionPane.showOptionDialog(
                            panel,
                            "Do you want to \"Accept\" or \"Reject\" the friend request?",
                            "Friend Request",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]);

                    switch (confirm) {
                        case 0 -> {
                            // "Accept"
                            relationshipBUS.acceptFriendRequest(username1, username2);
                            status = "3";
                            notification = "Accept " + name + "'s friend request";
                        }
                        case 1 -> {
                            // "Reject"
                            relationshipBUS.rejectOrCancelFriendRequest(username2, username1);
                            status = "0";
                            notification = "Reject " + name + "'s friend request";
                        }
                        default -> flag = false;
                    }
                }

                // Cancel
                else if ("1".equals(status)) {
                    boolean success = relationshipBUS.rejectOrCancelFriendRequest(username1, username2);
                    if (success) {
                        status = "0";
                        notification = "Cancel friend request to " + name;
                    } else {
                        notification = "Cancel request failed";
                    }
                }

                // Add friend
                else if ("0".equals(status)) {
                    boolean success = relationshipBUS.addFriend(username1, username2);
                    if (success) {
                        status = "1";
                        notification = "Send friend request to " + name;
                    } else {
                        notification = "Send request failed";
                    }
                }

                notification += "!";
                if (flag)
                    JOptionPane.showMessageDialog(panel, notification);
            }
        }

        @Override
        public Object getCellEditorValue() {
            System.out.println(status);
            return status;
        }
    }

    public JPanel getPanel() {
        return panel;
    }
}
