package org.example.Lab4;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
public class DES {
    // Initial Permutation Table
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Permuted Choice 1 (PC-1)
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    // Permuted Choice 2 (PC-2)
    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    // Expansion Table
    private static final int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private static final int[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };
    // S-Box Tables
    private static final int[][][] S_BOXES = {
            { // S1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { // S2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { // S3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { // S4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { // S5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { // S6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { // S7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { // S8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    // Initial message and key
    private static String message = "01234567";
    private static String key = "13345779";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DES::createAndShowGUI);
    }
    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("DES Algorithm Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setLayout(new BorderLayout());

        // Input panel for message and key
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));

        JLabel messageLabel = new JLabel("Message (8 chars):");
        JTextField messageField = new JTextField("01234567");

        JLabel keyLabel = new JLabel("Key (8 chars):");
        JTextField keyField = new JTextField("13345779");

        JButton processButton = new JButton("Process");

        inputPanel.add(messageLabel);
        inputPanel.add(messageField);
        inputPanel.add(keyLabel);
        inputPanel.add(keyField);
        inputPanel.add(processButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Output area
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Button action
        processButton.addActionListener(e -> {
            String message = messageField.getText();
            String key = keyField.getText();
            outputArea.setText("");

            // Validate inputs
            if (message.length() != 8 || key.length() != 8) {
                JOptionPane.showMessageDialog(frame, "Both Message and Key must be 8 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Process DES
            processDES(message, key, outputArea);
        });

        // Show the GUI
        frame.setVisible(true);
    }

    private static void processDES(String message, String key, JTextArea outputArea) {
        outputArea.append("Processing DES Algorithm...\n\n");

        // Step 1: Convert message to binary
        String messageBinary = formatBinary(stringToBinary(message));
        outputArea.append("Message (binary):\n" + messageBinary + "\n\n");

        // Step 2: Apply Initial Permutation (IP)
        String permutedMessage = formatBinary(permute(stringToBinary(message), IP));
        outputArea.append("After Initial Permutation (IP):\n" + permutedMessage + "\n\n");

        // Step 3: Split into L0 and R0
        String L0 = permutedMessage.replace(" ", "").substring(0, 32);
        String R0 = permutedMessage.replace(" ", "").substring(32, 64);
        outputArea.append("L0:\n" + formatBinary(L0) + "\n");
        outputArea.append("R0:\n" + formatBinary(R0) + "\n\n");

        // Step 4: Generate K1 (Subkey for Round 1)
        String keyBinary = stringToBinary(key);
        String permutedKey = permute(keyBinary, PC1);
        String C0 = permutedKey.substring(0, 28);
        String D0 = permutedKey.substring(28, 56);
        String C1 = leftShift(C0, 1);
        String D1 = leftShift(D0, 1);
        String K1 = formatBinary(permute(C1 + D1, PC2));
        outputArea.append("K1 (Subkey for Round 1):\n" + K1 + "\n\n");

        // Step 5: Expand R0
        String expandedR0 = formatBinary(permute(R0, E));
        outputArea.append("Expanded R0:\n" + expandedR0 + "\n\n");

        // Step 6: XOR with K1
        String xorResult = formatBinary(xor(expandedR0.replace(" ", ""), K1.replace(" ", "")));
        outputArea.append("R0 XOR K1:\n" + xorResult + "\n\n");

        // Step 7: S-Box Substitution
        String substituted = formatBinary(sBoxSubstitution(xorResult.replace(" ", "")));
        outputArea.append("After S-Box Substitution:\n" + substituted + "\n\n");

        // Step 8: Permutation (P)
        String permutedSubstitution = formatBinary(permute(substituted.replace(" ", ""), P));
        outputArea.append("After Permutation (P):\n" + permutedSubstitution + "\n\n");

        // Step 9: Calculate L1 and R1
        String L1 = formatBinary(R0);
        String R1 = formatBinary(xor(L0, permutedSubstitution.replace(" ", "")));
        outputArea.append("L1:\n" + L1 + "\n");
        outputArea.append("R1:\n" + R1 + "\n");
    }

    // Utility functions
    private static String stringToBinary(String input) {
        StringBuilder binary = new StringBuilder();
        for (char c : input.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    private static String permute(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int i : table) {
            output.append(input.charAt(i - 1));
        }
        return output.toString();
    }

    private static String leftShift(String input, int shifts) {
        return input.substring(shifts) + input.substring(0, shifts);
    }

    private static String xor(String input1, String input2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input1.length(); i++) {
            result.append(input1.charAt(i) ^ input2.charAt(i));
        }
        return result.toString();
    }
    private static String sBoxSubstitution(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String chunk = input.substring(i * 6, (i + 1) * 6);
            int row = Integer.parseInt("" + chunk.charAt(0) + chunk.charAt(5), 2);
            int col = Integer.parseInt(chunk.substring(1, 5), 2);
            int value = S_BOXES[i][row][col];
            output.append(String.format("%4s", Integer.toBinaryString(value)).replace(' ', '0'));
        }
        return output.toString();
    }
    private static String formatBinary(String binary) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < binary.length(); i++) {
            formatted.append(binary.charAt(i));
            if ((i + 1) % 4 == 0) {
                formatted.append(" ");
            }
        }
        return formatted.toString().trim();
    }
}
