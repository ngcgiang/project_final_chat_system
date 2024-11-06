import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserManagement extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;

    public UserManagement() {
        
        // Container
        setTitle("User Management - Chat system");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main Table Panel for User Information
        String[] columnNames = {"ID", "Username", "Full-name", "Address", "DOB", "Gender", "Email", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        filterComboBox = new JComboBox<>(new String[]{"Username", "Full-name", "Status"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JButton searchButton = new JButton("Apply");
        filterPanel.add(searchButton);
        
        // Add filterPanel below headerPanel
        add(filterPanel, BorderLayout.NORTH);

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Remove");
        JButton lockButton = new JButton("Lock/Unlock");
        JButton resetPasswordButton = new JButton("Update password");
        JButton viewLoginHistoryButton = new JButton("Login history");
        JButton viewFriendsButton = new JButton("Friends list");

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(viewLoginHistoryButton);
        buttonPanel.add(viewFriendsButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Display the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserManagement());
    }
}
