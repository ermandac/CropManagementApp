package com.cma.thesis.cropmanagementapp;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreSuggestedCropsHelper {
    private static final String TAG = "FirestoreSuggestedCrops";
    private static final String COLLECTION_NAME = "suggested_crops";

    private FirebaseFirestore db;
    private CollectionReference cropsRef;

    public FirestoreSuggestedCropsHelper() {
        db = FirebaseFirestore.getInstance();
        cropsRef = db.collection(COLLECTION_NAME);
    }

    /**
     * Callback interface for crop data retrieval
     */
    public interface CropsCallback {
        void onSuccess(List<Class_SuggestedCrop> crops);
        void onError(Exception e);
    }

    /**
     * Get all suggested crops
     */
    public void getAllSuggestedCrops(CropsCallback callback) {
        cropsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_SuggestedCrop> crops = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_SuggestedCrop crop = document.toObject(Class_SuggestedCrop.class);
                        crops.add(crop);
                    }
                    Log.d(TAG, "Retrieved " + crops.size() + " suggested crops");
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting suggested crops", e);
                    callback.onError(e);
                });
    }

    /**
     * Get suggested crops filtered by month
     * @param month The month to filter by (e.g., "January", "February", etc.)
     */
    public void getSuggestedCropsByMonth(String month, CropsCallback callback) {
        Query query = cropsRef.whereEqualTo("month", month);
        
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_SuggestedCrop> crops = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_SuggestedCrop crop = document.toObject(Class_SuggestedCrop.class);
                        crops.add(crop);
                    }
                    Log.d(TAG, "Retrieved " + crops.size() + " crops for month: " + month);
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting crops by month", e);
                    callback.onError(e);
                });
    }

    /**
     * Search suggested crops by crop name
     * Note: Firestore doesn't support native text search, so this performs a client-side filter
     */
    public void searchSuggestedCropsByName(String query, CropsCallback callback) {
        cropsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_SuggestedCrop> crops = new ArrayList<>();
                    String lowerCaseQuery = query.toLowerCase();
                    
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_SuggestedCrop crop = document.toObject(Class_SuggestedCrop.class);
                        if (crop.getCropName() != null && 
                            crop.getCropName().toLowerCase().contains(lowerCaseQuery)) {
                            crops.add(crop);
                        }
                    }
                    Log.d(TAG, "Found " + crops.size() + " crops matching: " + query);
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching crops by name", e);
                    callback.onError(e);
                });
    }

    /**
     * Get suggested crops filtered by category
     * @param category The category to filter by (e.g., "Vegetables", "Fruits", etc.)
     */
    public void getSuggestedCropsByCategory(String category, CropsCallback callback) {
        Query query = cropsRef.whereEqualTo("category", category);
        
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_SuggestedCrop> crops = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_SuggestedCrop crop = document.toObject(Class_SuggestedCrop.class);
                        crops.add(crop);
                    }
                    Log.d(TAG, "Retrieved " + crops.size() + " crops for category: " + category);
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting crops by category", e);
                    callback.onError(e);
                });
    }

    /**
     * Get suggested crops filtered by both month and category
     */
    public void getSuggestedCropsByMonthAndCategory(String month, String category, CropsCallback callback) {
        Query query = cropsRef.whereEqualTo("month", month)
                              .whereEqualTo("category", category);
        
        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_SuggestedCrop> crops = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_SuggestedCrop crop = document.toObject(Class_SuggestedCrop.class);
                        crops.add(crop);
                    }
                    Log.d(TAG, "Retrieved " + crops.size() + " crops for month: " + month + ", category: " + category);
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting crops by month and category", e);
                    callback.onError(e);
                });
    }

    /**
     * Callback interface for single operations (add, update, delete)
     */
    public interface OperationCallback {
        void onSuccess(String message);
        void onError(Exception e);
    }

    /**
     * Add a new suggested crop
     */
    public void addSuggestedCrop(Class_SuggestedCrop crop, OperationCallback callback) {
        String documentId = cropsRef.document().getId();
        crop.setId(documentId);
        
        cropsRef.document(documentId)
                .set(crop)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Suggested crop added successfully: " + documentId);
                    callback.onSuccess("Suggested crop added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding suggested crop", e);
                    callback.onError(e);
                });
    }

    /**
     * Update an existing suggested crop
     */
    public void updateSuggestedCrop(String cropId, Class_SuggestedCrop crop, OperationCallback callback) {
        cropsRef.document(cropId)
                .set(crop)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Suggested crop updated successfully: " + cropId);
                    callback.onSuccess("Suggested crop updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating suggested crop", e);
                    callback.onError(e);
                });
    }

    /**
     * Delete a suggested crop
     */
    public void deleteSuggestedCrop(String cropId, OperationCallback callback) {
        cropsRef.document(cropId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Suggested crop deleted successfully: " + cropId);
                    callback.onSuccess("Suggested crop deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting suggested crop", e);
                    callback.onError(e);
                });
    }

    /**
     * Get a single suggested crop by ID
     */
    public void getSuggestedCropById(String cropId, SingleCropCallback callback) {
        cropsRef.document(cropId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Class_SuggestedCrop crop = documentSnapshot.toObject(Class_SuggestedCrop.class);
                        Log.d(TAG, "Retrieved suggested crop: " + cropId);
                        callback.onSuccess(crop);
                    } else {
                        callback.onError(new Exception("Suggested crop not found"));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting suggested crop by ID", e);
                    callback.onError(e);
                });
    }

    /**
     * Callback interface for single crop retrieval
     */
    public interface SingleCropCallback {
        void onSuccess(Class_SuggestedCrop crop);
        void onError(Exception e);
    }

    /**
     * Get collection reference (for admin operations)
     */
    public CollectionReference getSuggestedCropsCollection() {
        return cropsRef;
    }
}
