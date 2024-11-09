package user;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public FriendList() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Tạo thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = Utilities.createLabel("Search Friend:", "plain", 14);
        txtSearch = new JTextField(20);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        // Tạo nút "Friend Request" và thêm nó vào góc phải
        JButton btnFriendRequest = Utilities.createButton("Friend Request");
        searchPanel.add(btnFriendRequest);

        // Tạo bảng hiển thị danh sách bạn bè
        String[] columns = { "Name", "Status", "Action" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 2)
                    return new ActionRenderer();
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2)
                    return new ActionEditor();
                return super.getCellEditor(row, column);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        table.setRowHeight(35); // Tăng chiều cao của các dòng
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Tăng chiều rộng của cột "Action" lên 200

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
            // Tìm JFrame chứa "Friend List" và yêu cầu chuyển đổi sang giao diện
            // "Friend Request"
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
    }

    private void filterTable() {
        String text = txtSearch.getText();
        if (text.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    private void addSampleData() {
        // Dữ liệu mẫu gồm tên và trạng thái
        Object[][] data = {
                { "Alice", "Online" }, { "Bob", "Offline" },
                { "Charlie", "Online" }, { "David", "Offline" },
                { "Eve", "Online" }, { "Frank", "Offline" },
                { "Grace", "Online" }, { "Hank", "Offline" },
                { "Ivy", "Online" }, { "Jack", "Offline" },
                { "Kara", "Online" }, { "Liam", "Offline" },
                { "Mona", "Online" }, { "Nate", "Offline" },
                { "Olivia", "Online" }, { "Paul", "Offline" },
                { "Quinn", "Online" }, { "Rose", "Offline" },
                { "Steve", "Online" }, { "Tina", "Offline" }
        };

        for (Object[] row : data) {
            tableModel.addRow(new Object[] { row[0], row[1], "Action" });
        }
    }

    // Renderer cho các nút trong cột "Action"
    private class ActionRenderer extends JPanel implements TableCellRenderer {
        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton btnAdd = Utilities.createButton("Add Friend", 100, 30);
            JButton btnUnfriend = Utilities.createButton("Unfriend", 100, 30);
            JButton btnBlock = Utilities.createButton("Block", 100, 30);
            add(btnAdd);
            add(btnUnfriend);
            add(btnBlock);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    // Editor cho các nút trong cột "Action"
    private class ActionEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private JButton btnAdd;
        private JButton btnUnfriend;
        private JButton btnBlock;
        private int editingRow;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnAdd = Utilities.createButton("Add Friend", 100, 30);
            btnUnfriend = Utilities.createButton("Unfriend", 100, 30);
            btnBlock = Utilities.createButton("Block", 100, 30);

            btnAdd.addActionListener(this);
            btnUnfriend.addActionListener(this);
            btnBlock.addActionListener(this);

            panel.add(btnAdd);
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
            if (e.getSource() == btnAdd) {
                JOptionPane.showMessageDialog(panel, "Friend request sent to " + name);
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
            fireEditingStopped();
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
