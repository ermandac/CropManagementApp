package com.cma.thesis.cropmanagementapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.cma.thesis.cropmanagementapp.database.AppDatabase;
import com.cma.thesis.cropmanagementapp.database.PlanDao;
import com.cma.thesis.cropmanagementapp.database.PlanEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * LocalPlannerHelper
 * Handles local Room database operations for crop planning
 * Replaces FirestorePlannerHelper for local-first storage
 */
public class LocalPlannerHelper {
    private static final String TAG = "LocalPlannerHelper";
    private PlanDao planDao;
    private Context context;

    public LocalPlannerHelper(Context context) {
        this.context = context.getApplicationContext();
        this.planDao = AppDatabase.getInstance(context).planDao();
    }

    /**
     * Create a new plan in local database
     */
    public void createPlan(String cropId, String cropName, String startDate, String endDate,
                          String plantingMethod, boolean notificationsEnabled,
                          PlanCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    String planId = UUID.randomUUID().toString();
                    long createdAt = System.currentTimeMillis();
                    
                    PlanEntity plan = new PlanEntity(
                        planId, cropId, cropName, startDate, endDate,
                        plantingMethod, notificationsEnabled, createdAt
                    );
                    
                    planDao.insert(plan);
                    Log.d(TAG, "Plan created successfully: " + planId);
                    return new Result(true, "Plan created successfully", null);
                } catch (Exception e) {
                    Log.e(TAG, "Error creating plan: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Load all plans for a specific crop
     */
    public void loadPlansByCropId(String cropId, PlanLoadCallback callback) {
        new AsyncTask<Void, Void, PlansResult>() {
            @Override
            protected PlansResult doInBackground(Void... voids) {
                try {
                    List<PlanEntity> entities = planDao.getPlansByCropId(cropId);
                    ArrayList<Class_Planner> plans = new ArrayList<>();
                    
                    for (PlanEntity entity : entities) {
                        Class_Planner plan = entityToPlanner(entity);
                        plans.add(plan);
                    }
                    
                    Log.d(TAG, "Loaded " + plans.size() + " plans for crop: " + cropId);
                    return new PlansResult(true, plans, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading plans: " + e.getMessage());
                    return new PlansResult(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(PlansResult result) {
                if (result.success) {
                    callback.onSuccess(result.plans);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Get all plans regardless of crop
     */
    public void getAllPlans(PlanLoadCallback callback) {
        new AsyncTask<Void, Void, PlansResult>() {
            @Override
            protected PlansResult doInBackground(Void... voids) {
                try {
                    List<PlanEntity> entities = planDao.getAllPlans();
                    ArrayList<Class_Planner> plans = new ArrayList<>();
                    
                    for (PlanEntity entity : entities) {
                        Class_Planner plan = entityToPlanner(entity);
                        plans.add(plan);
                    }
                    
                    Log.d(TAG, "Loaded " + plans.size() + " total plans");
                    return new PlansResult(true, plans, null);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading all plans: " + e.getMessage());
                    return new PlansResult(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(PlansResult result) {
                if (result.success) {
                    callback.onSuccess(result.plans);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Update an existing plan
     */
    public void updatePlan(String planId, String startDate, String endDate,
                          String plantingMethod, boolean notificationsEnabled,
                          PlanCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    PlanEntity plan = planDao.getPlanById(planId);
                    if (plan != null) {
                        plan.setStartDate(startDate);
                        plan.setEndDate(endDate);
                        plan.setPlantingMethod(plantingMethod);
                        plan.setNotificationsEnabled(notificationsEnabled);
                        planDao.update(plan);
                        Log.d(TAG, "Plan updated successfully: " + planId);
                        return new Result(true, "Plan updated successfully", null);
                    } else {
                        return new Result(false, null, "Plan not found");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error updating plan: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Update only the dates of a plan
     */
    public void updatePlanDates(String planId, String startDate, String endDate, PlanCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    planDao.updateDates(planId, startDate, endDate);
                    Log.d(TAG, "Plan dates updated successfully: " + planId);
                    return new Result(true, "Plan dates updated successfully", null);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating plan dates: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Update only the planting method of a plan
     */
    public void updatePlantingMethod(String planId, String plantingMethod, PlanCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    planDao.updatePlantingMethod(planId, plantingMethod);
                    Log.d(TAG, "Planting method updated successfully: " + planId);
                    return new Result(true, "Planting method updated successfully", null);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating planting method: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Delete a plan
     */
    public void deletePlan(String planId, PlanCallback callback) {
        new AsyncTask<Void, Void, Result>() {
            @Override
            protected Result doInBackground(Void... voids) {
                try {
                    planDao.deleteById(planId);
                    Log.d(TAG, "Plan deleted successfully: " + planId);
                    return new Result(true, "Plan deleted successfully", null);
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting plan: " + e.getMessage());
                    return new Result(false, null, "Error: " + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                if (result.success) {
                    callback.onSuccess(result.message);
                } else {
                    callback.onError(result.error);
                }
            }
        }.execute();
    }

    /**
     * Convert PlanEntity to Class_Planner
     */
    private Class_Planner entityToPlanner(PlanEntity entity) {
        return new Class_Planner(
            entity.getId(),
            entity.getCropId(),
            entity.getCropName(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPlantingMethod(),
            entity.isNotificationsEnabled(),
            entity.getCreatedAt()
        );
    }

    /**
     * Result wrapper for single operations
     */
    private static class Result {
        boolean success;
        String message;
        String error;

        Result(boolean success, String message, String error) {
            this.success = success;
            this.message = message;
            this.error = error;
        }
    }

    /**
     * Result wrapper for plan list operations
     */
    private static class PlansResult {
        boolean success;
        ArrayList<Class_Planner> plans;
        String error;

        PlansResult(boolean success, ArrayList<Class_Planner> plans, String error) {
            this.success = success;
            this.plans = plans;
            this.error = error;
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
}
