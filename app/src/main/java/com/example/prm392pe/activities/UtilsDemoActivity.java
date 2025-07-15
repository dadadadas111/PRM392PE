package com.example.prm392pe.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.prm392pe.R;
import com.example.prm392pe.utils.FileStorageUtils;
import com.example.prm392pe.utils.ImageUtils;
import com.example.prm392pe.utils.IntentUtils;
import com.example.prm392pe.utils.SharedPreferencesUtils;
import com.example.prm392pe.utils.UIUtils;
import com.example.prm392pe.utils.ValidationUtils;

import java.io.IOException;

/**
 * Activity demonstrating image handling, intents, file storage, and SharedPreferences
 */
public class UtilsDemoActivity extends AppCompatActivity {
    
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private ImageView selectedImageView;
    private EditText editTextData;
    private TextView textViewFileContent;
    private TextView textViewPreferences;
    
    private SharedPreferencesUtils prefsUtils;
    private Uri selectedImageUri;
    
    // Activity result launchers
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utils_demo);
        
        initViews();
        initUtils();
        setupActivityResultLaunchers();
        setupClickListeners();
        loadPreferencesData();
    }
    
    private void initViews() {
        selectedImageView = findViewById(R.id.img_selected);
        editTextData = findViewById(R.id.et_data);
        textViewFileContent = findViewById(R.id.tv_file_content);
        textViewPreferences = findViewById(R.id.tv_preferences);
    }
    
    private void initUtils() {
        prefsUtils = new SharedPreferencesUtils(this);
    }
    
    private void setupActivityResultLaunchers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        loadImageFromUri(selectedImageUri);
                    }
                });
        
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        selectedImageView.setImageBitmap(imageBitmap);
                        
                        // Convert to base64 and show toast
                        String base64 = ImageUtils.bitmapToBase64(imageBitmap);
                        UIUtils.showToast(this, "Image captured and converted to base64 (length: " + base64.length() + ")");
                    }
                });
    }
    
    private void setupClickListeners() {
        // Image selection buttons
        findViewById(R.id.btn_select_gallery).setOnClickListener(v -> selectImageFromGallery());
        findViewById(R.id.btn_capture_camera).setOnClickListener(v -> captureImageFromCamera());
        
        // File storage buttons
        findViewById(R.id.btn_save_internal).setOnClickListener(v -> saveToInternalStorage());
        findViewById(R.id.btn_load_internal).setOnClickListener(v -> loadFromInternalStorage());
        findViewById(R.id.btn_save_external).setOnClickListener(v -> saveToExternalStorage());
        findViewById(R.id.btn_load_external).setOnClickListener(v -> loadFromExternalStorage());
        
        // SharedPreferences buttons
        findViewById(R.id.btn_save_prefs).setOnClickListener(v -> saveToPreferences());
        findViewById(R.id.btn_load_prefs).setOnClickListener(v -> loadPreferencesData());
        findViewById(R.id.btn_clear_prefs).setOnClickListener(v -> clearPreferences());
        
        // Intent buttons
        findViewById(R.id.btn_share_text).setOnClickListener(v -> shareText());
        findViewById(R.id.btn_share_image).setOnClickListener(v -> shareImage());
        findViewById(R.id.btn_open_web).setOnClickListener(v -> openWebsite());
        findViewById(R.id.btn_send_email).setOnClickListener(v -> sendEmail());
        
        // Validation button
        findViewById(R.id.btn_validate).setOnClickListener(v -> validateInput());
    }
    
    private void selectImageFromGallery() {
        Intent intent = IntentUtils.createPickImageIntent();
        galleryLauncher.launch(intent);
    }
    
    private void captureImageFromCamera() {
        if (checkCameraPermission()) {
            Intent intent = IntentUtils.createCaptureImageIntent();
            cameraLauncher.launch(intent);
        } else {
            requestCameraPermission();
        }
    }
    
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImageFromCamera();
            } else {
                UIUtils.showToast(this, "Camera permission denied");
            }
        }
    }
    
    private void loadImageFromUri(Uri uri) {
        Glide.with(this)
                .load(uri)
                .into(selectedImageView);
        
        // Demonstrate image utilities
        Bitmap bitmap = ImageUtils.getBitmapFromUri(this, uri);
        if (bitmap != null) {
            Bitmap resized = ImageUtils.resizeBitmap(bitmap, 200, 200);
            String base64 = ImageUtils.bitmapToBase64(resized);
            UIUtils.showToast(this, "Image loaded and processed (base64 length: " + base64.length() + ")");
        }
    }
    
    private void saveToInternalStorage() {
        String data = editTextData.getText().toString();
        if (ValidationUtils.isNotEmpty(data)) {
            boolean success = FileStorageUtils.writeToInternalStorage(this, "test_file.txt", data);
            UIUtils.showToast(this, success ? "Saved to internal storage" : "Failed to save");
        } else {
            UIUtils.showToast(this, "Please enter some data");
        }
    }
    
    private void loadFromInternalStorage() {
        String content = FileStorageUtils.readFromInternalStorage(this, "test_file.txt");
        if (content != null) {
            textViewFileContent.setText("Internal Storage Content:\n" + content);
        } else {
            textViewFileContent.setText("No data found in internal storage");
        }
    }
    
    private void saveToExternalStorage() {
        String data = editTextData.getText().toString();
        if (ValidationUtils.isNotEmpty(data)) {
            boolean success = FileStorageUtils.writeToExternalStorage(this, "test_file_external.txt", data);
            UIUtils.showToast(this, success ? "Saved to external storage" : "Failed to save");
        } else {
            UIUtils.showToast(this, "Please enter some data");
        }
    }
    
    private void loadFromExternalStorage() {
        String content = FileStorageUtils.readFromExternalStorage(this, "test_file_external.txt");
        if (content != null) {
            textViewFileContent.setText("External Storage Content:\n" + content);
        } else {
            textViewFileContent.setText("No data found in external storage");
        }
    }
    
    private void saveToPreferences() {
        String data = editTextData.getText().toString();
        if (ValidationUtils.isNotEmpty(data)) {
            prefsUtils.saveString("user_data", data);
            prefsUtils.saveInt("save_count", prefsUtils.getInt("save_count", 0) + 1);
            prefsUtils.saveLong("last_save_time", System.currentTimeMillis());
            prefsUtils.saveBoolean("has_data", true);
            
            UIUtils.showToast(this, "Saved to SharedPreferences");
            loadPreferencesData();
        } else {
            UIUtils.showToast(this, "Please enter some data");
        }
    }
    
    private void loadPreferencesData() {
        String userData = prefsUtils.getString("user_data", "No data");
        int saveCount = prefsUtils.getInt("save_count", 0);
        long lastSaveTime = prefsUtils.getLong("last_save_time", 0);
        boolean hasData = prefsUtils.getBoolean("has_data", false);
        
        String prefsContent = "User Data: " + userData + "\n" +
                "Save Count: " + saveCount + "\n" +
                "Last Save: " + (lastSaveTime > 0 ? new java.util.Date(lastSaveTime).toString() : "Never") + "\n" +
                "Has Data: " + hasData;
        
        textViewPreferences.setText(prefsContent);
    }
    
    private void clearPreferences() {
        prefsUtils.clear();
        loadPreferencesData();
        UIUtils.showToast(this, "Preferences cleared");
    }
    
    private void shareText() {
        String data = editTextData.getText().toString();
        if (ValidationUtils.isNotEmpty(data)) {
            Intent intent = IntentUtils.createShareTextIntent(data, "Share Text");
            startActivity(intent);
        } else {
            UIUtils.showToast(this, "Please enter some text to share");
        }
    }
    
    private void shareImage() {
        if (selectedImageUri != null) {
            Intent intent = IntentUtils.createShareImageIntent(selectedImageUri, "Share Image");
            startActivity(intent);
        } else {
            UIUtils.showToast(this, "Please select an image first");
        }
    }
    
    private void openWebsite() {
        Intent intent = IntentUtils.createWebIntent("https://developer.android.com");
        startActivity(intent);
    }
    
    private void sendEmail() {
        String[] recipients = {"test@example.com"};
        Intent intent = IntentUtils.createEmailIntent(recipients, "Test Subject", "Hello from PRM392PE app!");
        try {
            startActivity(intent);
        } catch (Exception e) {
            UIUtils.showToast(this, "No email app found");
        }
    }
    
    private void validateInput() {
        String data = editTextData.getText().toString();
        
        StringBuilder validationResult = new StringBuilder("Validation Results:\n");
        
        // Check if not empty
        validationResult.append("Not Empty: ").append(ValidationUtils.isNotEmpty(data)).append("\n");
        
        // Check if valid email
        validationResult.append("Valid Email: ").append(ValidationUtils.isValidEmail(data)).append("\n");
        
        // Check if valid phone
        validationResult.append("Valid Phone: ").append(ValidationUtils.isValidPhoneNumber(data)).append("\n");
        
        // Check if valid URL
        validationResult.append("Valid URL: ").append(ValidationUtils.isValidUrl(data)).append("\n");
        
        // Check if valid password
        validationResult.append("Valid Password: ").append(ValidationUtils.isValidPassword(data)).append("\n");
        
        textViewFileContent.setText(validationResult.toString());
    }
}
