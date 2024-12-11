package view.admin;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UpdateUserDialog extends JDialog {
    private JTextField fullNameField;
    private JTextField addressField;
    private JTextField dobField; // Format: yyyy-MM-dd
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JComboBox<String> statusComboBox;
    private boolean saved;

    public UpdateUserDialog(Frame owner, String fullName, String address, String dob, String gender, String email, String status) {
        super(owner, "Update User Information", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(owner);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField(fullName);
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField(address);
        formPanel.add(addressField);

        formPanel.add(new JLabel("Date of Birth (yyyy-MM-dd):"));
        dobField = new JTextField(dob);
        formPanel.add(dobField);

        formPanel.add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderComboBox.setSelectedItem(gender);
        formPanel.add(genderComboBox);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField(email);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Online", "Offline"});
        statusComboBox.setSelectedItem(status);
        formPanel.add(statusComboBox);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        saveButton.addActionListener(e -> {
            saved = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    public boolean isSaved() {
        return saved;
    }

    public String getFullName() {
        return fullNameField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public Date getDateOfBirth() throws Exception {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dobField.getText());
    }

    public String getGender() {
        return (String) genderComboBox.getSelectedItem();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }
}
