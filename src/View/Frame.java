package View;

import Model.DatabaseTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Frame extends JFrame {

    private final int xFrameSize = 550;
    private final int yFrameSize = 500;

    public Frame(DatabaseTableModel tableModel, String title) {

        // setup frame
        setTitle(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(xFrameSize, yFrameSize);
        setLocationRelativeTo(null);
        setResizable(false);

        // create panels
        JPanel mainPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // create table and scroll pane
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // create buttons
        JButton newButton = new JButton("New");
        JButton saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        JButton deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);

        // create info text fields and areas
        JLabel lastNameLabel = new JLabel("Last name");
        JLabel firstNameLabel = new JLabel("First name");
        JLabel jobTitleLabel = new JLabel("Job title");
        JLabel phoneLabel = new JLabel("Phone");
        JLabel emailLabel = new JLabel("Email");
        JTextField lastNameTextField = new JTextField();
        JTextField firstNameTextField = new JTextField();
        JTextField jobTitleTextField = new JTextField();
        JTextField phoneTextField = new JTextField();
        JTextField emailTextField = new JTextField();

        // setup main panel
        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        // add table
        scrollPane.setBounds(10, 10, 530, 270);
        mainPanel.add(scrollPane);

        // add info panel
        infoPanel.setBounds(10, 290, 400, 200);
        infoPanel.setBorder(new TitledBorder("Employees information"));
        mainPanel.add(infoPanel);
        infoPanel.setLayout(new GridLayout(5, 2, 10, 10));
        infoPanel.add(lastNameLabel);
        infoPanel.add(lastNameTextField);
        infoPanel.add(firstNameLabel);
        infoPanel.add(firstNameTextField);
        infoPanel.add(jobTitleLabel);
        infoPanel.add(jobTitleTextField);
        infoPanel.add(phoneLabel);
        infoPanel.add(phoneTextField);
        infoPanel.add(emailLabel);
        infoPanel.add(emailTextField);

        // add buttons
        newButton.setBounds(425, 320, 100, 30);
        mainPanel.add(newButton);
        saveButton.setBounds(425, 380, 100, 30);
        mainPanel.add(saveButton);
        deleteButton.setBounds(425, 440, 100, 30);
        mainPanel.add(deleteButton);

        // init table listener
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastNameTextField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
                firstNameTextField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
                jobTitleTextField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
                phoneTextField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
                emailTextField.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 5)));
                deleteButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });

        // init "New" button listener
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = tableModel.addEmployee(
                        String.valueOf(lastNameTextField.getText()),
                        String.valueOf(firstNameTextField.getText()),
                        String.valueOf(jobTitleTextField.getText()),
                        String.valueOf(phoneTextField.getText()),
                        String.valueOf(emailTextField.getText()));
                saveButton.setEnabled(false);
                deleteButton.setEnabled(false);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Successfully add new employee");
                } else {
                    JOptionPane.showMessageDialog(null, "Can't add new employee");
                }
            }
        });

        // init "Save" button listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
                boolean result = tableModel.updateEmployeeInfo(
                        employeeID,
                        String.valueOf(lastNameTextField.getText()),
                        String.valueOf(firstNameTextField.getText()),
                        String.valueOf(jobTitleTextField.getText()),
                        String.valueOf(phoneTextField.getText()),
                        String.valueOf(emailTextField.getText()));
                saveButton.setEnabled(false);
                deleteButton.setEnabled(false);
                if (result) {
                    JOptionPane.showMessageDialog(null, "Successfully update employee info");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed update employee info");
                }
            }
        });

        // init "Delete" button listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int employeeID = Integer.valueOf(table.getValueAt(table.getSelectedRow(), 0).toString());
                saveButton.setEnabled(false);
                deleteButton.setEnabled(false);
                if (tableModel.deleteEmployee(employeeID)) {
                    JOptionPane.showMessageDialog(null, "Successfully delete employee #" + employeeID);
                } else {
                    JOptionPane.showMessageDialog(null, "Delete failed");
                }
            }
        });

        // start showing frame
        setVisible(true);
    }

}