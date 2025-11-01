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

    /**
     * Upload fertilizer data from fertilizers_data.json to Firestore
     * TEMPORARY METHOD - Remove after first successful upload
     */
    public void uploadFertilizerData(final UploadCallback callback) {
        try {
            // Read JSON file from assets
            String jsonString = readJsonFromAssets("fertilizers_data.json");
            JSONObject data = new JSONObject(jsonString);
            JSONObject fertilizers = data.getJSONObject("fertilizers");

            Log.d(TAG, "Starting fertilizer data upload...");

            WriteBatch batch = db.batch();
            Iterator<String> keys = fertilizers.keys();
            int count = 0;

            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject fertilizer = fertilizers.getJSONObject(key);
                Map<String, Object> fertilizerMap = jsonToMap(fertilizer);
                batch.set(db.collection("fertilizers").document(key), fertilizerMap);
                count++;
            }

            int finalCount = count;
            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "✓ Fertilizers uploaded: " + finalCount);
                callback.onSuccess("✓ Successfully uploaded " + finalCount + " fertilizers to Firestore!\n" +
                        "You can now remove the uploadFertilizerData() call from your code.");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading fertilizers", e);
                callback.onError("Error uploading fertilizers: " + e.getMessage());
            });

        } catch (Exception e) {
            Log.e(TAG, "Error reading fertilizer data", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    /**
     * Upload suggested crops data from suggested_crops_data.json to Firestore
     * TEMPORARY METHOD - Remove after first successful upload
     */
    public void uploadSuggestedCropsData(final UploadCallback callback) {
        try {
            // Read JSON file from assets (direct array, no wrapper object)
            String jsonString = readJsonFromAssets("suggested_crops_data.json");
            JSONArray cropsArray = new JSONArray(jsonString);

            Log.d(TAG, "Starting suggested crops data upload...");

            WriteBatch batch = db.batch();
            int count = 0;

            for (int i = 0; i < cropsArray.length(); i++) {
                JSONObject crop = cropsArray.getJSONObject(i);
                String docId = crop.getString("id");
                Map<String, Object> cropMap = jsonToMap(crop);
                batch.set(db.collection("suggested_crops").document(docId), cropMap);
                count++;
            }

            int finalCount = count;
            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "✓ Suggested crops uploaded: " + finalCount);
                callback.onSuccess("✓ Successfully uploaded " + finalCount + " suggested crops to Firestore!\n" +
                        "You can now remove the uploadSuggestedCropsData() call from your code.");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading suggested crops", e);
                callback.onError("Error uploading suggested crops: " + e.getMessage());
            });

        } catch (Exception e) {
            Log.e(TAG, "Error reading suggested crops data", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    /**
     * Upload pest data from pest_data.json to Firestore
     * TEMPORARY METHOD - Remove after first successful upload
     */
    public void uploadPestData(final UploadCallback callback) {
        try {
            // Read JSON file from assets (direct array, no wrapper object)
            String jsonString = readJsonFromAssets("pest_data.json");
            JSONArray pestsArray = new JSONArray(jsonString);

            Log.d(TAG, "Starting pest data upload...");

            WriteBatch batch = db.batch();
            int count = 0;

            for (int i = 0; i < pestsArray.length(); i++) {
                JSONObject pest = pestsArray.getJSONObject(i);
                String docId = pest.getString("id");
                Map<String, Object> pestMap = jsonToMap(pest);
                batch.set(db.collection("pests").document(docId), pestMap);
                count++;
            }

            int finalCount = count;
            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "✓ Pests uploaded: " + finalCount);
                callback.onSuccess("✓ Successfully uploaded " + finalCount + " pests to Firestore!\n" +
                        "You can now remove the uploadPestData() call from your code.");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading pests", e);
                callback.onError("Error uploading pests: " + e.getMessage());
            });

        } catch (Exception e) {
            Log.e(TAG, "Error reading pest data", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    /**
     * Upload organic farming data from organic_farming_data.json to Firestore
     * TEMPORARY METHOD - Remove after first successful upload
     */
    public void uploadOrganicFarmingData(final UploadCallback callback) {
        try {
            // Read JSON file from assets (direct array, no wrapper object)
            String jsonString = readJsonFromAssets("organic_farming_data.json");
            JSONArray organicArray = new JSONArray(jsonString);

            Log.d(TAG, "Starting organic farming data upload...");

            WriteBatch batch = db.batch();
            int count = 0;

            for (int i = 0; i < organicArray.length(); i++) {
                JSONObject organicItem = organicArray.getJSONObject(i);
                String docId = organicItem.getString("id");
                Map<String, Object> organicMap = jsonToMap(organicItem);
                batch.set(db.collection("organic_farming").document(docId), organicMap);
                count++;
            }

            int finalCount = count;
            batch.commit().addOnSuccessListener(aVoid -> {
                Log.d(TAG, "✓ Organic farming items uploaded: " + finalCount);
                callback.onSuccess("✓ Successfully uploaded " + finalCount + " organic farming items to Firestore!\n" +
                        "You can now remove the uploadOrganicFarmingData() call from your code.");
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Error uploading organic farming data", e);
                callback.onError("Error uploading organic farming data: " + e.getMessage());
            });

        } catch (Exception e) {
            Log.e(TAG, "Error reading organic farming data", e);
            callback.onError("Error: " + e.getMessage());
        }
    }

    /**
     * Bulk upload all data to Firestore
     * This method uploads all categories: crops, fertilizers, pests, organic farming, and suggested crops
     * Use this for initial setup or re-seeding the database
     */
    public void uploadAllData(final UploadCallback callback) {
        Log.d(TAG, "=== Starting bulk upload of all data to Firestore ===");
        
        final StringBuilder resultsLog = new StringBuilder();
        resultsLog.append("Bulk Upload Results:\n");
        resultsLog.append("====================\n");

        // Step 1: Upload migrated data (categories, crops, procedures)
        uploadMigratedData(new UploadCallback() {
            @Override
            public void onSuccess(String message) {
                Log.d(TAG, "Step 1/5 Complete: " + message);
                resultsLog.append("✓ Migrated Data: ").append(message).append("\n");
                
                // Step 2: Upload fertilizers
                uploadFertilizerData(new UploadCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.d(TAG, "Step 2/5 Complete: " + message);
                        resultsLog.append("✓ Fertilizers: ").append(message).append("\n");
                        
                        // Step 3: Upload pests
                        uploadPestData(new UploadCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Log.d(TAG, "Step 3/5 Complete: " + message);
                                resultsLog.append("✓ Pests: ").append(message).append("\n");
                                
                                // Step 4: Upload organic farming
                                uploadOrganicFarmingData(new UploadCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Log.d(TAG, "Step 4/5 Complete: " + message);
                                        resultsLog.append("✓ Organic Farming: ").append(message).append("\n");
                                        
                                        // Step 5: Upload suggested crops
                                        uploadSuggestedCropsData(new UploadCallback() {
                                            @Override
                                            public void onSuccess(String message) {
                                                Log.d(TAG, "Step 5/5 Complete: " + message);
                                                resultsLog.append("✓ Suggested Crops: ").append(message).append("\n");
                                                resultsLog.append("\n=== ALL DATA UPLOADED SUCCESSFULLY ===\n");
                                                resultsLog.append("You can now comment out the uploadAllData() call.");
                                                
                                                callback.onSuccess(resultsLog.toString());
                                            }
                                            
                                            @Override
                                            public void onError(String error) {
                                                resultsLog.append("✗ Suggested Crops: ").append(error).append("\n");
                                                callback.onError(resultsLog.toString());
                                            }
                                        });
                                    }
                                    
                                    @Override
                                    public void onError(String error) {
                                        resultsLog.append("✗ Organic Farming: ").append(error).append("\n");
                                        callback.onError(resultsLog.toString());
                                    }
                                });
                            }
                            
                            @Override
                            public void onError(String error) {
                                resultsLog.append("✗ Pests: ").append(error).append("\n");
                                callback.onError(resultsLog.toString());
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        resultsLog.append("✗ Fertilizers: ").append(error).append("\n");
                        callback.onError(resultsLog.toString());
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                resultsLog.append("✗ Migrated Data: ").append(error).append("\n");
                callback.onError(resultsLog.toString());
            }
        });
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
