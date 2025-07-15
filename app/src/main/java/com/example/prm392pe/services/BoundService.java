package com.example.prm392pe.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Bound service for communication between components
 * Allows other components to bind and interact with the service
 */
public class BoundService extends Service {
    
    private static final String TAG = "BoundService";
    
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    
    // Data and state
    private List<String> dataList = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isProcessing = false;
    private int operationCount = 0;
    
    /**
     * Class used for the client Binder
     */
    public class LocalBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BoundService created");
        initializeData();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Client bound to service");
        return binder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "Client unbound from service");
        return true; // Allow rebinding
    }
    
    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "Client rebound to service");
        super.onRebind(intent);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BoundService destroyed");
    }
    
    // ================ Public methods for bound clients ================
    
    /**
     * Add data to the service
     */
    public void addData(String data) {
        dataList.add(data);
        operationCount++;
        Log.d(TAG, "Data added: " + data + " (Total: " + dataList.size() + ")");
    }
    
    /**
     * Get all data from the service
     */
    public List<String> getAllData() {
        return new ArrayList<>(dataList);
    }
    
    /**
     * Remove data from the service
     */
    public boolean removeData(String data) {
        boolean removed = dataList.remove(data);
        if (removed) {
            operationCount++;
            Log.d(TAG, "Data removed: " + data + " (Total: " + dataList.size() + ")");
        }
        return removed;
    }
    
    /**
     * Clear all data
     */
    public void clearData() {
        int count = dataList.size();
        dataList.clear();
        operationCount++;
        Log.d(TAG, "Cleared " + count + " items");
    }
    
    /**
     * Get data count
     */
    public int getDataCount() {
        return dataList.size();
    }
    
    /**
     * Get operation count
     */
    public int getOperationCount() {
        return operationCount;
    }
    
    /**
     * Process data asynchronously
     */
    public void processDataAsync(DataProcessingCallback callback) {
        if (isProcessing) {
            if (callback != null) {
                callback.onError("Service is already processing data");
            }
            return;
        }
        
        isProcessing = true;
        Log.d(TAG, "Starting async data processing...");
        
        // Simulate processing in background thread
        new Thread(() -> {
            try {
                // Simulate processing time
                Thread.sleep(2000);
                
                // Process each item
                List<String> processedData = new ArrayList<>();
                for (String item : dataList) {
                    processedData.add("Processed: " + item);
                }
                
                // Return result on main thread
                handler.post(() -> {
                    isProcessing = false;
                    operationCount++;
                    Log.d(TAG, "Data processing completed");
                    
                    if (callback != null) {
                        callback.onSuccess(processedData);
                    }
                });
                
            } catch (InterruptedException e) {
                handler.post(() -> {
                    isProcessing = false;
                    Log.e(TAG, "Data processing interrupted", e);
                    
                    if (callback != null) {
                        callback.onError("Processing was interrupted");
                    }
                });
            }
        }).start();
    }
    
    /**
     * Check if service is currently processing
     */
    public boolean isProcessing() {
        return isProcessing;
    }
    
    /**
     * Search for data containing query
     */
    public List<String> searchData(String query) {
        List<String> results = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            String lowerQuery = query.toLowerCase();
            for (String item : dataList) {
                if (item.toLowerCase().contains(lowerQuery)) {
                    results.add(item);
                }
            }
        }
        Log.d(TAG, "Search for '" + query + "' returned " + results.size() + " results");
        return results;
    }
    
    /**
     * Get service statistics
     */
    public ServiceStats getStats() {
        return new ServiceStats(dataList.size(), operationCount, isProcessing);
    }
    
    // ================ Private methods ================
    
    private void initializeData() {
        // Initialize with some sample data
        dataList.add("Sample Item 1");
        dataList.add("Sample Item 2");
        dataList.add("Sample Item 3");
        Log.d(TAG, "Service initialized with " + dataList.size() + " items");
    }
    
    // ================ Callback interfaces ================
    
    /**
     * Callback interface for async operations
     */
    public interface DataProcessingCallback {
        void onSuccess(List<String> processedData);
        void onError(String error);
    }
    
    /**
     * Data class for service statistics
     */
    public static class ServiceStats {
        public final int dataCount;
        public final int operationCount;
        public final boolean isProcessing;
        
        public ServiceStats(int dataCount, int operationCount, boolean isProcessing) {
            this.dataCount = dataCount;
            this.operationCount = operationCount;
            this.isProcessing = isProcessing;
        }
        
        @Override
        public String toString() {
            return "ServiceStats{" +
                    "dataCount=" + dataCount +
                    ", operationCount=" + operationCount +
                    ", isProcessing=" + isProcessing +
                    '}';
        }
    }
}
