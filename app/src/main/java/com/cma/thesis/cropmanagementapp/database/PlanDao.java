package com.cma.thesis.cropmanagementapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

/**
 * Data Access Object for Plan operations
 * Provides CRUD operations for local plan storage
 */
@Dao
public interface PlanDao {
    
    /**
     * Insert a new plan
     */
    @Insert
    void insert(PlanEntity plan);
    
    /**
     * Update an existing plan
     */
    @Update
    void update(PlanEntity plan);
    
    /**
     * Delete a plan
     */
    @Delete
    void delete(PlanEntity plan);
    
    /**
     * Delete a plan by ID
     */
    @Query("DELETE FROM plans WHERE id = :planId")
    void deleteById(String planId);
    
    /**
     * Get all plans
     */
    @Query("SELECT * FROM plans ORDER BY createdAt DESC")
    List<PlanEntity> getAllPlans();
    
    /**
     * Get plans for a specific crop
     */
    @Query("SELECT * FROM plans WHERE cropId = :cropId ORDER BY createdAt DESC")
    List<PlanEntity> getPlansByCropId(String cropId);
    
    /**
     * Get a single plan by ID
     */
    @Query("SELECT * FROM plans WHERE id = :planId LIMIT 1")
    PlanEntity getPlanById(String planId);
    
    /**
     * Get plans with notifications enabled
     */
    @Query("SELECT * FROM plans WHERE notificationsEnabled = 1 ORDER BY startDate ASC")
    List<PlanEntity> getPlansWithNotifications();
    
    /**
     * Update plan dates
     */
    @Query("UPDATE plans SET startDate = :startDate, endDate = :endDate WHERE id = :planId")
    void updateDates(String planId, String startDate, String endDate);
    
    /**
     * Update planting method
     */
    @Query("UPDATE plans SET plantingMethod = :plantingMethod WHERE id = :planId")
    void updatePlantingMethod(String planId, String plantingMethod);
    
    /**
     * Update notification status
     */
    @Query("UPDATE plans SET notificationsEnabled = :enabled WHERE id = :planId")
    void updateNotificationStatus(String planId, boolean enabled);
    
    /**
     * Get count of all plans
     */
    @Query("SELECT COUNT(*) FROM plans")
    int getPlansCount();
    
    /**
     * Delete all plans (for testing/reset)
     */
    @Query("DELETE FROM plans")
    void deleteAllPlans();
}
