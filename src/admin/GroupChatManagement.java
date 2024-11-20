import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GroupChatManagement extends JPanel {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    private JButton backButton;
    private JFrame parentFrame;

    public GroupChatManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame; // Lưu tham chiếu đến JFrame cha
        initComponents();
    }
    
    private void initComponents() {
        // Setting main window properties
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

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        filterComboBox = new JComboBox<>(new String[]{"Group ID", "Group name", "Creation time"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JButton applyButton = new JButton("Apply");
        filterPanel.add(applyButton);
        
        // Add filter panel within the header panel
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table for Group Chat Information
        tableModel = new DefaultTableModel(new Object[]{"Group ID", "Name group", "Creation time", "Amount of members"}, 0);
        groupTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for testing (optional)
        tableModel.addRow(new Object[]{"GR01", "Study Group", "2024-11-01", 10});
        tableModel.addRow(new Object[]{"GR02", "Project Team", "2024-10-15", 5});

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Back button
        backButton = new JButton("BACK");
        JButton viewMember = new JButton("View member list");
        viewMember.addActionListener(e -> {
            MemberListManagement memberListManagementPanel  = new MemberListManagement();
            memberListManagementPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(memberListManagementPanel);
        }); // Open admin list view 

        JButton viewAdmin = new JButton("View admin list");
        viewAdmin.addActionListener(e -> {
            AdminListManagement adminListManagementPanel = new AdminListManagement();
            adminListManagementPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(adminListManagementPanel);
        }); // Open admin list view 

        buttonPanel.add(backButton);
        buttonPanel.add(viewMember);
        buttonPanel.add(viewAdmin);

        add(buttonPanel, BorderLayout.SOUTH);

    }

    //Switch panel
    private void switchPanel(JPanel newPanel) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(newPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    public JButton getBackButton() {
        return backButton;
    }
}
