package com.cma.thesis.cropmanagementapp;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FirestorePlannerHelper
 * Handles Firestore operations for crop planning
 * Manages plan creation, reading, updating, and deletion
 */
public class FirestorePlannerHelper {
    private static final String TAG = "FirestorePlannerHelper";
    private static final String COLLECTION_PLANS = "plans";
    private static final String COLLECTION_PROCEDURES = "procedures";
    private FirebaseFirestore db;

    public FirestorePlannerHelper() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Create a new plan in Firestore
     */
    public void createPlan(String cropId, String cropName, String startDate, String endDate,
                          String plantingMethod, boolean notificationsEnabled,
                          PlanCallback callback) {
        try {
            String planId = db.collection(COLLECTION_PLANS).document().getId();
            
            Map<String, Object> planData = new HashMap<>();
            planData.put("id", planId);
            planData.put("cropId", cropId);
            planData.put("cropName", cropName);
            planData.put("startDate", startDate);
            planData.put("endDate", endDate);
            planData.put("plantingMethod", plantingMethod);
            planData.put("notificationsEnabled", notificationsEnabled);
            planData.put("createdAt", FieldValue.serverTimestamp());

            db.collection(COLLECTION_PLANS).document(planId)
                    .set(planData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Plan created successfully: " + planId);
                        callback.onSuccess("Plan created successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error creating plan: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in createPlan: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Load all plans for a specific crop
     */
    public void loadPlansByCropId(String cropId, PlanLoadCallback callback) {
        try {
            db.collection(COLLECTION_PLANS)
                    .whereEqualTo("cropId", cropId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        ArrayList<Class_Planner> plans = new ArrayList<>();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                try {
                                    Class_Planner plan = documentToPlan(doc);
                                    if (plan != null) {
                                        plans.add(plan);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting document: " + e.getMessage());
                                }
                            }
                        }
                        Log.d(TAG, "Loaded " + plans.size() + " plans for crop: " + cropId);
                        callback.onSuccess(plans);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading plans: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in loadPlansByCropId: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Get all plans regardless of crop
     */
    public void getAllPlans(PlanLoadCallback callback) {
        try {
            db.collection(COLLECTION_PLANS)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        ArrayList<Class_Planner> plans = new ArrayList<>();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                try {
                                    Class_Planner plan = documentToPlan(doc);
                                    if (plan != null) {
                                        plans.add(plan);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting document: " + e.getMessage());
                                }
                            }
                        }
                        Log.d(TAG, "Loaded " + plans.size() + " total plans");
                        callback.onSuccess(plans);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading all plans: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in getAllPlans: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Update an existing plan
     */
    public void updatePlan(String planId, String startDate, String endDate,
                          String plantingMethod, boolean notificationsEnabled,
                          PlanCallback callback) {
        try {
            Map<String, Object> updates = new HashMap<>();
            updates.put("startDate", startDate);
            updates.put("endDate", endDate);
            updates.put("plantingMethod", plantingMethod);
            updates.put("notificationsEnabled", notificationsEnabled);

            db.collection(COLLECTION_PLANS).document(planId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Plan updated successfully: " + planId);
                        callback.onSuccess("Plan updated successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating plan: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in updatePlan: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Delete a plan
     */
    public void deletePlan(String planId, PlanCallback callback) {
        try {
            db.collection(COLLECTION_PLANS).document(planId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Plan deleted successfully: " + planId);
                        callback.onSuccess("Plan deleted successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error deleting plan: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in deletePlan: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Get procedure steps for a crop plan
     */
    public void getPlanSteps(int cropId, String startDate, StepsCallback callback) {
        try {
            db.collection(COLLECTION_PROCEDURES)
                    .whereEqualTo("crop_id", cropId)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        ArrayList<Class_Procedure> steps = new ArrayList<>();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                try {
                                    int id = Math.toIntExact(doc.getLong("id") != null ? doc.getLong("id") : 0);
                                    int crop_id = Math.toIntExact(doc.getLong("crop_id") != null ? doc.getLong("crop_id") : 0);
                                    int daysNotif = Math.toIntExact(doc.getLong("daysNotif") != null ? doc.getLong("daysNotif") : 0);
                                    int step = Math.toIntExact(doc.getLong("step") != null ? doc.getLong("step") : 0);
                                    String procedure = doc.getString("procedure") != null ? doc.getString("procedure") : "";
                                    String dateNotification = calculateNotificationDate(startDate, daysNotif);
                                    String status = doc.getString("status") != null ? doc.getString("status") : "pending";

                                    Class_Procedure proc = new Class_Procedure(id, crop_id, daysNotif, step, procedure, dateNotification, status);
                                    steps.add(proc);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting procedure: " + e.getMessage());
                                }
                            }
                        }
                        Log.d(TAG, "Loaded " + steps.size() + " steps for crop: " + cropId);
                        callback.onSuccess(steps);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading steps: " + e.getMessage());
                        callback.onError("Error: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in getPlanSteps: " + e.getMessage());
            callback.onError("Exception: " + e.getMessage());
        }
    }

    /**
     * Calculate notification date from start date + days
     */
    private String calculateNotificationDate(String startDate, int daysToAdd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date date = sdf.parse(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            Log.e(TAG, "Error calculating notification date: " + e.getMessage());
            return startDate;
        }
    }

    /**
     * Convert Firestore DocumentSnapshot to Class_Planner object
     */
    private Class_Planner documentToPlan(DocumentSnapshot doc) {
        try {
            String id = doc.getString("id") != null ? doc.getString("id") : doc.getId();
            String cropId = doc.getString("cropId") != null ? doc.getString("cropId") : "";
            String cropName = doc.getString("cropName") != null ? doc.getString("cropName") : "";
            String startDate = doc.getString("startDate") != null ? doc.getString("startDate") : "";
            String endDate = doc.getString("endDate") != null ? doc.getString("endDate") : "";
            String plantingMethod = doc.getString("plantingMethod") != null ? doc.getString("plantingMethod") : "SABONG_TANIM";
            boolean notificationsEnabled = doc.getBoolean("notificationsEnabled") != null ? doc.getBoolean("notificationsEnabled") : false;
            long createdAt = System.currentTimeMillis();
            if (doc.getTimestamp("createdAt") != null) {
                createdAt = doc.getTimestamp("createdAt").getSeconds() * 1000;
            }

            return new Class_Planner(id, cropId, cropName, startDate, endDate, plantingMethod, notificationsEnabled, createdAt);
        } catch (Exception e) {
            Log.e(TAG, "Error creating Class_Planner from document: " + e.getMessage());
            return null;
        }
    }

    /**
     * Callback interface for plan operations (create, update, delete)
     */
    public interface PlanCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    /**
     * Callback interface for plan loading operations
     */
    public interface PlanLoadCallback {
        void onSuccess(ArrayList<Class_Planner> plans);
        void onError(String error);
    }

    /**
     * Callback interface for procedure steps loading
     */
    public interface StepsCallback {
        void onSuccess(ArrayList<Class_Procedure> steps);
        void onError(String error);
    }
}
