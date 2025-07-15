package com.example.prm392pe.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.prm392pe.MainActivity;
import com.example.prm392pe.R;

/**
 * Foreground service for long-running tasks with persistent notification
 * Required for tasks that need to continue running even when app is in background
 */
public class ForegroundService extends Service {
    
    private static final String TAG = "ForegroundService";
    private static final int NOTIFICATION_ID = 1001;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int TASK_INTERVAL = 3000; // 3 seconds
    
    private Handler handler;
    private Runnable foregroundTask;
    private boolean isRunning = false;
    private int progressCounter = 0;
    private NotificationManager notificationManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ForegroundService created");
        
        handler = new Handler(Looper.getMainLooper());
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        
        createNotificationChannel();
        
        // Define the foreground task
        foregroundTask = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    performForegroundTask();
                    updateNotification();
                    handler.postDelayed(this, TASK_INTERVAL);
                }
            }
        };
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "ForegroundService started");
        
        if (intent != null) {
            String action = intent.getAction();
            if ("STOP_FOREGROUND_SERVICE".equals(action)) {
                stopForeground(true);
                stopSelf();
                return START_NOT_STICKY;
            }
        }
        
        // Start as foreground service
        startForeground(NOTIFICATION_ID, createNotification());
        startForegroundTask();
        
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ForegroundService destroyed");
        stopForegroundTask();
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Channel for foreground service notifications");
            channel.setShowBadge(false);
            
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    
    private Notification createNotification() {
        // Intent to open main activity when notification is tapped
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
        
        // Intent to stop the service
        Intent stopIntent = new Intent(this, ForegroundService.class);
        stopIntent.setAction("STOP_FOREGROUND_SERVICE");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,
                PendingIntent.FLAG_IMMUTABLE);
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("PRM392PE Service Running")
                .setContentText("Performing background tasks... Progress: " + progressCounter)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop", stopPendingIntent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }
    
    private void updateNotification() {
        Notification notification = createNotification();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
    
    private void startForegroundTask() {
        if (!isRunning) {
            isRunning = true;
            progressCounter = 0;
            handler.post(foregroundTask);
            Log.d(TAG, "Foreground task started");
        }
    }
    
    private void stopForegroundTask() {
        isRunning = false;
        if (handler != null) {
            handler.removeCallbacks(foregroundTask);
        }
        Log.d(TAG, "Foreground task stopped");
    }
    
    private void performForegroundTask() {
        progressCounter++;
        Log.d(TAG, "Performing foreground task #" + progressCounter);
        
        // Simulate some long-running work
        try {
            // Examples of foreground service tasks:
            // - File downloads
            // - Music playback
            // - Location tracking
            // - Data backup/sync
            // - Image/video processing
            
            String message = "Foreground task #" + progressCounter + " - Processing data...";
            Log.i(TAG, message);
            
            // Simulate work completion after 10 tasks
            if (progressCounter >= 10) {
                Log.i(TAG, "Foreground service work completed, stopping service");
                stopForeground(true);
                stopSelf();
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error in foreground task", e);
        }
    }
    
    /**
     * Get current progress
     */
    public int getProgress() {
        return progressCounter;
    }
    
    /**
     * Check if service is running
     */
    public boolean isTaskRunning() {
        return isRunning;
    }
}
