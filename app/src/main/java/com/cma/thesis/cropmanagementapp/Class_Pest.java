package com.cma.thesis.cropmanagementapp;

import java.util.List;

/**
 * Class_Pest - Model for pest data from Firestore
 * Represents agricultural pest information for Filipino farmers
 */
public class Class_Pest {
    private String id;
    private String pestName;
    private String scientificName;
    private String category;
    private List<String> affectedCrops;
    private String description;
    private String symptoms;
    private List<String> controlMethods;
    private String preventionTips;
    private String severity;
    private String commonSeason;

    // Default constructor required for Firestore
    public Class_Pest() {
    }

    // Legacy constructor for backwards compatibility with Activity_PestList
    public Class_Pest(int id, String pestName, String image) {
        this.id = String.valueOf(id);
        this.pestName = pestName;
        this.description = image; // Using description field to store image for legacy code
    }

    // Full constructor
    public Class_Pest(String id, String pestName, String scientificName, String category,
                     List<String> affectedCrops, String description, String symptoms,
                     List<String> controlMethods, String preventionTips, String severity,
                     String commonSeason) {
        this.id = id;
        this.pestName = pestName;
        this.scientificName = scientificName;
        this.category = category;
        this.affectedCrops = affectedCrops;
        this.description = description;
        this.symptoms = symptoms;
        this.controlMethods = controlMethods;
        this.preventionTips = preventionTips;
        this.severity = severity;
        this.commonSeason = commonSeason;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPestName() {
        return pestName;
    }

    public void setPestName(String pestName) {
        this.pestName = pestName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getAffectedCrops() {
        return affectedCrops;
    }

    public void setAffectedCrops(List<String> affectedCrops) {
        this.affectedCrops = affectedCrops;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public List<String> getControlMethods() {
        return controlMethods;
    }

    public void setControlMethods(List<String> controlMethods) {
        this.controlMethods = controlMethods;
    }

    public String getPreventionTips() {
        return preventionTips;
    }

    public void setPreventionTips(String preventionTips) {
        this.preventionTips = preventionTips;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCommonSeason() {
        return commonSeason;
    }

    public void setCommonSeason(String commonSeason) {
        this.commonSeason = commonSeason;
    }

    // Helper methods
    public String getAffectedCropsString() {
        if (affectedCrops == null || affectedCrops.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", affectedCrops);
    }

    public String getControlMethodsString() {
        if (controlMethods == null || controlMethods.isEmpty()) {
            return "N/A";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < controlMethods.size(); i++) {
            sb.append((i + 1)).append(". ").append(controlMethods.get(i));
            if (i < controlMethods.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
