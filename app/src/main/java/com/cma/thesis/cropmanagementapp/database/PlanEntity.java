package com.cma.thesis.cropmanagementapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Room Entity for storing crop plans locally
 * Replaces Firestore storage for private user plans
 */
@Entity(tableName = "plans")
public class PlanEntity {
    @PrimaryKey
    @NonNull
    private String id;
    
    private String cropId;
    private String cropName;
    private String startDate;
    private String endDate;
    private String plantingMethod;
    private boolean notificationsEnabled;
    private long createdAt;

    // Constructor
    public PlanEntity(@NonNull String id, String cropId, String cropName, String startDate, 
                     String endDate, String plantingMethod, boolean notificationsEnabled, long createdAt) {
        this.id = id;
        this.cropId = cropId;
        this.cropName = cropName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.plantingMethod = plantingMethod;
        this.notificationsEnabled = notificationsEnabled;
        this.createdAt = createdAt;
    }

    // Getters
    @NonNull
    public String getId() {
        return id;
    }

    public String getCropId() {
        return cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPlantingMethod() {
        return plantingMethod;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPlantingMethod(String plantingMethod) {
        this.plantingMethod = plantingMethod;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
