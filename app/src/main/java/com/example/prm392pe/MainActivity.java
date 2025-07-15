package com.example.prm392pe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.prm392pe.activities.UtilsDemoActivity;
import com.example.prm392pe.fragments.ProductListFragment;
import com.example.prm392pe.fragments.SettingsFragment;
import com.example.prm392pe.utils.SharedPreferencesUtils;
import com.example.prm392pe.utils.UIUtils;

/**
 * Main Activity demonstrating:
 * - Fragment management
 * - Menu handling
 * - SharedPreferences
 * - Intent usage
 * - Basic UI components
 */
public class MainActivity extends AppCompatActivity {
    
    private SharedPreferencesUtils prefsUtils;
    private ProductListFragment productListFragment;
    private SettingsFragment settingsFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initUtils();
        setupClickListeners();
        showWelcomeMessage();
        
        // Load default fragment
        if (savedInstanceState == null) {
            showHomeFragment();
        }
    }
    
    private void initUtils() {
        prefsUtils = new SharedPreferencesUtils(this);
    }
    
    private void setupClickListeners() {
        findViewById(R.id.btn_show_products).setOnClickListener(v -> showProductListFragment());
        findViewById(R.id.btn_show_settings).setOnClickListener(v -> showSettingsFragment());
        findViewById(R.id.btn_utils_demo).setOnClickListener(v -> openUtilsDemo());
        findViewById(R.id.btn_add_product).setOnClickListener(v -> addNewProduct());
        findViewById(R.id.btn_clear_products).setOnClickListener(v -> clearProducts());
        findViewById(R.id.btn_export_settings).setOnClickListener(v -> exportSettings());
    }
    
    private void showWelcomeMessage() {
        boolean isFirstTime = prefsUtils.getBoolean("is_first_time", true);
        if (isFirstTime) {
            prefsUtils.saveBoolean("is_first_time", false);
            prefsUtils.saveLong("first_launch_time", System.currentTimeMillis());
            
            new AlertDialog.Builder(this)
                    .setTitle("Welcome!")
                    .setMessage("Welcome to PRM392PE Test Preparation App!\n\nThis app demonstrates:\n" +
                            "• RecyclerView with custom adapter\n" +
                            "• Fragment management\n" +
                            "• Image handling & intents\n" +
                            "• File storage (internal/external)\n" +
                            "• SharedPreferences\n" +
                            "• Menu handling\n" +
                            "• Validation utilities\n" +
                            "• And much more!")
                    .setPositiveButton("Got it!", null)
                    .show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.menu_home) {
            showHomeFragment();
            return true;
        } else if (itemId == R.id.menu_products) {
            showProductListFragment();
            return true;
        } else if (itemId == R.id.menu_settings) {
            showSettingsFragment();
            return true;
        } else if (itemId == R.id.menu_utils) {
            openUtilsDemo();
            return true;
        } else if (itemId == R.id.menu_about) {
            showAboutDialog();
            return true;
        } else if (itemId == R.id.menu_exit) {
            showExitDialog();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void showHomeFragment() {
        // Clear any existing fragments and show main layout
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Fragment())
                .commit();
        
        // Show/hide main content
        UIUtils.showView(findViewById(R.id.main_content));
        UIUtils.hideView(findViewById(R.id.fragment_container));
        
        setTitle("Home");
    }
    
    private void showProductListFragment() {
        if (productListFragment == null) {
            productListFragment = ProductListFragment.newInstance();
        }
        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, productListFragment)
                .addToBackStack(null)
                .commit();
        
        UIUtils.hideView(findViewById(R.id.main_content));
        UIUtils.showView(findViewById(R.id.fragment_container));
        
        setTitle("Product List");
    }
    
    private void showSettingsFragment() {
        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
        }
        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit();
        
        UIUtils.hideView(findViewById(R.id.main_content));
        UIUtils.showView(findViewById(R.id.fragment_container));
        
        setTitle("Settings");
    }
    
    private void openUtilsDemo() {
        Intent intent = new Intent(this, UtilsDemoActivity.class);
        startActivity(intent);
    }
    
    private void addNewProduct() {
        if (productListFragment != null) {
            productListFragment.addNewProduct();
            UIUtils.showToast(this, "New product added!");
        } else {
            UIUtils.showToast(this, "Please open Products first");
        }
    }
    
    private void clearProducts() {
        if (productListFragment != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Clear Products")
                    .setMessage("Are you sure you want to clear all products?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        productListFragment.clearAllProducts();
                        UIUtils.showToast(this, "Products cleared!");
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            UIUtils.showToast(this, "Please open Products first");
        }
    }
    
    private void exportSettings() {
        if (settingsFragment != null) {
            settingsFragment.exportSettings();
            UIUtils.showToast(this, "Settings exported!");
        } else {
            UIUtils.showToast(this, "Please open Settings first");
        }
    }
    
    private void showAboutDialog() {
        long firstLaunch = prefsUtils.getLong("first_launch_time", 0);
        String firstLaunchStr = firstLaunch > 0 ? new java.util.Date(firstLaunch).toString() : "Unknown";
        
        new AlertDialog.Builder(this)
                .setTitle("About PRM392PE")
                .setMessage("PRM392PE Test Preparation App\n\n" +
                        "Version: 1.0\n" +
                        "Package: " + getPackageName() + "\n" +
                        "First Launch: " + firstLaunchStr + "\n\n" +
                        "This app demonstrates all the key concepts for Android development testing.")
                .setPositiveButton("OK", null)
                .show();
    }
    
    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    prefsUtils.saveLong("last_exit_time", System.currentTimeMillis());
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
    
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // Show main content if back to home
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                UIUtils.showView(findViewById(R.id.main_content));
                UIUtils.hideView(findViewById(R.id.fragment_container));
                setTitle("Home");
            }
        } else {
            showExitDialog();
        }
    }
}