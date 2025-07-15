package com.example.prm392pe.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392pe.R;
import com.example.prm392pe.services.BackgroundService;
import com.example.prm392pe.services.BoundService;
import com.example.prm392pe.services.ForegroundService;

import java.util.List;

/**
 * Activity demonstrating different types of Android services:
 * - Background Service
 * - Foreground Service 
 * - Bound Service
 */
public class ServiceDemoActivity extends AppCompatActivity {
    
    private static final String TAG = "ServiceDemoActivity";
    
    // UI Components
    private TextView textViewLogs;
    private ScrollView scrollViewLogs;
    private EditText editTextData;
    
    // Background Service
    private Button btnStartBackground;
    private Button btnStopBackground;
    
    // Foreground Service
    private Button btnStartForeground;
    private Button btnStopForeground;
    
    // Bound Service
    private Button btnBindService;
    private Button btnUnbindService;
    private Button btnAddData;
    private Button btnGetData;
    private Button btnProcessData;
    private Button btnGetStats;
    
    // Bound Service
    private BoundService boundService;
    private boolean isBound = false;
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;
            logMessage("Connected to BoundService");
            updateBoundServiceButtons();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundService = null;
            isBound = false;
            logMessage("Disconnected from BoundService");
            updateBoundServiceButtons();
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_demo);
        
        initViews();
        setupClickListeners();
        logMessage("ServiceDemoActivity created");
    }
    
    private void initViews() {
        // Logs
        textViewLogs = findViewById(R.id.tv_service_logs);
        scrollViewLogs = findViewById(R.id.scroll_logs);
        editTextData = findViewById(R.id.et_service_data);
        
        // Background Service
        btnStartBackground = findViewById(R.id.btn_start_background);
        btnStopBackground = findViewById(R.id.btn_stop_background);
        
        // Foreground Service
        btnStartForeground = findViewById(R.id.btn_start_foreground);
        btnStopForeground = findViewById(R.id.btn_stop_foreground);
        
        // Bound Service
        btnBindService = findViewById(R.id.btn_bind_service);
        btnUnbindService = findViewById(R.id.btn_unbind_service);
        btnAddData = findViewById(R.id.btn_add_data);
        btnGetData = findViewById(R.id.btn_get_data);
        btnProcessData = findViewById(R.id.btn_process_data);
        btnGetStats = findViewById(R.id.btn_get_stats);
        
        updateBoundServiceButtons();
    }
    
    private void setupClickListeners() {
        // Background Service
        btnStartBackground.setOnClickListener(v -> startBackgroundService());
        btnStopBackground.setOnClickListener(v -> stopBackgroundService());
        
        // Foreground Service
        btnStartForeground.setOnClickListener(v -> startForegroundService());
        btnStopForeground.setOnClickListener(v -> stopForegroundService());
        
        // Bound Service
        btnBindService.setOnClickListener(v -> bindToService());
        btnUnbindService.setOnClickListener(v -> unbindFromService());
        btnAddData.setOnClickListener(v -> addDataToService());
        btnGetData.setOnClickListener(v -> getDataFromService());
        btnProcessData.setOnClickListener(v -> processDataInService());
        btnGetStats.setOnClickListener(v -> getServiceStats());
        
        // Clear logs button
        findViewById(R.id.btn_clear_logs).setOnClickListener(v -> clearLogs());
    }
    
    // ================ Background Service Methods ================
    
    private void startBackgroundService() {
        Intent intent = new Intent(this, BackgroundService.class);
        startService(intent);
        logMessage("Started BackgroundService");
        Toast.makeText(this, "Background Service Started", Toast.LENGTH_SHORT).show();
    }
    
    private void stopBackgroundService() {
        Intent intent = new Intent(this, BackgroundService.class);
        intent.putExtra("action", "STOP");
        startService(intent);
        logMessage("Stopping BackgroundService");
    }
    
    // ================ Foreground Service Methods ================
    
    private void startForegroundService() {
        Intent intent = new Intent(this, ForegroundService.class);
        startForegroundService(intent);
        logMessage("Started ForegroundService");
        Toast.makeText(this, "Foreground Service Started (Check notification)", Toast.LENGTH_LONG).show();
    }
    
    private void stopForegroundService() {
        Intent intent = new Intent(this, ForegroundService.class);
        intent.setAction("STOP_FOREGROUND_SERVICE");
        startService(intent);
        logMessage("Stopping ForegroundService");
    }
    
    // ================ Bound Service Methods ================
    
    private void bindToService() {
        Intent intent = new Intent(this, BoundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        logMessage("Binding to BoundService...");
    }
    
    private void unbindFromService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
            boundService = null;
            logMessage("Unbound from BoundService");
            updateBoundServiceButtons();
        }
    }
    
    private void addDataToService() {
        if (!isBound || boundService == null) {
            showToast("Service not bound");
            return;
        }
        
        String data = editTextData.getText().toString().trim();
        if (data.isEmpty()) {
            data = "Sample Data " + System.currentTimeMillis();
        }
        
        boundService.addData(data);
        editTextData.setText("");
        logMessage("Added data: " + data);
        showToast("Data added to service");
    }
    
    private void getDataFromService() {
        if (!isBound || boundService == null) {
            showToast("Service not bound");
            return;
        }
        
        List<String> data = boundService.getAllData();
        logMessage("Retrieved " + data.size() + " items from service:");
        for (int i = 0; i < data.size(); i++) {
            logMessage("  " + (i + 1) + ". " + data.get(i));
        }
    }
    
    private void processDataInService() {
        if (!isBound || boundService == null) {
            showToast("Service not bound");
            return;
        }
        
        if (boundService.isProcessing()) {
            showToast("Service is already processing data");
            return;
        }
        
        logMessage("Starting async data processing...");
        boundService.processDataAsync(new BoundService.DataProcessingCallback() {
            @Override
            public void onSuccess(List<String> processedData) {
                logMessage("Data processing completed:");
                for (int i = 0; i < Math.min(processedData.size(), 5); i++) {
                    logMessage("  " + (i + 1) + ". " + processedData.get(i));
                }
                if (processedData.size() > 5) {
                    logMessage("  ... and " + (processedData.size() - 5) + " more items");
                }
                showToast("Data processing completed");
            }
            
            @Override
            public void onError(String error) {
                logMessage("Data processing error: " + error);
                showToast("Processing error: " + error);
            }
        });
    }
    
    private void getServiceStats() {
        if (!isBound || boundService == null) {
            showToast("Service not bound");
            return;
        }
        
        BoundService.ServiceStats stats = boundService.getStats();
        logMessage("Service Statistics:");
        logMessage("  Data Count: " + stats.dataCount);
        logMessage("  Operations: " + stats.operationCount);
        logMessage("  Processing: " + stats.isProcessing);
    }
    
    // ================ Helper Methods ================
    
    private void updateBoundServiceButtons() {
        btnUnbindService.setEnabled(isBound);
        btnAddData.setEnabled(isBound);
        btnGetData.setEnabled(isBound);
        btnProcessData.setEnabled(isBound);
        btnGetStats.setEnabled(isBound);
        btnBindService.setEnabled(!isBound);
    }
    
    private void logMessage(String message) {
        String timestamp = java.text.DateFormat.getTimeInstance().format(new java.util.Date());
        String logEntry = "[" + timestamp + "] " + message;
        
        runOnUiThread(() -> {
            textViewLogs.append(logEntry + "\n");
            // Auto-scroll to bottom
            scrollViewLogs.post(() -> scrollViewLogs.fullScroll(ScrollView.FOCUS_DOWN));
        });
        
        Log.d(TAG, message);
    }
    
    private void clearLogs() {
        textViewLogs.setText("");
        logMessage("Logs cleared");
    }
    
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Unbind from service to prevent memory leaks
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        
        logMessage("ServiceDemoActivity destroyed");
    }
}
