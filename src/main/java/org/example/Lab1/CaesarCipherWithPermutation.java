package org.example.Lab1;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class CaesarCipherWithPermutation {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose operation (E for encryption, D for decryption): ");
        String operation = scanner.nextLine().toUpperCase();

        if (!operation.equals("E") && !operation.equals("D")) {
            System.out.println("Invalid operation. Please enter 'E' for encryption or 'D' for decryption.");
            return;
        }

        System.out.print("Enter key 1 (1-25): ");
        int key1 = scanner.nextInt();
        if (key1 < 1 || key1 > 25) {
            System.out.println("Invalid key. Please enter a value between 1 and 25.");
            return;
        }
        scanner.nextLine();

        System.out.print("Enter key 2 (a word with unique letters, minimum length 7): ");
        String key2 = scanner.nextLine().toUpperCase();
        if (key2.length() < 7 || !key2.matches("[A-Z]+")) {
            System.out.println("Invalid key 2. Ensure it has only letters and is at least 7 characters long.");
            return;
        }

        System.out.print("Enter the message: ");
        String message = scanner.nextLine().toUpperCase().replaceAll(" ", "");

        // Generate permuted alphabet based on key 2
        String permutedAlphabet = generatePermutedAlphabet(key2);

        String result;
        if (operation.equals("E")) {
            result = encrypt(message, key1, permutedAlphabet);
            System.out.println("Encrypted message: " + result);
        } else {
            result = decrypt(message, key1, permutedAlphabet);
            System.out.println("Decrypted message: " + result);
        }

        scanner.close();
    }

    private static String encrypt(String message, int key1, String permutedAlphabet) {
        return shiftMessage(message, key1, permutedAlphabet);
    }

    private static String decrypt(String message, int key1, String permutedAlphabet) {
        return shiftMessage(message, 26 - key1, permutedAlphabet);
    }

    private static String shiftMessage(String message, int shift, String permutedAlphabet) {
        StringBuilder shiftedMessage = new StringBuilder();

        for (char ch : message.toCharArray()) {
            int letterIndex = permutedAlphabet.indexOf(ch);

            // Only process valid alphabetic characters
            if (letterIndex != -1) {
                int newIndex = (letterIndex + shift) % 26;
                shiftedMessage.append(permutedAlphabet.charAt(newIndex));
            } else {
                System.out.println("Invalid character in the message. Only A-Z characters are allowed.");
                return "";
            }
        }

        return shiftedMessage.toString();
    }

    private static String generatePermutedAlphabet(String key2) {
        Set<Character> uniqueLetters = new LinkedHashSet<>();

        // Add key2 letters first, ensuring no duplicates
        for (char ch : key2.toCharArray()) {
            uniqueLetters.add(ch);
        }

        // Add remaining letters of the alphabet in order
        for (char ch : ALPHABET.toCharArray()) {
            uniqueLetters.add(ch);
        }

        // Convert the set to a string to form the permuted alphabet
        StringBuilder permutedAlphabet = new StringBuilder();
        for (char ch : uniqueLetters) {
            permutedAlphabet.append(ch);
        }

        return permutedAlphabet.toString();
    }
}