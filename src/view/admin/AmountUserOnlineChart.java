import java.awt.*;
import javax.swing.*;

public class AmountUserOnlineChart extends JPanel {

    private JTextField usernameFilterField;
    private JButton applyFilterButton;
    private JButton backButton;

    public AmountUserOnlineChart() {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("Amount User Online Chart");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Set year to draw
        usernameFilterField = new JTextField(7);
        filterSortPanel.add(new JLabel("Year:"));
        filterSortPanel.add(usernameFilterField);

        // Apply 
        applyFilterButton = new JButton("Apply");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Chart panel
        JPanel chartPanel = new ChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Custom panel for drawing the chart
    class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Example data: new registrations for each month
            int[] registrations = {30, 45, 60, 80, 90, 70, 100, 120, 95, 85, 50, 40};
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            int chartWidth = getWidth() - 100;
            int chartHeight = getHeight() - 100;

            int barWidth = chartWidth / registrations.length;
            int maxRegistrations = 120; // Assume max for scaling

            // Draw axes
            g.drawLine(50, 50, 50, chartHeight + 50); // Y-axis
            g.drawLine(50, chartHeight + 50, chartWidth + 50, chartHeight + 50); // X-axis

            // Draw bars and labels
            for (int i = 0; i < registrations.length; i++) {
                int barHeight = (registrations[i] * chartHeight) / maxRegistrations;
                g.setColor(Color.ORANGE);
                g.fillRect(50 + i * barWidth, chartHeight + 50 - barHeight, barWidth - 10, barHeight);

                g.setColor(Color.BLACK);
                g.drawString(months[i], 50 + i * barWidth + barWidth / 4, chartHeight + 70);
            }

            // Add y-axis labels
            for (int i = 0; i <= maxRegistrations; i += 20) {
                int y = chartHeight + 50 - (i * chartHeight) / maxRegistrations;
                g.drawString(String.valueOf(i), 20, y);
                g.drawLine(45, y, 50, y);
            }
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
