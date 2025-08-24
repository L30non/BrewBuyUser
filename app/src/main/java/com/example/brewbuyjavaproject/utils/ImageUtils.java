package com.example.brewbuyjavaproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    
    // Test Base64 string for a small red square PNG
    private static final String TEST_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==";

    /**
     * Convert Base64 string to Bitmap
     * @param base64String Base64 encoded image string
     * @return Bitmap object or null if conversion fails
     */
    public static Bitmap base64ToBitmap(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            Log.d(TAG, "Base64 string is null or empty");
            return null;
        }

        try {
            Log.d(TAG, "Converting Base64 string of length: " + base64String.length());
            
            // Remove data URL prefix if present (e.g., "data:image/jpeg;base64,")
            String base64Data = base64String;
            if (base64String.contains(",")) {
                base64Data = base64String.substring(base64String.indexOf(",") + 1);
                Log.d(TAG, "Removed data URL prefix, data length now: " + base64Data.length());
            }

            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);
            Log.d(TAG, "Decoded bytes length: " + decodedBytes.length);
            
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap from byte array");
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error converting Base64 to Bitmap", e);
            return null;
        }
    }

    /**
     * Convert Base64 string to ByteArrayInputStream for use with Glide
     * @param base64String Base64 encoded image string
     * @return ByteArrayInputStream or null if conversion fails
     */
    public static ByteArrayInputStream base64ToStream(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            Log.d(TAG, "Base64 string is null or empty for stream conversion");
            return null;
        }

        try {
            Log.d(TAG, "Converting Base64 string to stream, length: " + base64String.length());
            
            // Remove data URL prefix if present (e.g., "data:image/jpeg;base64,")
            String base64Data = base64String;
            if (base64String.contains(",")) {
                base64Data = base64String.substring(base64String.indexOf(",") + 1);
                Log.d(TAG, "Removed data URL prefix for stream, data length now: " + base64Data.length());
            }

            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);
            Log.d(TAG, "Decoded bytes length for stream: " + decodedBytes.length);
            
            return new ByteArrayInputStream(decodedBytes);
        } catch (Exception e) {
            Log.e(TAG, "Error converting Base64 to Stream", e);
            return null;
        }
    }
    
    /**
     * Test method to verify Base64 conversion is working
     * @return true if test passes, false otherwise
     */
    public static boolean testBase64Conversion() {
        try {
            Log.d(TAG, "Testing Base64 conversion with known good data");
            Bitmap bitmap = base64ToBitmap(TEST_BASE64);
            boolean success = bitmap != null;
            Log.d(TAG, "Base64 conversion test " + (success ? "PASSED" : "FAILED"));
            if (bitmap != null) {
                bitmap.recycle();
            }
            return success;
        } catch (Exception e) {
            Log.e(TAG, "Base64 conversion test FAILED with exception", e);
            return false;
        }
    }
}