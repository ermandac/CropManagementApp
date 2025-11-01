package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * Helper class for image operations:
 * - Compressing images using Compressor library
 * - Uploading images to Firebase Storage
 * - Deleting images from Firebase Storage
 */
public class ImageHelper {
    private static final String TAG = "ImageHelper";
    private static final int MAX_IMAGE_SIZE = 500; // Max width/height in KB
    private static final int IMAGE_QUALITY = 80; // Compression quality (0-100)
    
    private final FirebaseStorage storage;
    
    public ImageHelper() {
        // Initialize Firebase Storage with explicit bucket URL
        try {
            storage = FirebaseStorage.getInstance("gs://cropmanagementapp-ba8af.firebasestorage.app");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Firebase Storage with bucket URL, using default: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Firebase Storage. Please check Firebase configuration.", e);
        }
    }
    
    /**
     * Compress an image from URI
     * @param context Application context
     * @param imageUri URI of the image to compress
     * @return Compressed File or null if compression fails
     */
    public File compressImage(Context context, Uri imageUri) {
        try {
            // Get input stream from URI
            java.io.InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream == null) {
                Log.e(TAG, "Failed to open input stream from URI");
                return null;
            }
            
            // Decode bitmap
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            
            if (bitmap == null) {
                Log.e(TAG, "Failed to decode bitmap from URI");
                return null;
            }
            
            // Calculate scaled dimensions
            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            int maxDimension = 1024;
            
            float scale = Math.min(
                (float) maxDimension / originalWidth,
                (float) maxDimension / originalHeight
            );
            
            int scaledWidth = Math.round(originalWidth * scale);
            int scaledHeight = Math.round(originalHeight * scale);
            
            // Scale bitmap
            android.graphics.Bitmap scaledBitmap = android.graphics.Bitmap.createScaledBitmap(
                bitmap, scaledWidth, scaledHeight, true
            );
            
            // Create output file
            File outputDir = context.getCacheDir();
            File compressedFile = File.createTempFile("compressed_", ".jpg", outputDir);
            
            // Compress and save
            java.io.FileOutputStream fos = new java.io.FileOutputStream(compressedFile);
            scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, fos);
            fos.flush();
            fos.close();
            
            // Clean up
            bitmap.recycle();
            scaledBitmap.recycle();
            
            Log.d(TAG, "Image compressed successfully. Size: " + compressedFile.length());
            
            return compressedFile;
        } catch (IOException e) {
            Log.e(TAG, "Error compressing image: " + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error compressing image: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Convert image file to Base64 string for Firestore storage
     * @param imageFile File to convert
     * @return Base64 encoded string or null if conversion fails
     */
    public String convertImageToBase64(File imageFile) {
        try {
            // Read file into byte array
            java.io.FileInputStream fis = new java.io.FileInputStream(imageFile);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            
            fis.close();
            
            // Convert to Base64
            byte[] imageBytes = baos.toByteArray();
            String base64String = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
            
            Log.d(TAG, "Image converted to Base64. Size: " + base64String.length() + " characters");
            
            return base64String;
        } catch (Exception e) {
            Log.e(TAG, "Error converting image to Base64: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Save image to local internal storage (alternative to Firebase Storage for POC)
     * @param context Application context
     * @param imageFile File to save
     * @param categoryId Category ID for organizing storage
     * @param cropName Crop name for file naming
     * @return Local file path or null if save fails
     */
    public String saveToLocalStorage(Context context, File imageFile, String categoryId, String cropName) {
        try {
            // Create directory structure: app_images/crops/{categoryId}/
            File imagesDir = new File(context.getFilesDir(), "app_images/crops/" + categoryId);
            if (!imagesDir.exists()) {
                if (!imagesDir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory: " + imagesDir.getAbsolutePath());
                    return null;
                }
            }
            
            // Create unique filename: {cropName}_{timestamp}.jpg
            String timestamp = String.valueOf(System.currentTimeMillis());
            String sanitizedCropName = cropName.replaceAll("[^a-zA-Z0-9]", "_");
            String filename = sanitizedCropName + "_" + timestamp + ".jpg";
            
            File destinationFile = new File(imagesDir, filename);
            
            // Copy compressed file to internal storage
            java.io.FileInputStream fis = new java.io.FileInputStream(imageFile);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(destinationFile);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            
            fis.close();
            fos.close();
            
            String localPath = destinationFile.getAbsolutePath();
            Log.d(TAG, "Image saved to local storage: " + localPath);
            
            return localPath;
        } catch (IOException e) {
            Log.e(TAG, "Error saving image to local storage: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Delete image from local storage
     * @param localPath Absolute path to the local image file
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteFromLocalStorage(String localPath) {
        try {
            if (localPath == null || localPath.isEmpty()) {
                return false;
            }
            
            File file = new File(localPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    Log.d(TAG, "Image deleted from local storage: " + localPath);
                } else {
                    Log.e(TAG, "Failed to delete image: " + localPath);
                }
                return deleted;
            } else {
                Log.w(TAG, "Image file does not exist: " + localPath);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deleting image from local storage: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Upload image to Firebase Storage
     * @param imageFile File to upload
     * @param categoryId Category ID for organizing storage
     * @param cropName Crop name for file naming
     * @return Task containing the download URL
     */
    public Task<Uri> uploadToFirebaseStorage(File imageFile, String categoryId, String cropName) {
        // Create unique filename: crops/{categoryId}/{cropName}_{timestamp}.jpg
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sanitizedCropName = cropName.replaceAll("[^a-zA-Z0-9]", "_");
        String filename = sanitizedCropName + "_" + timestamp + ".jpg";
        String storagePath = "crops/" + categoryId + "/" + filename;
        
        // Get storage reference
        StorageReference storageRef = storage.getReference().child(storagePath);
        
        // Upload file
        Uri fileUri = Uri.fromFile(imageFile);
        UploadTask uploadTask = storageRef.putFile(fileUri);
        
        // Return task that gets download URL after upload completes
        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            // Get download URL
            return storageRef.getDownloadUrl();
        });
    }
    
    /**
     * Delete image from Firebase Storage using its URL
     * @param imageUrl Full download URL of the image
     * @return Task for deletion operation
     */
    public Task<Void> deleteFromFirebaseStorage(String imageUrl) {
        try {
            // Create storage reference from URL
            StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);
            
            // Delete the file
            return storageRef.delete()
                    .addOnSuccessListener(aVoid -> 
                            Log.d(TAG, "Image deleted successfully from Storage"))
                    .addOnFailureListener(e -> 
                            Log.e(TAG, "Error deleting image: " + e.getMessage(), e));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid storage URL: " + imageUrl, e);
            // Return failed task
            return com.google.android.gms.tasks.Tasks.forException(e);
        }
    }
    
    /**
     * Extract storage path from download URL
     * @param downloadUrl Full download URL
     * @return Storage path or null if invalid
     */
    public String getStoragePathFromUrl(String downloadUrl) {
        try {
            StorageReference ref = storage.getReferenceFromUrl(downloadUrl);
            return ref.getPath();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid URL: " + downloadUrl, e);
            return null;
        }
    }
}
