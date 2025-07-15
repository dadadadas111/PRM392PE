package com.example.prm392pe.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Simple background service for demonstrating background tasks
 * Note: In Android 8+ (API 26+), background services are limited
 */
public class BackgroundService extends Service {
    
    private static final String TAG = "BackgroundService";
    private static final int TASK_INTERVAL = 5000; // 5 seconds
    
    private Handler handler;
    private Runnable backgroundTask;
    private boolean isRunning = false;
    private int taskCounter = 0;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "BackgroundService created");
        
        handler = new Handler(Looper.getMainLooper());
        
        // Define the background task
        backgroundTask = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    performBackgroundTask();
                    handler.postDelayed(this, TASK_INTERVAL);
                }
            }
        };
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "BackgroundService started");
        
        if (intent != null) {
            String action = intent.getStringExtra("action");
            if ("STOP".equals(action)) {
                stopSelf();
                return START_NOT_STICKY;
            }
        }
        
        startBackgroundTask();
        
        // START_STICKY: Service will be restarted if killed by system
        // START_NOT_STICKY: Service won't be restarted
        // START_REDELIVER_INTENT: Service will be restarted with the same intent
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // This service doesn't support binding
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "BackgroundService destroyed");
        stopBackgroundTask();
        
        // Show toast on main thread
        handler.post(() -> 
            Toast.makeText(BackgroundService.this, "Background Service stopped", Toast.LENGTH_SHORT).show()
        );
    }
    
    private void startBackgroundTask() {
        if (!isRunning) {
            isRunning = true;
            taskCounter = 0;
            handler.post(backgroundTask);
            Log.d(TAG, "Background task started");
        }
    }
    
    private void stopBackgroundTask() {
        isRunning = false;
        if (handler != null) {
            handler.removeCallbacks(backgroundTask);
        }
        Log.d(TAG, "Background task stopped");
    }
    
    private void performBackgroundTask() {
        taskCounter++;
        Log.d(TAG, "Performing background task #" + taskCounter);
        
        // Simulate some background work
        try {
            // You can perform actual work here like:
            // - Data synchronization
            // - File processing
            // - Network requests
            // - Database operations
            
            // For demo purposes, just log the task
            String message = "Background task #" + taskCounter + " completed";
            Log.i(TAG, message);
            
            // Optionally show toast for first few tasks
            if (taskCounter <= 3) {
                handler.post(() -> 
                    Toast.makeText(BackgroundService.this, message, Toast.LENGTH_SHORT).show()
                );
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error in background task", e);
        }
    }
    
    /**
     * Public method to get current task count (for testing/monitoring)
     */
    public int getTaskCounter() {
        return taskCounter;
    }
    
    /**
     * Check if service is currently running tasks
     */
    public boolean isTaskRunning() {
        return isRunning;
    }
}
