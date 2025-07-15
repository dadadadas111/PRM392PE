package com.example.prm392pe.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for image operations
 */
public class ImageUtils {
    
    /**
     * Convert bitmap to base64 string
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    
    /**
     * Convert base64 string to bitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    
    /**
     * Resize bitmap to specified dimensions
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
    }
    
    /**
     * Compress bitmap with quality
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    
    /**
     * Get bitmap from URI
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Calculate sample size for loading large images efficiently
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        
        return inSampleSize;
    }
    
    /**
     * Load bitmap with sample size to prevent OutOfMemoryError
     */
    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri, int reqWidth, int reqHeight) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            
            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            
            inputStream.close();
            
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            
            // Decode bitmap with inSampleSize set
            inputStream = context.getContentResolver().openInputStream(uri);
            options.inJustDecodeBounds = false;
            
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Rotate bitmap based on EXIF data
     */
    public static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {
        try {
            InputStream input = context.getContentResolver().openInputStream(selectedImage);
            ExifInterface ei = new ExifInterface(input);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return img;
        }
    }
    
    /**
     * Rotate bitmap by specified angle
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    
    /**
     * Create circular bitmap
     */
    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;
        
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
        
        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        
        final int color = 0xff424242;
        final android.graphics.Paint paint = new android.graphics.Paint();
        final android.graphics.Rect rect = new android.graphics.Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        
        float r = 0;
        
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2f;
        } else {
            r = bitmap.getWidth() / 2f;
        }
        
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        return output;
    }
}
