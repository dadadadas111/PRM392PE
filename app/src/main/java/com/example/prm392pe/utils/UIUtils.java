package com.example.prm392pe.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Utility class for common UI operations
 */
public class UIUtils {
    
    /**
     * Show short toast message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Show long toast message
     */
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    
    /**
     * Hide soft keyboard
     */
    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    /**
     * Show soft keyboard
     */
    public static void showKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    
    /**
     * Convert dp to pixels
     */
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    
    /**
     * Convert pixels to dp
     */
    public static int pxToDp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(px / density);
    }
    
    /**
     * Get screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    
    /**
     * Get screen height in pixels
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    
    /**
     * Set view visibility to VISIBLE
     */
    public static void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * Set view visibility to GONE
     */
    public static void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }
    
    /**
     * Set view visibility to INVISIBLE
     */
    public static void invisibleView(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }
    
    /**
     * Toggle view visibility between VISIBLE and GONE
     */
    public static void toggleViewVisibility(View view) {
        if (view != null) {
            view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }
    }
    
    /**
     * Check if view is visible
     */
    public static boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }
}
