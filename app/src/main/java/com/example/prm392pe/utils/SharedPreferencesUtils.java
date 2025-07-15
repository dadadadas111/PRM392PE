package com.example.prm392pe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class for SharedPreferences operations
 */
public class SharedPreferencesUtils {
    
    private static final String PREF_NAME = "app_preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    public SharedPreferencesUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    
    /**
     * Save string value
     */
    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    
    /**
     * Get string value
     */
    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    
    /**
     * Save integer value
     */
    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }
    
    /**
     * Get integer value
     */
    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    
    /**
     * Save boolean value
     */
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    /**
     * Get boolean value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
    
    /**
     * Save float value
     */
    public void saveFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }
    
    /**
     * Get float value
     */
    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }
    
    /**
     * Save long value
     */
    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }
    
    /**
     * Get long value
     */
    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }
    
    /**
     * Remove a key
     */
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }
    
    /**
     * Clear all preferences
     */
    public void clear() {
        editor.clear();
        editor.apply();
    }
    
    /**
     * Check if key exists
     */
    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }
}
