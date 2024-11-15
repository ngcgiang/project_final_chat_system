import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserFriendListManagement extends JFrame {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JTextField amountOfFriendsFilterField;
    private JComboBox<String> amountFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;

    public UserFriendListManagement() {
        // Main window setup
        setTitle("User Friend List Management - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        // Set FlowLayout to left-align the title
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Friends List Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Date Creation", "Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by amount of friends
        amountFilterComboBox = new JComboBox<>(new String[]{"Greater than", "Equal to", "Less than"});
        filterSortPanel.add(new JLabel("Amount of friends: "));
        filterSortPanel.add(amountFilterComboBox);
        
        // Box input for amount of friends
        amountOfFriendsFilterField = new JTextField(4);
        filterSortPanel.add(amountOfFriendsFilterField);

        // Filter by Username
        usernameFilterField = new JTextField(10);
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

        // Table for displaying user friend list
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Amount of friends", "Date of creation"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Adding sample data to the table (optional)
        tableModel.addRow(new Object[]{"US01", "user123", "123", "09/11/2024"});
        tableModel.addRow(new Object[]{"US02", "user456", "560", "10/11/2024"});

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserFriendListManagement());
    }
}
