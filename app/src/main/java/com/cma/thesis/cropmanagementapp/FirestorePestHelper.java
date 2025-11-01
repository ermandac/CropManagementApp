package com.cma.thesis.cropmanagementapp;

import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FirestorePestHelper - Helper class for pest data operations
 * Handles all Firestore queries for pest information
 */
public class FirestorePestHelper {
    private static final String TAG = "FirestorePestHelper";
    private static final String COLLECTION_PESTS = "pests";
    
    private FirebaseFirestore db;

    public FirestorePestHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Callback interface for pest data operations
     */
    public interface PestsCallback {
        void onSuccess(List<Class_Pest> pests);
        void onError(String error);
    }

    /**
     * Get all pests from Firestore
     */
    public void getAllPests(final PestsCallback callback) {
        db.collection(COLLECTION_PESTS)
                .orderBy("pestName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_Pest> pestsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_Pest pest = document.toObject(Class_Pest.class);
                        pestsList.add(pest);
                    }
                    Log.d(TAG, "Fetched " + pestsList.size() + " pests");
                    callback.onSuccess(pestsList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching pests", e);
                    callback.onError("Failed to load pests: " + e.getMessage());
                });
    }

    /**
     * Get pests by severity level
     * @param severity - "Very High", "High", "Medium"
     */
    public void getPestsBySeverity(String severity, final PestsCallback callback) {
        db.collection(COLLECTION_PESTS)
                .whereEqualTo("severity", severity)
                .orderBy("pestName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_Pest> pestsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_Pest pest = document.toObject(Class_Pest.class);
                        pestsList.add(pest);
                    }
                    Log.d(TAG, "Fetched " + pestsList.size() + " pests with severity: " + severity);
                    callback.onSuccess(pestsList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching pests by severity", e);
                    callback.onError("Failed to load pests: " + e.getMessage());
                });
    }

    /**
     * Get pests by category
     * @param category - "Insect", "Nematode", etc.
     */
    public void getPestsByCategory(String category, final PestsCallback callback) {
        db.collection(COLLECTION_PESTS)
                .whereEqualTo("category", category)
                .orderBy("pestName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_Pest> pestsList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_Pest pest = document.toObject(Class_Pest.class);
                        pestsList.add(pest);
                    }
                    Log.d(TAG, "Fetched " + pestsList.size() + " pests in category: " + category);
                    callback.onSuccess(pestsList);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching pests by category", e);
                    callback.onError("Failed to load pests: " + e.getMessage());
                });
    }

    /**
     * Search pests by name (client-side filtering)
     * @param query - search query string
     */
    public void searchPestsByName(String query, final PestsCallback callback) {
        getAllPests(new PestsCallback() {
            @Override
            public void onSuccess(List<Class_Pest> pests) {
                List<Class_Pest> filteredPests = new ArrayList<>();
                String lowerQuery = query.toLowerCase();
                
                for (Class_Pest pest : pests) {
                    if (pest.getPestName() != null && 
                        pest.getPestName().toLowerCase().contains(lowerQuery)) {
                        filteredPests.add(pest);
                    }
                }
                
                Log.d(TAG, "Search found " + filteredPests.size() + " pests matching: " + query);
                callback.onSuccess(filteredPests);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    /**
     * Get pests affecting a specific crop (client-side filtering)
     * @param cropName - name of the crop
     */
    public void getPestsByAffectedCrop(String cropName, final PestsCallback callback) {
        getAllPests(new PestsCallback() {
            @Override
            public void onSuccess(List<Class_Pest> pests) {
                List<Class_Pest> filteredPests = new ArrayList<>();
                
                for (Class_Pest pest : pests) {
                    if (pest.getAffectedCrops() != null) {
                        for (String crop : pest.getAffectedCrops()) {
                            if (crop.equalsIgnoreCase(cropName)) {
                                filteredPests.add(pest);
                                break;
                            }
                        }
                    }
                }
                
                Log.d(TAG, "Found " + filteredPests.size() + " pests affecting: " + cropName);
                callback.onSuccess(filteredPests);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // ==================== ADMIN CRUD OPERATIONS ====================

    /**
     * Get pests collection reference for admin operations
     */
    public CollectionReference getPestsCollection() {
        return db.collection(COLLECTION_PESTS);
    }

    /**
     * Add a new pest to Firestore
     */
    public void addPest(Map<String, Object> pestData, OnCompleteListener<DocumentReference> listener) {
        db.collection(COLLECTION_PESTS)
                .add(pestData)
                .addOnCompleteListener(listener);
    }

    /**
     * Update an existing pest in Firestore
     */
    public void updatePest(String pestId, Map<String, Object> pestData, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_PESTS)
                .document(pestId)
                .update(pestData)
                .addOnCompleteListener(listener);
    }

    /**
     * Delete a pest from Firestore
     */
    public void deletePest(String pestId, OnCompleteListener<Void> listener) {
        db.collection(COLLECTION_PESTS)
                .document(pestId)
                .delete()
                .addOnCompleteListener(listener);
    }

    /**
     * Get a single pest by ID
     */
    public void getPestById(String pestId, final PestCallback callback) {
        db.collection(COLLECTION_PESTS)
                .document(pestId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Class_Pest pest = documentSnapshot.toObject(Class_Pest.class);
                        if (pest != null) {
                            pest.setId(documentSnapshot.getId());
                            callback.onSuccess(pest);
                        } else {
                            callback.onError("Failed to parse pest data");
                        }
                    } else {
                        callback.onError("Pest not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching pest by ID", e);
                    callback.onError("Failed to load pest: " + e.getMessage());
                });
    }

    /**
     * Callback interface for single pest operations
     */
    public interface PestCallback {
        void onSuccess(Class_Pest pest);
        void onError(String error);
    }
}
