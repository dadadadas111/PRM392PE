package com.example.prm392pe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility class for SharedPreferences operations
 * Supports all primitive types and Serializable objects
 */
public class SharedPreferencesUtils {
    
    private static final String TAG = "SharedPreferencesUtils";
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
    
    // ==================== OBJECT SERIALIZATION METHODS ====================
    
    /**
     * Save a Serializable object to SharedPreferences
     * The object is serialized to bytes and then encoded to Base64 string
     * 
     * @param key The key to store the object under
     * @param object The Serializable object to save
     * @return true if successful, false if failed
     */
    public boolean saveObject(String key, Serializable object) {
        if (object == null) {
            remove(key);
            return true;
        }
        
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();
            
            String serializedObject = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            editor.putString(key, serializedObject);
            editor.apply();
            
            Log.d(TAG, "Object saved successfully with key: " + key);
            return true;
            
        } catch (IOException e) {
            Log.e(TAG, "Failed to save object with key: " + key, e);
            return false;
        }
    }
    
    /**
     * Retrieve a Serializable object from SharedPreferences
     * The Base64 string is decoded and deserialized back to object
     * 
     * @param key The key where the object is stored
     * @param defaultValue The default value to return if object not found or deserialization fails
     * @param <T> The type of object to retrieve
     * @return The deserialized object or defaultValue
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getObject(String key, T defaultValue) {
        String serializedObject = sharedPreferences.getString(key, null);
        
        if (serializedObject == null) {
            Log.d(TAG, "No object found with key: " + key);
            return defaultValue;
        }
        
        try {
            byte[] data = Base64.decode(serializedObject, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            
            T object = (T) ois.readObject();
            ois.close();
            
            Log.d(TAG, "Object retrieved successfully with key: " + key);
            return object;
            
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Log.e(TAG, "Failed to retrieve object with key: " + key, e);
            return defaultValue;
        }
    }
    
    /**
     * Save a Serializable object with automatic type detection
     * Convenience method that doesn't require explicit type casting
     * 
     * @param key The key to store the object under
     * @param object The object to save
     * @return true if successful, false if failed
     */
    public boolean saveTypedObject(String key, Object object) {
        if (!(object instanceof Serializable)) {
            Log.e(TAG, "Object must implement Serializable interface");
            return false;
        }
        return saveObject(key, (Serializable) object);
    }
    
    /**
     * Retrieve object with Class type for better type safety
     * 
     * @param key The key where the object is stored
     * @param clazz The Class type of the expected object
     * @param <T> The type of object to retrieve
     * @return The deserialized object or null if not found/failed
     */
    public <T extends Serializable> T getObject(String key, Class<T> clazz) {
        String serializedObject = sharedPreferences.getString(key, null);
        
        if (serializedObject == null) {
            Log.d(TAG, "No object found with key: " + key);
            return null;
        }
        
        try {
            byte[] data = Base64.decode(serializedObject, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            
            Object object = ois.readObject();
            ois.close();
            
            if (clazz.isInstance(object)) {
                Log.d(TAG, "Object retrieved successfully with key: " + key);
                return clazz.cast(object);
            } else {
                Log.e(TAG, "Object type mismatch for key: " + key);
                return null;
            }
            
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Failed to retrieve object with key: " + key, e);
            return null;
        }
    }
    
    // ==================== BATCH OPERATIONS FOR OBJECTS ====================
    
    /**
     * Save multiple objects at once
     * More efficient than calling saveObject multiple times
     * 
     * @param objectMap Map of key-value pairs where values are Serializable objects
     * @return true if all objects saved successfully, false otherwise
     */
    public boolean saveObjects(java.util.Map<String, Serializable> objectMap) {
        if (objectMap == null || objectMap.isEmpty()) {
            return true;
        }
        
        boolean allSuccessful = true;
        
        // Start batch edit
        SharedPreferences.Editor batchEditor = sharedPreferences.edit();
        
        for (java.util.Map.Entry<String, Serializable> entry : objectMap.entrySet()) {
            String key = entry.getKey();
            Serializable object = entry.getValue();
            
            if (object == null) {
                batchEditor.remove(key);
                continue;
            }
            
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                oos.close();
                
                String serializedObject = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                batchEditor.putString(key, serializedObject);
                
            } catch (IOException e) {
                Log.e(TAG, "Failed to serialize object with key: " + key, e);
                allSuccessful = false;
            }
        }
        
        batchEditor.apply();
        Log.d(TAG, "Batch save completed. All successful: " + allSuccessful);
        return allSuccessful;
    }
    
    /**
     * Get the size of a stored object in bytes
     * Useful for monitoring storage usage
     * 
     * @param key The key of the stored object
     * @return Size in bytes, or -1 if object not found
     */
    public long getObjectSize(String key) {
        String serializedObject = sharedPreferences.getString(key, null);
        if (serializedObject == null) {
            return -1;
        }
        
        try {
            byte[] data = Base64.decode(serializedObject, Base64.DEFAULT);
            return data.length;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Failed to decode object for size calculation: " + key, e);
            return -1;
        }
    }
    
    /*
     * USAGE EXAMPLES:
     * 
     * // Save custom objects
     * User user = new User("John", "john@email.com");
     * prefsUtils.saveObject("current_user", user);
     * 
     * // Retrieve objects
     * User savedUser = prefsUtils.getObject("current_user", new User());
     * // OR with type safety
     * User savedUser = prefsUtils.getObject("current_user", User.class);
     * 
     * // Save collections
     * ArrayList<String> items = new ArrayList<>();
     * items.add("item1");
     * items.add("item2");
     * prefsUtils.saveObject("item_list", items);
     * 
     * // Retrieve collections
     * ArrayList<String> savedItems = prefsUtils.getObject("item_list", new ArrayList<>());
     * 
     * // Batch operations
     * Map<String, Serializable> objects = new HashMap<>();
     * objects.put("user", user);
     * objects.put("settings", settingsObject);
     * prefsUtils.saveObjects(objects);
     * 
     * NOTE: Objects must implement Serializable interface
     * For complex objects, consider using JSON serialization instead
     * Large objects may impact app performance - use wisely
     */
}
