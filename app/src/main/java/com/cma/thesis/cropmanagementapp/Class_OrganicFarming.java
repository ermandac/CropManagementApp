package com.cma.thesis.cropmanagementapp;

import java.util.List;

public class Class_OrganicFarming {
    private String id;
    private String name;
    private String type;
    private String category;
    private String description;
    private List<String> materials;
    private String benefits;
    private String applicationMethod;
    private String duration;

    // Default constructor for Firestore
    public Class_OrganicFarming() {
    }

    // Full constructor
    public Class_OrganicFarming(String id, String name, String type, String category, 
                               String description, List<String> materials, String benefits, 
                               String applicationMethod, String duration) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.description = description;
        this.materials = materials;
        this.benefits = benefits;
        this.applicationMethod = applicationMethod;
        this.duration = duration;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // Helper method to get materials as a formatted string
    public String getMaterialsString() {
        if (materials == null || materials.isEmpty()) {
            return "N/A";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < materials.size(); i++) {
            sb.append("• ").append(materials.get(i));
            if (i < materials.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
