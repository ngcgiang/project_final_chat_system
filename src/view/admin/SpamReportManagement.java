import components.admin.login_history.LoginHistoryDTO;
import components.admin.spam_reports.*;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SpamReportManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JComboBox<String> timeFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton lockAccountButton;
    private JButton backButton;
    private SpamReportBUS spamReportBUS;

    public SpamReportManagement() {
        spamReportBUS = new SpamReportBUS();

        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Spam Report Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Sort by Time", "Sort by Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Time
        timeFilterComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Filter by Time:"));
        filterSortPanel.add(timeFilterComboBox);

        // Filter by Username
        usernameFilterField = new JTextField(15);
        filterSortPanel.add(new JLabel("Filter by Username:"));
        filterSortPanel.add(usernameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply Filter");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Table for displaying spam reports
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Reported by", "Report Time", "Reason", "Access"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for locking accounts
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        lockAccountButton = new JButton("Lock/Unlock Account");
        buttonPanel.add(backButton);
        buttonPanel.add(lockAccountButton);

        add(buttonPanel, BorderLayout.SOUTH);

        lockAccountButton.addActionListener(e -> {
            int selectedRow = reportTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to lock/unlock.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String currentAccess = tableModel.getValueAt(selectedRow, 5).toString();

            // Confirm before changes
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to " + (currentAccess.equals("Yes") ? "lock" : "unlock") + " this user?",
                "Confirm Lock/Unlock",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
                
            boolean success = spamReportBUS.toggleUserAccess(userId, currentAccess);
            if (success) {
                JOptionPane.showMessageDialog(this, "User access updated successfully.");
                applyFilters(); // Reload data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user access.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        applyFilterButton.addActionListener(e -> applyFilters());

        loadSpamReportData();
    }

    private void loadSpamReportData() {
        String usernameDef = null;
        String sortByDef = "Sort by Time";
        String timeFilter = "All";

        List<SpamReportDTO> reports = spamReportBUS.getSpamReports(sortByDef, timeFilter, usernameDef);
        tableModel.setRowCount(0);
        for (SpamReportDTO report : reports) {
            tableModel.addRow(new Object[]{
                report.getUserId(),
                report.getUsername(),
                report.getReportedBy(),
                report.getReportTime(),
                report.getReason(),
                report.getAccess()
            });
        }
    }

    private void applyFilters() {
        String username = usernameFilterField.getText();
        String sortBy = sortComboBox.getSelectedItem().toString();
        String timeFilter = timeFilterComboBox.getSelectedItem().toString();

        List<SpamReportDTO> reports = spamReportBUS.getSpamReports(sortBy, timeFilter, username);
        tableModel.setRowCount(0);
        for (SpamReportDTO report : reports) {
            tableModel.addRow(new Object[]{
                report.getUserId(),
                report.getUsername(),
                report.getReportedBy(),
                report.getReportTime(),
                report.getReason(),
                report.getAccess()
            });
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
