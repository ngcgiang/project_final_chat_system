import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LoginManagement extends JFrame {
    private JTable historyLoginTable;
    private DefaultTableModel tableModel;

    public LoginManagement() {
        // Container
        setTitle("Login Management - Chat system");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Label
        JLabel loginHisLabel = new JLabel("Login history management");
        loginHisLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(loginHisLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH); // Add header to frame

        // Table for Login History
        tableModel = new DefaultTableModel(new Object[]{"Login Time","User ID", "User-name", "Full-name"}, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        // Sample data for testing
        tableModel.addRow(new Object[]{"2024-11-08 10:15:00", "US01", "ngcjang", "Nguyen Ngoc Giang"});
        tableModel.addRow(new Object[]{"2024-11-08 10:18:45", "US02",  "huytan", "Nguyen Huy Tan"});

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginManagement());
    }
}
