import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SpamReportManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JComboBox<String> timeFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton lockAccountButton;
    private JButton backButton;

    public SpamReportManagement() {
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
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Reported by", "Report Time", "Reason"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for locking accounts
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        lockAccountButton = new JButton("Lock Account");
        buttonPanel.add(backButton);
        buttonPanel.add(lockAccountButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Adding sample data to the table (optional)
        tableModel.addRow(new Object[]{"US01", "user123", "US02", "2024-11-08 10:30", "Spam messages"});
        tableModel.addRow(new Object[]{"US02", "user456", "US01", "2024-11-07 14:15", "Inappropriate content"});

    }

    public JButton getBackButton() {
        return backButton;
    }
}
