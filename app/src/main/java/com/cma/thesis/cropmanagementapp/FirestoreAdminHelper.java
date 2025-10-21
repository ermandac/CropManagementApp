package com.cma.thesis.cropmanagementapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Helper class for admin CRUD operations on Firestore
 * Handles: Add, Update, Delete, and Retrieve crop data
 */
public class FirestoreAdminHelper {
    private static final String TAG = "FirestoreAdminHelper";
    private static final String COLLECTION_CROPS = "crops";
    
    private final FirebaseFirestore db;
    
    public FirestoreAdminHelper() {
        db = FirebaseFirestore.getInstance();
    }
    
    /**
     * Add a new crop to Firestore
     * @param cropData Map containing all crop fields
     * @param listener Callback for completion
     */
    public void addCrop(Map<String, Object> cropData, OnCompleteListener<DocumentReference> listener) {
        db.collection(COLLECTION_CROPS)
                .add(cropData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Crop added successfully with ID: " + task.getResult().getId());
                    } else {
                        Log.e(TAG, "Error adding crop", task.getException());
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    
    /**
     * Update an existing crop in Firestore
     * @param cropId Document ID of the crop to update
     * @param cropData Map containing updated crop fields
     * @param listener Callback for completion
     */
    public void updateCrop(String cropId, Map<String, Object> cropData, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_CROPS)
                .document(cropId)
                .update(cropData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Crop updated successfully: " + cropId);
                    } else {
                        Log.e(TAG, "Error updating crop: " + cropId, task.getException());
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    
    /**
     * Delete a crop from Firestore
     * @param cropId Document ID of the crop to delete
     * @param listener Callback for completion
     */
    public void deleteCrop(String cropId, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_CROPS)
                .document(cropId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Crop deleted successfully: " + cropId);
                    } else {
                        Log.e(TAG, "Error deleting crop: " + cropId, task.getException());
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    
    /**
     * Get a specific crop by ID
     * @param cropId Document ID of the crop to retrieve
     * @param listener Callback for completion
     */
    public void getCropById(String cropId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection(COLLECTION_CROPS)
                .document(cropId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Crop retrieved successfully: " + cropId);
                    } else {
                        Log.e(TAG, "Error retrieving crop: " + cropId, task.getException());
                    }
                    if (listener != null) {
                        listener.onComplete(task);
                    }
                });
    }
    
    /**
     * Get reference to crops collection
     * @return CollectionReference for direct queries
     */
    public com.google.firebase.firestore.CollectionReference getCropsCollection() {
        return db.collection(COLLECTION_CROPS);
    }
}
