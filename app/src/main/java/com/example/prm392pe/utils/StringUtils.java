package com.example.prm392pe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Utility class for string operations and manipulations
 */
public class StringUtils {
    
    private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();
    
    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Check if string is null, empty, or only whitespace
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
    
    /**
     * Get safe string (returns empty string if null)
     */
    public static String safe(String str) {
        return str != null ? str : "";
    }
    
    /**
     * Get safe string with default value
     */
    public static String safe(String str, String defaultValue) {
        return str != null ? str : defaultValue;
    }
    
    /**
     * Capitalize first letter of string
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    /**
     * Capitalize first letter of each word
     */
    public static String capitalizeWords(String str) {
        if (isEmpty(str)) return str;
        
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            result.append(capitalize(words[i]));
        }
        
        return result.toString();
    }
    
    /**
     * Reverse a string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) return str;
        return new StringBuilder(str).reverse().toString();
    }
    
    /**
     * Count occurrences of substring in string
     */
    public static int countOccurrences(String str, String substring) {
        if (isEmpty(str) || isEmpty(substring)) return 0;
        
        int count = 0;
        int index = 0;
        
        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }
    
    /**
     * Remove all whitespace from string
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("\\s", "");
    }
    
    /**
     * Remove extra whitespace (multiple spaces become single space)
     */
    public static String normalizeWhitespace(String str) {
        if (isEmpty(str)) return str;
        return str.replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Truncate string to max length with ellipsis
     */
    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }
    
    /**
     * Truncate string to max length with custom suffix
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (isEmpty(str) || str.length() <= maxLength) return str;
        
        int truncateLength = maxLength - suffix.length();
        if (truncateLength <= 0) return suffix;
        
        return str.substring(0, truncateLength) + suffix;
    }
    
    /**
     * Pad string to left with character
     */
    public static String padLeft(String str, int length, char padChar) {
        if (str == null) str = "";
        if (str.length() >= length) return str;
        
        StringBuilder sb = new StringBuilder();
        for (int i = str.length(); i < length; i++) {
            sb.append(padChar);
        }
        sb.append(str);
        
        return sb.toString();
    }
    
    /**
     * Pad string to right with character
     */
    public static String padRight(String str, int length, char padChar) {
        if (str == null) str = "";
        if (str.length() >= length) return str;
        
        StringBuilder sb = new StringBuilder(str);
        for (int i = str.length(); i < length; i++) {
            sb.append(padChar);
        }
        
        return sb.toString();
    }
    
    /**
     * Generate random string of given length
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_NUMERIC.charAt(random.nextInt(ALPHA_NUMERIC.length())));
        }
        return sb.toString();
    }
    
    /**
     * Generate MD5 hash of string
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Check if string contains only digits
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) return false;
        return str.matches("\\d+");
    }
    
    /**
     * Check if string contains only letters
     */
    public static boolean isAlpha(String str) {
        if (isEmpty(str)) return false;
        return str.matches("[a-zA-Z]+");
    }
    
    /**
     * Check if string contains only letters and digits
     */
    public static boolean isAlphaNumeric(String str) {
        if (isEmpty(str)) return false;
        return str.matches("[a-zA-Z0-9]+");
    }
    
    /**
     * Extract numbers from string
     */
    public static String extractNumbers(String str) {
        if (isEmpty(str)) return "";
        return str.replaceAll("[^0-9]", "");
    }
    
    /**
     * Extract letters from string
     */
    public static String extractLetters(String str) {
        if (isEmpty(str)) return "";
        return str.replaceAll("[^a-zA-Z]", "");
    }
    
    /**
     * Convert string to camelCase
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) return str;
        
        String[] words = str.toLowerCase().split("[\\s_-]+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i == 0) {
                result.append(words[i]);
            } else {
                result.append(capitalize(words[i]));
            }
        }
        
        return result.toString();
    }
    
    /**
     * Convert string to snake_case
     */
    public static String toSnakeCase(String str) {
        if (isEmpty(str)) return str;
        
        return str.replaceAll("([a-z])([A-Z])", "$1_$2")
                  .replaceAll("[\\s-]+", "_")
                  .toLowerCase();
    }
}
