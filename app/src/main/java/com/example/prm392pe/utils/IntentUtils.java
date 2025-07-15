package com.example.prm392pe.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Utility class for creating various intents
 */
public class IntentUtils {
    
    /**
     * Create intent to open email app
     */
    public static Intent createEmailIntent(String[] recipients, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        return intent;
    }
    
    /**
     * Create intent to make phone call
     */
    public static Intent createCallIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }
    
    /**
     * Create intent to dial phone number
     */
    public static Intent createDialIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }
    
    /**
     * Create intent to send SMS
     */
    public static Intent createSmsIntent(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", message);
        return intent;
    }
    
    /**
     * Create intent to open web browser
     */
    public static Intent createWebIntent(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }
    
    /**
     * Create intent to share text
     */
    public static Intent createShareTextIntent(String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(intent, title);
    }
    
    /**
     * Create intent to share image
     */
    public static Intent createShareImageIntent(Uri imageUri, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return Intent.createChooser(intent, title);
    }
    
    /**
     * Create intent to pick image from gallery
     */
    public static Intent createPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }
    
    /**
     * Create intent to capture image from camera
     */
    public static Intent createCaptureImageIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }
    
    /**
     * Create intent to open maps with location
     */
    public static Intent createMapIntent(double latitude, double longitude, String label) {
        String uriString = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude;
        if (label != null && !label.isEmpty()) {
            uriString += "(" + label + ")";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriString));
        return intent;
    }
    
    /**
     * Create intent to open play store app page
     */
    public static Intent createPlayStoreIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageName));
        return intent;
    }
    
    /**
     * Create intent to open settings
     */
    public static Intent createSettingsIntent() {
        return new Intent(android.provider.Settings.ACTION_SETTINGS);
    }
    
    /**
     * Create intent to open app settings
     */
    public static Intent createAppSettingsIntent(String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }
}
