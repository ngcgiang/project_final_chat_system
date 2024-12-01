import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminListManagement extends JPanel {
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public AdminListManagement(int groupID, String groupName) {
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
        // Base query
        String query = """
                SELECT u.UserID, u.UserName, u.FullName
                FROM Users u
                INNER JOIN GroupInfo g ON g.AdminID = u.UserID
                WHERE g.GroupID = ?
                GROUP BY u.UserID, u.UserName, u.FullName
                ORDER BY u.UserID
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Gán giá trị cho tham số dấu hỏi ?
            preparedStatement.setInt(1, groupID);

            // Execute query and fetch results
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear the table before adding new data
            tableModel.setRowCount(0);

            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String userName = resultSet.getString("UserName");
                String fullName = resultSet.getNString("FullName");

                // Thêm dòng vào tableModel
                tableModel.addRow(new Object[] { userID, userName, fullName });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
