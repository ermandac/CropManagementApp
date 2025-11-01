package com.cma.thesis.cropmanagementapp;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreOrganicFarmingHelper {
    private static final String TAG = "OrganicFarmingHelper";
    private static final String COLLECTION_NAME = "organic_farming";
    
    private FirebaseFirestore db;

    public FirestoreOrganicFarmingHelper() {
        db = FirebaseFirestore.getInstance();
    }

    // Callback interface
    public interface OrganicFarmingCallback {
        void onSuccess(List<Class_OrganicFarming> items);
        void onError(String error);
    }

    /**
     * Get all organic farming items
     */
    public void getAllOrganicFarming(OrganicFarmingCallback callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_OrganicFarming> items = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_OrganicFarming item = document.toObject(Class_OrganicFarming.class);
                        items.add(item);
                    }
                    Log.d(TAG, "Successfully fetched " + items.size() + " organic farming items");
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching organic farming items", e);
                    callback.onError(e.getMessage());
                });
    }

    /**
     * Get organic farming items by type (Farming Method or Material)
     */
    public void getOrganicFarmingByType(String type, OrganicFarmingCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("type", type)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_OrganicFarming> items = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_OrganicFarming item = document.toObject(Class_OrganicFarming.class);
                        items.add(item);
                    }
                    Log.d(TAG, "Successfully fetched " + items.size() + " items for type: " + type);
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching items by type", e);
                    callback.onError(e.getMessage());
                });
    }

    /**
     * Get organic farming items by category
     */
    public void getOrganicFarmingByCategory(String category, OrganicFarmingCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_OrganicFarming> items = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_OrganicFarming item = document.toObject(Class_OrganicFarming.class);
                        items.add(item);
                    }
                    Log.d(TAG, "Successfully fetched " + items.size() + " items for category: " + category);
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching items by category", e);
                    callback.onError(e.getMessage());
                });
    }

    /**
     * Search organic farming items by name
     */
    public void searchOrganicFarmingByName(String query, OrganicFarmingCallback callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Class_OrganicFarming> items = new ArrayList<>();
                    String lowerQuery = query.toLowerCase();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Class_OrganicFarming item = document.toObject(Class_OrganicFarming.class);
                        if (item.getName() != null && 
                            item.getName().toLowerCase().contains(lowerQuery)) {
                            items.add(item);
                        }
                    }
                    Log.d(TAG, "Search found " + items.size() + " items matching: " + query);
                    callback.onSuccess(items);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching organic farming items", e);
                    callback.onError(e.getMessage());
                });
    }

    // ==================== ADMIN CRUD OPERATIONS ====================

    /**
     * Callback interface for single item operations
     */
    public interface SingleItemCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    /**
     * Add a new organic farming item (Admin)
     */
    public void addOrganicFarming(Class_OrganicFarming item, SingleItemCallback callback) {
        if (item.getId() == null || item.getId().isEmpty()) {
            // Generate a new ID if not provided
            item.setId(db.collection(COLLECTION_NAME).document().getId());
        }

        db.collection(COLLECTION_NAME)
                .document(item.getId())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully added organic farming item: " + item.getName());
                    callback.onSuccess("Organic farming item added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding organic farming item", e);
                    callback.onError("Error: " + e.getMessage());
                });
    }

    /**
     * Update an existing organic farming item (Admin)
     */
    public void updateOrganicFarming(Class_OrganicFarming item, SingleItemCallback callback) {
        if (item.getId() == null || item.getId().isEmpty()) {
            callback.onError("Error: Organic farming item ID is required for update");
            return;
        }

        db.collection(COLLECTION_NAME)
                .document(item.getId())
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully updated organic farming item: " + item.getName());
                    callback.onSuccess("Organic farming item updated successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating organic farming item", e);
                    callback.onError("Error: " + e.getMessage());
                });
    }

    /**
     * Delete an organic farming item (Admin)
     */
    public void deleteOrganicFarming(String itemId, SingleItemCallback callback) {
        if (itemId == null || itemId.isEmpty()) {
            callback.onError("Error: Organic farming item ID is required for deletion");
            return;
        }

        db.collection(COLLECTION_NAME)
                .document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully deleted organic farming item with ID: " + itemId);
                    callback.onSuccess("Organic farming item deleted successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting organic farming item", e);
                    callback.onError("Error: " + e.getMessage());
                });
    }

    /**
     * Get a single organic farming item by ID (Admin)
     */
    public void getOrganicFarmingById(String itemId, SingleOrganicCallback callback) {
        if (itemId == null || itemId.isEmpty()) {
            callback.onError("Error: Organic farming item ID is required");
            return;
        }

        db.collection(COLLECTION_NAME)
                .document(itemId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Class_OrganicFarming item = documentSnapshot.toObject(Class_OrganicFarming.class);
                        Log.d(TAG, "Successfully fetched organic farming item: " + itemId);
                        callback.onSuccess(item);
                    } else {
                        callback.onError("Organic farming item not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching organic farming item", e);
                    callback.onError("Error: " + e.getMessage());
                });
    }

    /**
     * Callback interface for single organic farming item
     */
    public interface SingleOrganicCallback {
        void onSuccess(Class_OrganicFarming item);
        void onError(String error);
    }
}
