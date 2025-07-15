package com.example.prm392pe.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for file storage operations (internal and external)
 */
public class FileStorageUtils {
    
    private static final String TAG = "FileStorageUtils";
    
    /**
     * Write text to internal storage
     */
    public static boolean writeToInternalStorage(Context context, String fileName, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
            Log.d(TAG, "File written to internal storage: " + fileName);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing to internal storage", e);
            return false;
        }
    }
    
    /**
     * Read text from internal storage
     */
    public static String readFromInternalStorage(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            reader.close();
            fis.close();
            Log.d(TAG, "File read from internal storage: " + fileName);
            return content.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error reading from internal storage", e);
            return null;
        }
    }
    
    /**
     * Write text to external storage (app-specific directory)
     */
    public static boolean writeToExternalStorage(Context context, String fileName, String content) {
        if (!isExternalStorageWritable()) {
            Log.e(TAG, "External storage not writable");
            return false;
        }
        
        try {
            File directory = context.getExternalFilesDir(null);
            File file = new File(directory, fileName);
            
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
            Log.d(TAG, "File written to external storage: " + fileName);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error writing to external storage", e);
            return false;
        }
    }
    
    /**
     * Read text from external storage (app-specific directory)
     */
    public static String readFromExternalStorage(Context context, String fileName) {
        if (!isExternalStorageReadable()) {
            Log.e(TAG, "External storage not readable");
            return null;
        }
        
        try {
            File directory = context.getExternalFilesDir(null);
            File file = new File(directory, fileName);
            
            if (!file.exists()) {
                Log.w(TAG, "File does not exist: " + fileName);
                return null;
            }
            
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            reader.close();
            fis.close();
            Log.d(TAG, "File read from external storage: " + fileName);
            return content.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error reading from external storage", e);
            return null;
        }
    }
    
    /**
     * Delete file from internal storage
     */
    public static boolean deleteFromInternalStorage(Context context, String fileName) {
        return context.deleteFile(fileName);
    }
    
    /**
     * Delete file from external storage
     */
    public static boolean deleteFromExternalStorage(Context context, String fileName) {
        File directory = context.getExternalFilesDir(null);
        File file = new File(directory, fileName);
        return file.delete();
    }
    
    /**
     * Check if external storage is writable
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    
    /**
     * Check if external storage is readable
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || 
               Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    
    /**
     * Get file size
     */
    public static long getFileSize(Context context, String fileName, boolean isExternal) {
        File file;
        if (isExternal) {
            File directory = context.getExternalFilesDir(null);
            file = new File(directory, fileName);
        } else {
            file = new File(context.getFilesDir(), fileName);
        }
        
        return file.exists() ? file.length() : -1;
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(Context context, String fileName, boolean isExternal) {
        File file;
        if (isExternal) {
            File directory = context.getExternalFilesDir(null);
            file = new File(directory, fileName);
        } else {
            file = new File(context.getFilesDir(), fileName);
        }
        
        return file.exists();
    }
}
