package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import components.admin.AdminUserBUS;
import components.admin.AdminUserDTO;

public class NewUserManagement extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilterField;
    private JComboBox<String> dateRangeComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton showChartButton;
    private JButton backButton;
    private JFrame parentFrame;
    private AdminUserBUS adminUserBUS;

    public NewUserManagement(JFrame parentFrame) {
        adminUserBUS = new AdminUserBUS();

        this.parentFrame = parentFrame; // Lưu tham chiếu đến JFrame cha
        initComponents();
    }
    
    private void initComponents() {
        // Main window setup
        setLayout(new BorderLayout());

        // Main header and filter/sort panel to hold all north components
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("New User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Registration time", "Full-name"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Time
        dateRangeComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Date range:"));
        filterSortPanel.add(dateRangeComboBox);

        // Filter by Username
        nameFilterField = new JTextField(15);
        filterSortPanel.add(new JLabel("Search by Name:"));
        filterSortPanel.add(nameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply Filter");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Table for displaying new user registrations
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Full-name", "Registration Time"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Show Chart Button
        showChartButton = new JButton("Show Registration Chart");
        showChartButton.addActionListener(e -> {
            NewRegistrationChartView newRegistrationChartViewPanel = new NewRegistrationChartView();
            newRegistrationChartViewPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(newRegistrationChartViewPanel);
        }); // Open chart view on button click


        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);
        buttonPanel.add(showChartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        //Add actionListener sort
        sortComboBox.addActionListener(e ->{
            String fullName = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, fullName);
        });

        //Add actionListener apply time
        dateRangeComboBox.addActionListener(e -> {
            String fullName = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, fullName);
        });

        //Add actionListener search filter
        applyFilterButton.addActionListener(e -> {
            String fullName = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, fullName);
        });

        String usernameDef = null;
        String sortByDef = "Registration time";
        String timeDef = "All";

        loadDataFromDatabase(sortByDef, timeDef, usernameDef);
    }

    private void loadDataFromDatabase(String sortBy, String time, String fullName) {
        List<AdminUserDTO> newUserList = adminUserBUS.getNewUsers(sortBy, time, fullName);
        tableModel.setRowCount(0);
        for (AdminUserDTO user : newUserList) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getFullName(),
                user.getCreatedAt(),
            });
        }
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
