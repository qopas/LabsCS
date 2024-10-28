package org.example.Lab3;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class PlayfairCipher {
    private static char[][] keyMatrix = new char[5][5];

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Alege operațiunea (encrypt/decrypt): ");
        String operation = scanner.nextLine().toLowerCase();

        System.out.print("Introduceți cheia (minim 7 caractere): ");
        String key = scanner.nextLine();
        if (key.length() < 7) {
            System.out.println("Eroare: Cheia trebuie să aibă minim 7 caractere.");
            return;
        }

        System.out.print("Introduceți textul: ");
        String inputText = scanner.nextLine();
        inputText = inputText.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        if (inputText.isEmpty()) {
            System.out.println("Eroare: Textul trebuie să conțină doar caractere alfabetice.");
            return;
        }

        setupKeyMatrix(key);
        if (operation.equals("encrypt")) {
            System.out.println("Text criptat: " + encrypt(inputText));
        } else if (operation.equals("decrypt")) {
            System.out.println("Text decriptat: " + decrypt(inputText));
        } else {
            System.out.println("Operațiune invalidă.");
        }
    }

    private static void setupKeyMatrix(String key) {
        key = key.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        Set<Character> uniqueChars = new LinkedHashSet<>();
        for (char c : key.toCharArray()) uniqueChars.add(c);
        for (char c = 'A'; c <= 'Z'; c++) if (c != 'J') uniqueChars.add(c);

        int k = 0;
        for (char c : uniqueChars) {
            keyMatrix[k / 5][k % 5] = c;
            k++;
            if (k == 25) break;
        }
    }

    private static String encrypt(String text) {
        return processText(formatTextForPairs(text), true);
    }

    private static String decrypt(String text) {
        return processText(formatTextForPairs(text), false);
    }

    private static String formatTextForPairs(String text) {
        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            formattedText.append(text.charAt(i));
            if (i + 1 < text.length()) {
                char nextChar = text.charAt(i + 1);
                if (text.charAt(i) == nextChar) {
                    formattedText.append('X');
                    i--;
                } else {
                    formattedText.append(nextChar);
                }
            }
        }
        if (formattedText.length() % 2 != 0) formattedText.append('X');
        return formattedText.toString();
    }

    private static String processText(String text, boolean isEncrypt) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += 2) {
            char char1 = text.charAt(i);
            char char2 = text.charAt(i + 1);
            int[] pos1 = findPosition(char1);
            int[] pos2 = findPosition(char2);

            if (pos1[0] == pos2[0]) {
                result.append(keyMatrix[pos1[0]][(pos1[1] + (isEncrypt ? 1 : 4)) % 5]);
                result.append(keyMatrix[pos2[0]][(pos2[1] + (isEncrypt ? 1 : 4)) % 5]);
            } else if (pos1[1] == pos2[1]) {
                result.append(keyMatrix[(pos1[0] + (isEncrypt ? 1 : 4)) % 5][pos1[1]]);
                result.append(keyMatrix[(pos2[0] + (isEncrypt ? 1 : 4)) % 5][pos2[1]]);
            } else {
                result.append(keyMatrix[pos1[0]][pos2[1]]);
                result.append(keyMatrix[pos2[0]][pos1[1]]);
            }
        }
        return result.toString();
    }

    private static int[] findPosition(char ch) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyMatrix[i][j] == ch) return new int[]{i, j};
            }
        }
        return null;
    }
}
