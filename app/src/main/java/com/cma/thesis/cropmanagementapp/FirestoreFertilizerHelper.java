package com.cma.thesis.cropmanagementapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Helper class for Firestore fertilizer operations
 */
public class FirestoreFertilizerHelper {
    
    private static final String TAG = "FirestoreFertilizerHelper";
    private static final String COLLECTION_NAME = "fertilizers";
    
    private FirebaseFirestore db;
    
    public FirestoreFertilizerHelper() {
        this.db = FirebaseFirestore.getInstance();
    }
    
    /**
     * Callback interface for fertilizer list operations
     */
    public interface FertilizerCallback {
        void onSuccess(List<Class_Fertilizer> fertilizers);
        void onError(String error);
    }
    
    /**
     * Callback interface for single fertilizer operations
     */
    public interface SingleFertilizerCallback {
        void onSuccess(Class_Fertilizer fertilizer);
        void onError(String error);
    }
    
    /**
     * Get all fertilizers from Firestore
     */
    public void getAllFertilizers(final FertilizerCallback callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Class_Fertilizer> fertilizers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class_Fertilizer fertilizer = documentToFertilizer(document);
                                if (fertilizer != null) {
                                    fertilizers.add(fertilizer);
                                }
                            }
                            callback.onSuccess(fertilizers);
                        } else {
                            String error = task.getException() != null ? 
                                    task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Error getting fertilizers: " + error);
                            callback.onError(error);
                        }
                    }
                });
    }
    
    /**
     * Get fertilizers by category
     */
    public void getFertilizersByCategory(String category, final FertilizerCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Class_Fertilizer> fertilizers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class_Fertilizer fertilizer = documentToFertilizer(document);
                                if (fertilizer != null) {
                                    fertilizers.add(fertilizer);
                                }
                            }
                            callback.onSuccess(fertilizers);
                        } else {
                            String error = task.getException() != null ? 
                                    task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Error getting fertilizers by category: " + error);
                            callback.onError(error);
                        }
                    }
                });
    }
    
    /**
     * Search fertilizers by name
     */
    public void searchFertilizersByName(String searchQuery, final FertilizerCallback callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Class_Fertilizer> fertilizers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class_Fertilizer fertilizer = documentToFertilizer(document);
                                if (fertilizer != null && fertilizer.getName() != null) {
                                    if (fertilizer.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                                        fertilizers.add(fertilizer);
                                    }
                                }
                            }
                            callback.onSuccess(fertilizers);
                        } else {
                            String error = task.getException() != null ? 
                                    task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Error searching fertilizers: " + error);
                            callback.onError(error);
                        }
                    }
                });
    }
    
    /**
     * Get fertilizers filtered by organic/chemical
     */
    public void getFertilizersByType(boolean isOrganic, final FertilizerCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("isOrganic", isOrganic)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Class_Fertilizer> fertilizers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class_Fertilizer fertilizer = documentToFertilizer(document);
                                if (fertilizer != null) {
                                    fertilizers.add(fertilizer);
                                }
                            }
                            callback.onSuccess(fertilizers);
                        } else {
                            String error = task.getException() != null ? 
                                    task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Error getting fertilizers by type: " + error);
                            callback.onError(error);
                        }
                    }
                });
    }
    
    /**
     * Convert Firestore document to Class_Fertilizer object
     */
    private Class_Fertilizer documentToFertilizer(QueryDocumentSnapshot document) {
        try {
            Class_Fertilizer fertilizer = new Class_Fertilizer();
            fertilizer.setId(document.getId());
            fertilizer.setName(document.getString("name"));
            fertilizer.setNpk(document.getString("npk"));
            fertilizer.setCategory(document.getString("category"));
            fertilizer.setDescription(document.getString("description"));
            fertilizer.setBenefits(document.getString("benefits"));
            
            // Handle bestFor array
            Object bestForObj = document.get("bestFor");
            if (bestForObj instanceof List) {
                fertilizer.setBestFor((List<String>) bestForObj);
            }
            
            fertilizer.setApplication(document.getString("application"));
            fertilizer.setTiming(document.getString("timing"));
            fertilizer.setPrecautions(document.getString("precautions"));
            fertilizer.setPriceRange(document.getString("priceRange"));
            
            // Handle boolean field
            Boolean isOrganic = document.getBoolean("isOrganic");
            fertilizer.setOrganic(isOrganic != null ? isOrganic : false);
            
            // Handle popularity (stored as string in Firestore)
            fertilizer.setPopularity(document.getString("popularity"));
            
            return fertilizer;
        } catch (Exception e) {
            Log.e(TAG, "Error converting document to fertilizer: " + e.getMessage());
            return null;
        }
    }
    
    // ==================== ADMIN CRUD OPERATIONS ====================
    
    /**
     * Get fertilizers collection reference for admin operations
     */
    public CollectionReference getFertilizersCollection() {
        return db.collection(COLLECTION_NAME);
    }
    
    /**
     * Add a new fertilizer to Firestore
     */
    public void addFertilizer(Map<String, Object> fertilizerData, OnCompleteListener<DocumentReference> listener) {
        db.collection(COLLECTION_NAME)
                .add(fertilizerData)
                .addOnCompleteListener(listener);
    }
    
    /**
     * Update an existing fertilizer in Firestore
     */
    public void updateFertilizer(String fertilizerId, Map<String, Object> fertilizerData, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME)
                .document(fertilizerId)
                .update(fertilizerData)
                .addOnCompleteListener(listener);
    }
    
    /**
     * Delete a fertilizer from Firestore
     */
    public void deleteFertilizer(String fertilizerId, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_NAME)
                .document(fertilizerId)
                .delete()
                .addOnCompleteListener(listener);
    }
    
    /**
     * Get a single fertilizer by ID
     */
    public void getFertilizerById(String fertilizerId, final SingleFertilizerCallback callback) {
        db.collection(COLLECTION_NAME)
                .document(fertilizerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try {
                            Class_Fertilizer fertilizer = new Class_Fertilizer();
                            fertilizer.setId(documentSnapshot.getId());
                            fertilizer.setName(documentSnapshot.getString("name"));
                            fertilizer.setNpk(documentSnapshot.getString("npk"));
                            fertilizer.setCategory(documentSnapshot.getString("category"));
                            fertilizer.setDescription(documentSnapshot.getString("description"));
                            fertilizer.setBenefits(documentSnapshot.getString("benefits"));
                            
                            // Handle bestFor array
                            Object bestForObj = documentSnapshot.get("bestFor");
                            if (bestForObj instanceof List) {
                                fertilizer.setBestFor((List<String>) bestForObj);
                            }
                            
                            fertilizer.setApplication(documentSnapshot.getString("application"));
                            fertilizer.setTiming(documentSnapshot.getString("timing"));
                            fertilizer.setPrecautions(documentSnapshot.getString("precautions"));
                            fertilizer.setPriceRange(documentSnapshot.getString("priceRange"));
                            
                            // Handle boolean field
                            Boolean isOrganic = documentSnapshot.getBoolean("isOrganic");
                            fertilizer.setOrganic(isOrganic != null ? isOrganic : false);
                            
                            fertilizer.setPopularity(documentSnapshot.getString("popularity"));
                            
                            callback.onSuccess(fertilizer);
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing fertilizer data", e);
                            callback.onError("Failed to parse fertilizer data");
                        }
                    } else {
                        callback.onError("Fertilizer not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching fertilizer by ID", e);
                    callback.onError("Failed to load fertilizer: " + e.getMessage());
                });
    }
}
