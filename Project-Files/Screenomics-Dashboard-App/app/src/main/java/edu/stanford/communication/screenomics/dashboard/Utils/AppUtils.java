/**
 * AppUtils.java
 * Created by Ian Kim; Updated on 2025-06-20.
 * © The Stanford Screenomics Lab
 */

package edu.stanford.communication.screenomics.dashboard.Utils;

public class AppUtils {

    /**
     * Extracts the third part (index 2) of an underscore-delimited string.
     * Returns null if the input doesn't have at least three parts.
     *
     * Example: input = "abc_def_email@example.com" → returns "email@example.com"
     */
    public static String extractEmail(String input) {
        String[] parts = input.split("_");
        if (parts.length > 2) {
            return parts[2];
        } else {
            return null;
        }
    }

    /**
     * Extracts a formatted string from an email by taking the
     * first two underscore-separated parts of the local-part of the email,
     * converts them to uppercase, and joins them with a space.
     *
     * Example: "test_000_test000@gmail.com" → "TEST 000"
     */
    public static String extractStringFromEmail(String email) {
        // Get substring before '@'
        String prefix = email.substring(0, email.indexOf("@"));

        // Split by underscores
        String[] parts = prefix.split("_");

        // Build result from first two parts, uppercase and space-separated
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length && i < 2; i++) {
            if (result.length() > 0) {
                result.append(" ");
            }
            result.append(capitalize(parts[i]));
        }

        return result.toString();
    }

    /**
     * Capitalizes the entire input string (to uppercase).
     */
    private static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.toUpperCase();
    }
}