package components.shared.utils;

import java.awt.*;
import javax.swing.*;

public class Utilities {
    public static JButton createButton(String text, int width, int height) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }

    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JLabel createLabel(String text, String font, int size) {
        JLabel label = new JLabel(text);
        switch (font) {
            case "bold":
                label.setFont(new Font("Arial", Font.BOLD, size));
                break;
            case "plain":
                label.setFont(new Font("Arial", Font.PLAIN, size));
                break;
        }
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }
}
