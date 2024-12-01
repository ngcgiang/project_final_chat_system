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

    // Custom panel for drawing the chart
    class ChartPanel extends JPanel {
        private int[] userCounts; // Số lượng người dùng mỗi tháng
        private String[] months;

        public ChartPanel(int[] userCounts, String[] months) {
            this.userCounts = userCounts;
            this.months = months;
        }

        // Setters to update data and repaint
        public void setData(int[] userCounts, String[] months) {
            this.userCounts = userCounts;
            this.months = months;
            repaint(); // Repaint chart after updating data
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int chartWidth = getWidth() - 100;
            int chartHeight = getHeight() - 100;

            int barWidth = chartWidth / userCounts.length;
            int maxUsers = 0;

            // Determine the maximum value in userCounts to scale the chart
            for (int count : userCounts) {
                if (count > maxUsers) maxUsers = count;
            }

            // Draw axes
            g.drawLine(50, 50, 50, chartHeight + 50); // Y-axis
            g.drawLine(50, chartHeight + 50, chartWidth + 50, chartHeight + 50); // X-axis

            // handle year with no register
            if(maxUsers != 0){
                // Draw bars and labels
                for (int i = 0; i < userCounts.length; i++) {
                    int barHeight = (userCounts[i] * chartHeight) / maxUsers;
                    g.setColor(Color.BLUE);
                    g.fillRect(50 + i * barWidth, chartHeight + 50 - barHeight, barWidth - 10, barHeight);

                    g.setColor(Color.BLACK);
                    g.drawString(months[i], 50 + i * barWidth + barWidth / 4, chartHeight + 70);
                }

                // Add y-axis labels
                for (int i = 0; i <= maxUsers; i += maxUsers / 5) {
                    int y = chartHeight + 50 - (i * chartHeight) / maxUsers;
                    g.drawString(String.valueOf(i), 20, y);
                    g.drawLine(45, y, 50, y);
                }
            }
            else {
                for (int i = 0; i < userCounts.length; i++) {
                    g.drawString(months[i], 50 + i * barWidth + barWidth / 4, chartHeight + 70);
                }
                int y = chartHeight + 50;  
                g.drawString("0", 20, y); 
                g.drawLine(45, y, 50, y);  

            }
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
