package com.example.prm392pe.utils;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

/**
 * Utility class for various validation operations
 */
public class ValidationUtils {
    
    private static final Pattern PASSWORD_PATTERN = 
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    
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
