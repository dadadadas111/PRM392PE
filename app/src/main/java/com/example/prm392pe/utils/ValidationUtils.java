package com.example.prm392pe.utils;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

/**
 * Utility class for various validation operations
 */
public class ValidationUtils {
    
    // Current password pattern: Strong password with all character types
    // ^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,}$
    // - At least 8 characters
    // - At least 1 digit
    // - At least 1 lowercase letter
    // - At least 1 uppercase letter  
    // - At least 1 special character from [@#$%^&+=]
    // - No whitespace allowed
    private static final Pattern PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    
    /* Alternative PASSWORD_PATTERN Examples:
     * 
     * 1. BASIC PASSWORD (minimum 6 characters, any characters allowed):
     * Pattern.compile("^.{6,}$")
     * 
     * 2. ALPHANUMERIC ONLY (8+ chars, letters and numbers only):
     * Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$")
     * 
     * 3. MEDIUM STRENGTH (8+ chars, at least 1 letter and 1 number):
     * Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{8,}$")
     * 
     * 4. STRONG WITH FLEXIBLE SPECIAL CHARS (any non-alphanumeric as special):
     * Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$")
     * 
     * 5. ENTERPRISE LEVEL (12+ chars, all types, specific special chars):
     * Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{12,}$")
     * 
     * 6. NO CONSECUTIVE REPEATING CHARS (prevents "aaa", "111", etc.):
     * Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?!.*(.)\\1{2,}).{8,}$")
     * 
     * 7. MUST START WITH LETTER (common in some systems):
     * Pattern.compile("^[a-zA-Z](?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{7,}$")
     * 
     * 8. MODERATE (6-20 chars, at least 1 letter, 1 number, optional special):
     * Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9!@#$%^&*]{6,20}$")
     * 
     * Regex Components Explanation:
     * ^ = Start of string
     * $ = End of string
     * (?=.*[0-9]) = Positive lookahead for at least one digit
     * (?=.*[a-z]) = Positive lookahead for at least one lowercase letter
     * (?=.*[A-Z]) = Positive lookahead for at least one uppercase letter
     * (?=.*[@#$%^&+=]) = Positive lookahead for at least one special character
     * (?=\S+$) = Positive lookahead ensuring no whitespace (all non-whitespace chars)
     * .{8,} = Any character, minimum 8 times
     * .{6,20} = Any character, between 6 and 20 times
     * [a-zA-Z0-9] = Character class: any letter or digit
     * (?!.*(.)\\1{2,}) = Negative lookahead preventing 3+ consecutive same characters
     * [^a-zA-Z0-9] = Negated character class: any non-alphanumeric character
     */
    
    /**
     * Check if email is valid
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Check if phone number is valid
     */
    public static boolean isValidPhoneNumber(String phone) {
        return !TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches() && phone.length() >= 10;
    }
    
    /**
     * Check if password meets criteria
     * At least 8 characters, 1 digit, 1 lowercase, 1 uppercase, 1 special char
     */
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Check if string is not null or empty
     */
    public static boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text) && !text.trim().isEmpty();
    }
    
    /**
     * Check if number is within range
     */
    public static boolean isNumberInRange(int number, int min, int max) {
        return number >= min && number <= max;
    }
    
    /**
     * Check if URL is valid
     */
    public static boolean isValidUrl(String url) {
        return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }
    
    /**
     * Validate credit card number using Luhn algorithm
     */
    public static boolean isValidCreditCard(String cardNumber) {
        if (TextUtils.isEmpty(cardNumber)) return false;
        
        cardNumber = cardNumber.replaceAll("\\s", "");
        
        if (cardNumber.length() < 13 || cardNumber.length() > 19) return false;
        
        int sum = 0;
        boolean alternate = false;
        
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
}
