package org.example.Lab2;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class FrequencyAnalyzer extends JFrame {
    private JTextArea encryptedTextArea;
    private JTextArea decryptedTextArea;
    private JTable frequencyTable;
    private JTable substitutionTable;
    private DefaultTableModel substitutionModel;
    private DefaultTableModel frequencyModel;
    private Map<Character, Character> substitutionMap;
    private String originalText;

    public FrequencyAnalyzer() {
        setTitle("Frequency Analyzer and Character Substitution Tool");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Encrypted Text Area
        encryptedTextArea = new JTextArea(5, 20);
        encryptedTextArea.setLineWrap(true);
        encryptedTextArea.setWrapStyleWord(true);
        add(new JScrollPane(encryptedTextArea), BorderLayout.NORTH);

        // Decrypted Text Area
        decryptedTextArea = new JTextArea(5, 20);
        decryptedTextArea.setLineWrap(true);
        decryptedTextArea.setWrapStyleWord(true);
        decryptedTextArea.setEditable(false);
        add(new JScrollPane(decryptedTextArea), BorderLayout.CENTER);

        // Frequency Table
        frequencyModel = new DefaultTableModel(new Object[]{"Character", "Frequency"}, 0);
        frequencyTable = new JTable(frequencyModel);
        add(new JScrollPane(frequencyTable), BorderLayout.EAST);

        // Substitution Table
        substitutionModel = new DefaultTableModel(2, 26);
        substitutionTable = new JTable(substitutionModel);

        // Fill first row with A-Z characters and set column width
        for (int i = 0; i < 26; i++) {
            substitutionModel.setValueAt((char) ('A' + i), 0, i);
            substitutionTable.getColumnModel().getColumn(i).setPreferredWidth(30);
        }
        substitutionTable.setTableHeader(null);  // Remove column headers

        // Panel to hold substitution table
        JPanel substitutionPanel = new JPanel(new BorderLayout());
        substitutionPanel.add(new JLabel("Substitution Table"), BorderLayout.NORTH);
        substitutionPanel.add(substitutionTable, BorderLayout.CENTER);
        add(substitutionPanel, BorderLayout.SOUTH);

        substitutionMap = new HashMap<>();

        // Add listener for substitution changes
        substitutionModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() == 1) {
                int col = e.getColumn();
                char originalChar = (char) ('a' + col);
                String newSubstitute = (String) substitutionModel.getValueAt(1, col);

                if (newSubstitute != null && newSubstitute.length() == 1) {
                    char substituteChar = newSubstitute.toLowerCase().charAt(0);
                    substitutionMap.put(originalChar, substituteChar);
                    applySubstitutions();
                }
            }
        });

        encryptedTextArea.getDocument().addDocumentListener(new TextChangeListener());
        setVisible(true);
    }

    // Analyze frequency in encrypted text
    private void analyzeFrequency() {
        originalText = encryptedTextArea.getText().toLowerCase();  // Store the original text
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // Count frequency of each character
        for (char c : originalText.toCharArray()) {
            if (Character.isLetter(c)) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }
        }

        // Update frequency table
        frequencyModel.setRowCount(0);  // Clear previous data
        frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> frequencyModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        decryptedTextArea.setText(originalText);
    }

    // Apply all substitutions to the original text and update the decrypted text area
    private void applySubstitutions() {
        StringBuilder decryptedText = new StringBuilder(originalText);
        for (int i = 0; i < decryptedText.length(); i++) {
            char originalChar = decryptedText.charAt(i);
            if (substitutionMap.containsKey(originalChar)) {
                decryptedText.setCharAt(i, substitutionMap.get(originalChar));
            }
        }
        decryptedTextArea.setText(decryptedText.toString());
    }

    private class TextChangeListener implements javax.swing.event.DocumentListener {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            analyzeFrequency();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            analyzeFrequency();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrequencyAnalyzer::new);
    }
}
