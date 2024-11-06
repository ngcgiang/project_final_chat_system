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

        // Title Panel 
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Login History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        // Add titlePanel to the top of the frame
        add(titlePanel, BorderLayout.NORTH);

        // Main table panel for user information
        String[] columnNames = {"ID", "Username", "Full-name", "Time"};
        tableModel = new DefaultTableModel(columnNames, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginManagement());
    }
}
