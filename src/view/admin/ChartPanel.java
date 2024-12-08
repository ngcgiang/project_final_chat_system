package view.admin;

import java.awt.*;
import javax.swing.*;

 // Custom panel for drawing the chart
 class ChartPanel extends JPanel {
    private int[] userCounts; // Số lượng người đăng kí mỗi tháng
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
            int stepHeight = (maxUsers < 5) ? 1 : maxUsers / 5;

            for (int i = 0; i <= maxUsers; i += stepHeight) {
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