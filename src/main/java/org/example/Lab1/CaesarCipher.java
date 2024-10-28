package org.example.Lab1;

import java.util.Scanner;

public class CaesarCipher {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose operation (E for encryption, D for decryption): ");
        String operation = scanner.nextLine().toUpperCase();

        if (!operation.equals("E") && !operation.equals("D")) {
            System.out.println("Invalid operation. Please enter 'E' for encryption or 'D' for decryption.");
            return;
        }

        System.out.print("Enter key (1-25): ");
        int key = scanner.nextInt();
        if (key < 1 || key > 25) {
            System.out.println("Invalid key. Please enter a value between 1 and 25.");
            return;
        }
        scanner.nextLine(); // Consume the newline left by nextInt()

        System.out.print("Enter the message: ");
        String message = scanner.nextLine().toUpperCase().replaceAll(" ", "");

        String result;
        if (operation.equals("E")) {
            result = encrypt(message, key);
            System.out.println("Encrypted message: " + result);
        } else {
            result = decrypt(message, key);
            System.out.println("Decrypted message: " + result);
        }

        scanner.close();
    }

    private static String encrypt(String message, int key) {
        return shiftMessage(message, key);
    }

    private static String decrypt(String message, int key) {
        return shiftMessage(message, 26 - key);
    }

    private static String shiftMessage(String message, int shift) {
        StringBuilder shiftedMessage = new StringBuilder();

        for (char ch : message.toCharArray()) {
            int letterIndex = ALPHABET.indexOf(ch);

            // Only process valid alphabetic characters
            if (letterIndex != -1) {
                int newIndex = (letterIndex + shift) % 26;
                shiftedMessage.append(ALPHABET.charAt(newIndex));
            } else {
                System.out.println("Invalid character in the message. Only A-Z characters are allowed.");
                return "";
            }
        }

        return shiftedMessage.toString();
    }
}
