import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SpamReportManagement extends JFrame {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JComboBox<String> timeFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton lockAccountButton;

    public SpamReportManagement() {
        // Main window setup
        setTitle("Spam Report Management - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JLabel titleLabel = new JLabel("Spam Report Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

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

        add(filterSortPanel, BorderLayout.NORTH);

        // Table for displaying spam reports
        tableModel = new DefaultTableModel(new Object[]{"Username", "Report Time", "Reason"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for locking accounts
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lockAccountButton = new JButton("Lock Account");
        buttonPanel.add(lockAccountButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Adding sample data to the table (optional)
        tableModel.addRow(new Object[]{"user123", "2024-11-08 10:30", "Spam messages"});
        tableModel.addRow(new Object[]{"user456", "2024-11-07 14:15", "Inappropriate content"});

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SpamReportManagement());
    }
}
