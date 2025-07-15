package com.example.prm392pe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm392pe.R;
import com.example.prm392pe.utils.SharedPreferencesUtils;
import com.example.prm392pe.utils.UIUtils;
import com.example.prm392pe.utils.ValidationUtils;

/**
 * Fragment demonstrating form handling and preferences
 */
public class SettingsFragment extends Fragment {
    
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private RadioGroup radioGroupTheme;
    private CheckBox checkBoxNotifications;
    private CheckBox checkBoxAutoSave;
    private Spinner spinnerLanguage;
    private TextView textViewStatus;
    
    private SharedPreferencesUtils prefsUtils;
    
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initUtils();
        setupClickListeners();
        loadSettings();
    }
    
    private void initViews(View view) {
        editTextName = view.findViewById(R.id.et_name);
        editTextEmail = view.findViewById(R.id.et_email);
        editTextPhone = view.findViewById(R.id.et_phone);
        radioGroupTheme = view.findViewById(R.id.rg_theme);
        checkBoxNotifications = view.findViewById(R.id.cb_notifications);
        checkBoxAutoSave = view.findViewById(R.id.cb_auto_save);
        spinnerLanguage = view.findViewById(R.id.spinner_language);
        textViewStatus = view.findViewById(R.id.tv_status);
    }
    
    private void initUtils() {
        prefsUtils = new SharedPreferencesUtils(requireContext());
    }
    
    private void setupClickListeners() {
        Button btnSave = getView().findViewById(R.id.btn_save_settings);
        Button btnReset = getView().findViewById(R.id.btn_reset_settings);
        Button btnValidate = getView().findViewById(R.id.btn_validate_form);
        
        btnSave.setOnClickListener(v -> saveSettings());
        btnReset.setOnClickListener(v -> resetSettings());
        btnValidate.setOnClickListener(v -> validateForm());
    }
    
    private void loadSettings() {
        // Load user info
        editTextName.setText(prefsUtils.getString("user_name", ""));
        editTextEmail.setText(prefsUtils.getString("user_email", ""));
        editTextPhone.setText(prefsUtils.getString("user_phone", ""));
        
        // Load theme preference
        String theme = prefsUtils.getString("theme", "light");
        if ("dark".equals(theme)) {
            ((RadioButton) getView().findViewById(R.id.rb_dark)).setChecked(true);
        } else {
            ((RadioButton) getView().findViewById(R.id.rb_light)).setChecked(true);
        }
        
        // Load boolean preferences
        checkBoxNotifications.setChecked(prefsUtils.getBoolean("notifications_enabled", true));
        checkBoxAutoSave.setChecked(prefsUtils.getBoolean("auto_save_enabled", false));
        
        // Load language preference
        int languageIndex = prefsUtils.getInt("language_index", 0);
        spinnerLanguage.setSelection(languageIndex);
        
        updateStatus("Settings loaded");
    }
    
    private void saveSettings() {
        // Validate before saving
        if (!validateForm()) {
            return;
        }
        
        // Save user info
        prefsUtils.saveString("user_name", editTextName.getText().toString().trim());
        prefsUtils.saveString("user_email", editTextEmail.getText().toString().trim());
        prefsUtils.saveString("user_phone", editTextPhone.getText().toString().trim());
        
        // Save theme preference
        int selectedThemeId = radioGroupTheme.getCheckedRadioButtonId();
        String theme = selectedThemeId == R.id.rb_dark ? "dark" : "light";
        prefsUtils.saveString("theme", theme);
        
        // Save boolean preferences
        prefsUtils.saveBoolean("notifications_enabled", checkBoxNotifications.isChecked());
        prefsUtils.saveBoolean("auto_save_enabled", checkBoxAutoSave.isChecked());
        
        // Save language preference
        prefsUtils.saveInt("language_index", spinnerLanguage.getSelectedItemPosition());
        
        // Save timestamp
        prefsUtils.saveLong("last_settings_update", System.currentTimeMillis());
        
        updateStatus("Settings saved successfully!");
        UIUtils.showToast(requireContext(), "Settings saved");
    }
    
    private void resetSettings() {
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
        
        ((RadioButton) getView().findViewById(R.id.rb_light)).setChecked(true);
        checkBoxNotifications.setChecked(true);
        checkBoxAutoSave.setChecked(false);
        spinnerLanguage.setSelection(0);
        
        updateStatus("Settings reset");
        UIUtils.showToast(requireContext(), "Settings reset");
    }
    
    private boolean validateForm() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        
        StringBuilder errors = new StringBuilder();
        
        // Validate name
        if (!ValidationUtils.isNotEmpty(name)) {
            errors.append("• Name is required\n");
        }
        
        // Validate email
        if (!ValidationUtils.isNotEmpty(email)) {
            errors.append("• Email is required\n");
        } else if (!ValidationUtils.isValidEmail(email)) {
            errors.append("• Email format is invalid\n");
        }
        
        // Validate phone (optional)
        if (ValidationUtils.isNotEmpty(phone) && !ValidationUtils.isValidPhoneNumber(phone)) {
            errors.append("• Phone number format is invalid\n");
        }
        
        if (errors.length() > 0) {
            updateStatus("Validation Errors:\n" + errors.toString());
            return false;
        } else {
            updateStatus("Form validation passed ✓");
            return true;
        }
    }
    
    private void updateStatus(String message) {
        textViewStatus.setText(message);
    }
    
    public void exportSettings() {
        // Create settings summary
        StringBuilder settings = new StringBuilder();
        settings.append("=== SETTINGS EXPORT ===\n");
        settings.append("Name: ").append(prefsUtils.getString("user_name", "")).append("\n");
        settings.append("Email: ").append(prefsUtils.getString("user_email", "")).append("\n");
        settings.append("Phone: ").append(prefsUtils.getString("user_phone", "")).append("\n");
        settings.append("Theme: ").append(prefsUtils.getString("theme", "light")).append("\n");
        settings.append("Notifications: ").append(prefsUtils.getBoolean("notifications_enabled", true)).append("\n");
        settings.append("Auto Save: ").append(prefsUtils.getBoolean("auto_save_enabled", false)).append("\n");
        settings.append("Language Index: ").append(prefsUtils.getInt("language_index", 0)).append("\n");
        
        long lastUpdate = prefsUtils.getLong("last_settings_update", 0);
        if (lastUpdate > 0) {
            settings.append("Last Updated: ").append(new java.util.Date(lastUpdate)).append("\n");
        }
        
        updateStatus(settings.toString());
    }
}
