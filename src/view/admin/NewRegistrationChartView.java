import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class NewRegistrationChartView extends JPanel {

    private JTextField yearFilterField;
    private JButton applyFilterButton;
    private JButton backButton;
    private ChartPanel chartPanel; // Declare chartPanel at class level
    private int[] userCounts; // Declare userCounts as an instance variable

    public NewRegistrationChartView() {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("New registration Chart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Set year to draw
        yearFilterField = new JTextField(7);
        filterSortPanel.add(new JLabel("Year:"));
        filterSortPanel.add(yearFilterField);

        // Apply button
        applyFilterButton = new JButton("Apply");
        filterSortPanel.add(applyFilterButton);

        // Default value label
        int defaultYear = LocalDate.now().getYear();
        JLabel defaultValueLabel = new JLabel("Year default: " + defaultYear);
        filterSortPanel.add(defaultValueLabel);

        // Combine header and filter panels into a north container
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Fetch initial data for the current year
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        userCounts = ChartDataFetcher.fetchUserCountsByMonth(defaultYear); // Initialize userCounts

        // Initialize the ChartPanel with fetched data
        chartPanel = new ChartPanel(userCounts, months);
        add(chartPanel, BorderLayout.CENTER);

        // Add actionListener to apply year
        applyFilterButton.addActionListener(e -> {
            String yearText = yearFilterField.getText();
            try {
                int year = Integer.parseInt(yearText);

                // Fetch new data for the selected year
                userCounts = ChartDataFetcher.fetchUserCountsByMonth(year);

                // Update chart data and repaint
                chartPanel.setData(userCounts, months); // Update data and repaint the chart
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid year.");
            }
        });

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JButton getBackButton() {
        return backButton;
    }
}
