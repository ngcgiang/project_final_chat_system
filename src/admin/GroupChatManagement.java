import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupChatManagement extends JFrame {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;

    public GroupChatManagement() {
        // Setting main window properties
        setTitle("Group chat management - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 70)); // Adjusted to fit filter panel

        // Left Panel for Header
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel chatLabel = new JLabel("Group chat management");
        chatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(chatLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Right Panel for Admin Info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        JLabel adminNameLabel = new JLabel("ngcjang");
        adminNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(adminNameLabel);

        // Admin Button with Icon
        ImageIcon adminLogoIcon = new ImageIcon(".\\img\\profile-user.png");
        Image scaledAdminLogo = adminLogoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JButton adminButton = new JButton(new ImageIcon(scaledAdminLogo));
        adminButton.setFocusPainted(false);
        adminButton.setContentAreaFilled(false);
        adminButton.setBorderPainted(false);
        adminButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Open settings"));
        rightPanel.add(adminButton);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        filterComboBox = new JComboBox<>(new String[]{"Group name", "Creation time"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JButton applyButton = new JButton("Apply");
        filterPanel.add(applyButton);
        
        // Add filter panel within the header panel
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table for Group Chat Information
        tableModel = new DefaultTableModel(new Object[]{"Name group", "Creation time", "Amount of members"}, 0);
        groupTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for testing (optional)
        tableModel.addRow(new Object[]{"Study Group", "2024-11-01", 10});
        tableModel.addRow(new Object[]{"Project Team", "2024-10-15", 5});

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewMember = new JButton("View member list");
        JButton viewAdmin = new JButton("View admin list");

        buttonPanel.add(viewMember);
        buttonPanel.add(viewAdmin);

        add(buttonPanel, BorderLayout.SOUTH);

        // Show window
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GroupChatManagement());
    }
}
