package com.cma.thesis.cropmanagementapp;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;

/**
 * FirestoreCropHelper
 * Handles Firestore queries for crop data
 */
public class FirestoreCropHelper {
    private static final String TAG = "FirestoreCropHelper";
    private FirebaseFirestore db;

    public FirestoreCropHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Load crops by category ID from Firestore
     */
    public void loadCropsByCategory(String categoryId, CropLoadCallback callback) {
        db.collection("crops")
                .whereEqualTo("categoryId", Long.parseLong(categoryId))
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    ArrayList<Class_Crops> crops = new ArrayList<>();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            try {
                                Class_Crops crop = documentToCrop(doc);
                                if (crop != null) {
                                    crops.add(crop);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error converting document: " + e.getMessage());
                            }
                        }
                    }
                    callback.onSuccess(crops);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading crops: " + e.getMessage());
                    callback.onError("Error: " + e.getMessage());
                });
    }

    /**
     * Convert Firestore DocumentSnapshot to Class_Crops object
     */
    private Class_Crops documentToCrop(DocumentSnapshot doc) {
        try {
            int id = Math.toIntExact(doc.getLong("id") != null ? doc.getLong("id") : 0);
            int categoryId = Math.toIntExact(doc.getLong("categoryId") != null ? doc.getLong("categoryId") : 0);
            String cropName = doc.getString("cropName") != null ? doc.getString("cropName") : "";
            String description = doc.getString("description") != null ? doc.getString("description") : "";
            String scienceName = doc.getString("scienceName") != null ? doc.getString("scienceName") : "";
            String duration = doc.getString("duration") != null ? doc.getString("duration") : "";
            String varieties = getArrayAsString(doc, "varieties");
            String soilClimate = doc.getString("soilClimate") != null ? doc.getString("soilClimate") : "";
            String season = doc.getString("season") != null ? doc.getString("season") : "";
            String mainField = doc.getString("mainField") != null ? doc.getString("mainField") : "";
            String irrigation = doc.getString("irrigation") != null ? doc.getString("irrigation") : "";
            String growthManagement = doc.getString("growthManagement") != null ? doc.getString("growthManagement") : "";
            String harvesting = doc.getString("harvesting") != null ? doc.getString("harvesting") : "";
            String image = doc.getString("image") != null ? doc.getString("image") : "";

            return new Class_Crops(
                    id,
                    categoryId,
                    cropName,
                    description,
                    scienceName,
                    duration,
                    varieties,
                    soilClimate,
                    season,
                    mainField,
                    irrigation,
                    growthManagement,
                    harvesting,
                    image
            );
        } catch (Exception e) {
            Log.e(TAG, "Error creating Class_Crops from document: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert array fields to comma-separated string
     */
    private String getArrayAsString(DocumentSnapshot doc, String fieldName) {
        try {
            Object field = doc.get(fieldName);
            if (field instanceof java.util.List) {
                java.util.List<?> list = (java.util.List<?>) field;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i).toString());
                    if (i < list.size() - 1) {
                        sb.append("\n");
                    }
                }
                return sb.toString();
            } else if (field instanceof String) {
                return (String) field;
            }
        } catch (Exception e) {
            Log.w(TAG, "Error converting field " + fieldName + " to string: " + e.getMessage());
        }
        return "";
    }

    /**
     * Callback interface for crop loading operations
     */
    public interface CropLoadCallback {
        void onSuccess(ArrayList<Class_Crops> crops);
        void onError(String error);
    }
}
