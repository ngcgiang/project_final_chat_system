import javax.swing.*;
import java.awt.*;

public class AmountUserOnlineChart extends JFrame {

    private JTextField usernameFilterField;
    private JButton applyFilterButton;

    public AmountUserOnlineChart() {
        // Main window setup
        setTitle("User Online Management - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AmountUserOnlineChart());
    }
}
