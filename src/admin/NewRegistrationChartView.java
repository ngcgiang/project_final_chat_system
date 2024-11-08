import javax.swing.*;
import java.awt.*;

public class NewRegistrationChartView extends JFrame {
    private JComboBox<String> yearComboBox;
    private JPanel chartPanel;

    public NewRegistrationChartView() {
        // Set up the main window for the chart view
        setTitle("New Registration Chart by Year");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header panel for selecting the year
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);

        JLabel yearLabel = new JLabel("Select Year:");
        headerPanel.add(yearLabel);

        // ComboBox for selecting the year
        yearComboBox = new JComboBox<>(new String[]{"2022", "2023", "2024"});
        headerPanel.add(yearComboBox);

        JButton showChartButton = new JButton("Show Chart");
        headerPanel.add(showChartButton);

        add(headerPanel, BorderLayout.NORTH);

        // Chart panel placeholder
        chartPanel = new JPanel();
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewRegistrationChartView());
    }
}
