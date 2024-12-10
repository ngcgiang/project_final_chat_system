package view.admin;

import components.admin.login_history.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoginManagement extends JPanel {
    private JTable historyLoginTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private LoginHistoryBUS loginHistoryBUS;

    public LoginManagement() {
        loginHistoryBUS = new LoginHistoryBUS();

        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        JLabel loginHisLabel = new JLabel("Login history management");
        loginHisLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(loginHisLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Table for Login History
        tableModel = new DefaultTableModel(new Object[]{"Login Time", "User ID", "Username", "Full Name"}, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadLoginHistoryData();

        setVisible(true);
    }

    public LoginManagement(int userId) {
        // Container
        setLayout(new BorderLayout());

        loginHistoryBUS = new LoginHistoryBUS();

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Label
        JLabel loginHisLabel = new JLabel("Login history for UserID: " + userId);
        loginHisLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(loginHisLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH); // Add header to frame

        // Table for Login History
        tableModel = new DefaultTableModel(new Object[]{"Username", "Login Time", "Logout Time"}, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from database
        loadLoginHistoryData(userId);

        // Add ActionListener for Back button
        backButton.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow instanceof JFrame) {
                ((JFrame) parentWindow).dispose(); // Close the JFrame
            }
        });

        setVisible(true);
    }

    private void loadLoginHistoryData() {
        List<LoginHistoryDTO> loginHistories = loginHistoryBUS.getAllLoginHistory();
        tableModel.setRowCount(0); // Clear existing data
        for (LoginHistoryDTO history : loginHistories) {
            tableModel.addRow(new Object[]{
                history.getLoginTime(),
                history.getUserId(),
                history.getUsername(),
                history.getFullName()
            });
        }
    }

    private void loadLoginHistoryData(int userID) {
        List<LoginHistoryDTO> loginHistories = loginHistoryBUS.getUserLoginHistory(userID);
        tableModel.setRowCount(0); // Clear existing data
        for (LoginHistoryDTO history : loginHistories) {
            tableModel.addRow(new Object[]{
                history.getUsername(),
                history.getLoginTime(),
                history.getLogoutTime()
            });
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
