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
