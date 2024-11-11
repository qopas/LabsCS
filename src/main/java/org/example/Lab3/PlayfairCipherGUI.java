package org.example.Lab3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;

public class PlayfairCipherGUI extends JFrame {
    private JTextField keyField;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JComboBox<String> operationBox;
    private PlayfairCipher playfairCipher;
    private JTable matrixTable;  // Table to display the Playfair matrix
    private DefaultTableModel matrixTableModel; // Model for JTable

    public PlayfairCipherGUI() {
        setTitle("Playfair Cipher");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels for layout
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        JPanel matrixPanel = new JPanel(new BorderLayout(5, 5));

        // Key input
        topPanel.add(new JLabel("Enter Key (at least 7 characters):"));
        keyField = new JTextField();
        topPanel.add(keyField);

        // Operation selection
        topPanel.add(new JLabel("Choose Operation:"));
        operationBox = new JComboBox<>(new String[]{"Encrypt", "Decrypt"});
        topPanel.add(operationBox);

        // Message input
        topPanel.add(new JLabel("Enter Message:"));
        inputArea = new JTextArea(3, 30);
        topPanel.add(new JScrollPane(inputArea));

        // Output display
        bottomPanel.add(new JLabel("Output:"), BorderLayout.NORTH);
        outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button for processing
        JButton processButton = new JButton("Process");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processInput();
            }
        });
        bottomPanel.add(processButton, BorderLayout.SOUTH);

        // Matrix display table
        matrixPanel.add(new JLabel("Playfair Matrix:"), BorderLayout.NORTH);
        matrixTableModel = new DefaultTableModel(6, 6); // 6x6 table model
        matrixTable = new JTable(matrixTableModel);
        matrixTable.setTableHeader(null);
        matrixTable.setEnabled(false); // Disable editing of the table
        matrixTable.setRowHeight(30);
        matrixPanel.add(new JScrollPane(matrixTable), BorderLayout.CENTER);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(matrixPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void processInput() {
        String key = keyField.getText();
        String message = inputArea.getText();
        String operation = (String) operationBox.getSelectedItem();

        // Validate and process key
        if (key.length() < 7) {
            JOptionPane.showMessageDialog(this, "Key length must be at least 7 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            playfairCipher = new PlayfairCipher(key);

            // Display the Playfair matrix
            displayMatrix(playfairCipher.getMatrix());

            String result;
            if ("Encrypt".equalsIgnoreCase(operation)) {
                result = playfairCipher.encrypt(message);
            } else {
                result = playfairCipher.decrypt(message);
            }

            // Display the result
            outputArea.setText(result);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrixTableModel.setValueAt(matrix[i][j], i, j);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayfairCipherGUI gui = new PlayfairCipherGUI();
            gui.setVisible(true);
        });
    }
}
