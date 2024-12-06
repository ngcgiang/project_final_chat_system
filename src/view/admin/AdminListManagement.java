import components.admin.*;
import components.admin.group_chat.*;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AdminListManagement extends JPanel {
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private GroupChatBUS groupChatBUS;

    public AdminListManagement(int groupID, String groupName) {
        groupChatBUS = new GroupChatBUS();

        // Setting main window properties
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(600, 70)); // Adjusted to fit filter panel

        // Left Panel for Header
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel chatLabel = new JLabel("Admin List Of " + groupName);
        chatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(chatLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Add filter panel within the header panel
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table for Member List Information
        tableModel = new DefaultTableModel(new Object[] { "AdminID", "Username", "Full-name" }, 0);
        adminTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadDataFromDatabase(groupID);

    }

    private void loadDataFromDatabase(int groupID) {
        List<AdminUserDTO> admins = groupChatBUS.getGroupAdmins(groupID);

        tableModel.setRowCount(0); // Clear the table
        for (AdminUserDTO user : admins) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getFullName()
            });
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
