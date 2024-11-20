import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemberListManagement extends JPanel {
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    private JButton backButton;

    public MemberListManagement() {
        // Setting main window properties
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(600, 70)); // Adjusted to fit filter panel

        // Left Panel for Header
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel chatLabel = new JLabel("Member List Management");
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

        filterComboBox = new JComboBox<>(new String[]{"Group ID", "User ID"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JButton applyButton = new JButton("Apply");
        filterPanel.add(applyButton);
        
        // Add filter panel within the header panel
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table for Member List Information
        tableModel = new DefaultTableModel(new Object[]{"Group ID", "User ID"}, 0);
        memberTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for testing (optional)
        tableModel.addRow(new Object[]{"GR01", "User001"});
        tableModel.addRow(new Object[]{"GR01", "User002"});
        tableModel.addRow(new Object[]{"GR02", "User101"});
        tableModel.addRow(new Object[]{"GR02", "User102"});

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

    }

    public JButton getBackButton() {
        return backButton;
    }
}
