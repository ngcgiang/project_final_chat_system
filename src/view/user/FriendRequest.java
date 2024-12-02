package view.user;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import components.shared.utils.Utilities;

public class FriendRequest {
    private JPanel panel;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public FriendRequest() {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Tạo thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);

        // Tạo bảng hiển thị danh sách yêu cầu kết bạn
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
        btnBack.addActionListener(e -> {
            // Tìm JFrame chứa "Friend Request" và yêu cầu chuyển đổi sang giao diện "Friend
            // List"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Friend List");
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
                { "Alice", "Pending" }, { "Bob", "Pending" },
                { "Charlie", "Pending" }, { "David", "Pending" },
                { "Eve", "Pending" }, { "Frank", "Pending" },
                { "Grace", "Pending" }, { "Hank", "Pending" },
                { "Ivy", "Pending" }, { "Jack", "Pending" },
                { "Kara", "Pending" }, { "Liam", "Pending" },
                { "Mona", "Pending" }, { "Nate", "Pending" },
                { "Olivia", "Pending" }, { "Paul", "Pending" },
                { "Quinn", "Pending" }, { "Rose", "Pending" },
                { "Steve", "Pending" }, { "Tina", "Pending" }
        };

        for (Object[] row : data) {
            tableModel.addRow(new Object[] { row[0], row[1], "Action" });
        }
    }

    // Renderer cho các nút trong cột "Action"
    private class ActionRenderer extends JPanel implements TableCellRenderer {
        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton btnAccept = Utilities.createButton("Accept", 100, 30);
            JButton btnReject = Utilities.createButton("Reject", 100, 30);
            add(btnAccept);
            add(btnReject);
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
        private JButton btnAccept;
        private JButton btnReject;
        private int editingRow;

        public ActionEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnAccept = Utilities.createButton("Accept", 100, 30);
            btnReject = Utilities.createButton("Reject", 100, 30);

            btnAccept.addActionListener(this);
            btnReject.addActionListener(this);

            panel.add(btnAccept);
            panel.add(btnReject);
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
            if (e.getSource() == btnAccept) {
                tableModel.removeRow(editingRow); // Xóa yêu cầu
                JOptionPane.showMessageDialog(panel, "Friend request from " + name + " accepted.");
            } else if (e.getSource() == btnReject) {
                int confirm = JOptionPane.showConfirmDialog(panel,
                        "Are you sure you want to reject the friend request from " + name + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(editingRow); // Xóa yêu cầu
                    JOptionPane.showMessageDialog(panel, name + " has been rejected.");
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
