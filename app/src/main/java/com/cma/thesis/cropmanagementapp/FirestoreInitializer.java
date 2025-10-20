package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * FirestoreInitializer
 * Uploads SQLite migrated data directly to Firestore from Android
 */
public class FirestoreInitializer {
    private static final String TAG = "FirestoreInitializer";
    private FirebaseFirestore db;
    private Context context;

    public FirestoreInitializer(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Upload data from firestore_migration.json to Firestore
     */
    public void uploadMigratedData(final UploadCallback callback) {
        try {
            // Read JSON file from assets
            String jsonString = readJsonFromAssets("firestore_migration.json");
            JSONObject migratedData = new JSONObject(jsonString);

            Log.d(TAG, "Starting Firestore data upload...");

            // Upload categories
            uploadCategories(migratedData, new UploadCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.d(TAG, message);
                    // Upload crops
                    uploadCrops(migratedData, new UploadCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Log.d(TAG, message);
                            // Upload procedures
                            uploadProcedures(migratedData, callback);
                        }

                        @Override
                        public void onError(String error) {
                            callback.onError(error);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error reading migration file", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    private void uploadCategories(JSONObject data, UploadCallback callback) {
        try {
            JSONObject categories = data.getJSONObject("categories");
            WriteBatch batch = db.batch();
            Iterator<String> keys = categories.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject category = categories.getJSONObject(key);
                Map<String, Object> categoryMap = jsonToMap(category);
                batch.set(db.collection("categories").document(key), categoryMap);
            }

            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Categories uploaded: " + categories.length());
                callback.onSuccess("✓ Categories uploaded: " + categories.length());
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading categories", e);
                callback.onError("Error uploading categories: " + e.getMessage());
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error processing categories", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    private void uploadCrops(JSONObject data, UploadCallback callback) {
        try {
            JSONObject crops = data.getJSONObject("crops");
            WriteBatch batch = db.batch();
            Iterator<String> keys = crops.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject crop = crops.getJSONObject(key);
                Map<String, Object> cropMap = jsonToMap(crop);
                batch.set(db.collection("crops").document(key), cropMap);
            }

            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Crops uploaded: " + crops.length());
                callback.onSuccess("✓ Crops uploaded: " + crops.length());
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading crops", e);
                callback.onError("Error uploading crops: " + e.getMessage());
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error processing crops", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    private void uploadProcedures(JSONObject data, UploadCallback callback) {
        try {
            JSONObject procedures = data.getJSONObject("procedures");
            WriteBatch batch = db.batch();
            Iterator<String> keys = procedures.keys();

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject procedure = procedures.getJSONObject(key);
                Map<String, Object> procedureMap = jsonToMap(procedure);
                batch.set(db.collection("procedures").document(key), procedureMap);
            }

            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Procedures uploaded: " + procedures.length());
                callback.onSuccess("✓ Procedures uploaded: " + procedures.length() +
                        "\n✓ All data successfully migrated to Firestore!");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading procedures", e);
                callback.onError("Error uploading procedures: " + e.getMessage());
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error processing procedures", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    private String readJsonFromAssets(String fileName) throws Exception {
        InputStream inputStream = context.getAssets().open(fileName);
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private Map<String, Object> jsonToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject) {
                map.put(key, jsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(key, jsonToList((JSONArray) value));
            } else if (value.equals(JSONObject.NULL)) {
                map.put(key, null);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    private java.util.List<Object> jsonToList(JSONArray jsonArray) throws JSONException {
        java.util.List<Object> list = new java.util.ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                list.add(jsonToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                list.add(jsonToList((JSONArray) value));
            } else if (value.equals(JSONObject.NULL)) {
                list.add(null);
            } else {
                list.add(value);
            }
        }
        return list;
    }

    public interface UploadCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
