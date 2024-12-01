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

public class FindUser extends JPanel {
    private JPanel panel;
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

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
                return column == 2;
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
        Object[][] users = {
                { "john_doe", "John Doe" },
                { "jane_smith", "Jane Smith" },
                { "alex_taylor", "Alex Taylor" },
                { "emily_rose", "Emily Rose" },
                { "michael_brown", "Michael Brown" }
        };

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

    // Renderer và Editor cho nút "Add Friend" trong bảng
    private class AddRenderer extends JPanel implements TableCellRenderer {
        public AddRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
            JButton btnAdd = Utilities.createButton("Add Friend", 100, 30);
            add(btnAdd);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    private class AddEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel;
        private JButton btnAdd;
        private int editingRow;

        public AddEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnAdd = Utilities.createButton("Add Friend", 100, 30);

            btnAdd.addActionListener(this);

            panel.add(btnAdd);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            editingRow = row;
            return panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = (String) tableModel.getValueAt(editingRow, 1);
            if (e.getSource() == btnAdd) {
                JOptionPane.showMessageDialog(panel, "Send friend request to " + name);
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
