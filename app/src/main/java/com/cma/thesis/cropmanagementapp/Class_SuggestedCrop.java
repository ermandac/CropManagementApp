package com.cma.thesis.cropmanagementapp;

import java.util.List;

public class Class_SuggestedCrop {
    private String id;
    private String cropName;
    private String month;
    private String category;
    private String plantingReason;
    private String description;
    private List<String> bestVarieties;
    private String expectedHarvest;
    private String tips;
    private String popularity;

    // Default constructor required for Firestore
    public Class_SuggestedCrop() {
    }

    // Constructor with all fields
    public Class_SuggestedCrop(String id, String cropName, String month, String category,
                               String plantingReason, String description, List<String> bestVarieties,
                               String expectedHarvest, String tips, String popularity) {
        this.id = id;
        this.cropName = cropName;
        this.month = month;
        this.category = category;
        this.plantingReason = plantingReason;
        this.description = description;
        this.bestVarieties = bestVarieties;
        this.expectedHarvest = expectedHarvest;
        this.tips = tips;
        this.popularity = popularity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCropName() {
        return cropName;
    }

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public String getPlantingReason() {
        return plantingReason;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getBestVarieties() {
        return bestVarieties;
    }

    public String getExpectedHarvest() {
        return expectedHarvest;
    }

    public String getTips() {
        return tips;
    }

    public String getPopularity() {
        return popularity;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPlantingReason(String plantingReason) {
        this.plantingReason = plantingReason;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBestVarieties(List<String> bestVarieties) {
        this.bestVarieties = bestVarieties;
    }

    public void setExpectedHarvest(String expectedHarvest) {
        this.expectedHarvest = expectedHarvest;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    // Helper method to get varieties as a formatted string
    public String getVarietiesString() {
        if (bestVarieties == null || bestVarieties.isEmpty()) {
            return "No varieties listed";
        }
        return String.join(", ", bestVarieties);
    }
}
