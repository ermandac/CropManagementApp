package com.cma.thesis.cropmanagementapp;

/**
 * Enhanced Planner Class with Firestore Integration
 * Tracks crop planning with start/end dates, planting method, and notifications
 */
public class Class_Planner {
    private String id;
    private String cropId;
    private String cropName;
    private String startDate;
    private String endDate;
    private String plantingMethod; // SABONG_TANIM or LIPAT_TANIM
    private boolean notificationsEnabled;
    private long createdAt;

    // Enum for Planting Methods
    public enum PlantingMethod {
        SABONG_TANIM("Sabong Tanim", "Direct Seeding"),
        LIPAT_TANIM("Lipat Tanim", "Transplanting");

        private final String tagalog;
        private final String english;

        PlantingMethod(String tagalog, String english) {
            this.tagalog = tagalog;
            this.english = english;
        }

        public String getTagalog() {
            return tagalog;
        }

        public String getEnglish() {
            return english;
        }

        public static PlantingMethod fromString(String value) {
            for (PlantingMethod method : PlantingMethod.values()) {
                if (method.name().equals(value)) {
                    return method;
                }
            }
            return SABONG_TANIM; // Default
        }
    }

    // Constructor for API compatibility (backward)
    public Class_Planner(int id, String cropid, String startdate) {
        this.id = String.valueOf(id);
        this.cropId = cropid;
        this.startDate = startdate;
        this.endDate = "";
        this.plantingMethod = PlantingMethod.SABONG_TANIM.name();
        this.notificationsEnabled = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Full constructor for Firestore
    public Class_Planner(String id, String cropId, String cropName, String startDate, 
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

    // Default constructor for Firestore
    public Class_Planner() {
        this.id = "";
        this.cropId = "";
        this.cropName = "";
        this.startDate = "";
        this.endDate = "";
        this.plantingMethod = PlantingMethod.SABONG_TANIM.name();
        this.notificationsEnabled = false;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCropid() {
        return cropId;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropid(String cropid) {
        this.cropId = cropid;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getStartdate() {
        return startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartdate(String startdate) {
        this.startDate = startdate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlantingMethod() {
        return plantingMethod;
    }

    public void setPlantingMethod(String plantingMethod) {
        this.plantingMethod = plantingMethod;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to get planting method display name
    public String getPlantingMethodDisplay() {
        try {
            PlantingMethod method = PlantingMethod.valueOf(plantingMethod);
            return method.getTagalog() + " (" + method.getEnglish() + ")";
        } catch (Exception e) {
            return "Unknown Method";
        }
    }

    @Override
    public String toString() {
        return "Class_Planner{" +
                "id='" + id + '\'' +
                ", cropId='" + cropId + '\'' +
                ", cropName='" + cropName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", plantingMethod='" + plantingMethod + '\'' +
                ", notificationsEnabled=" + notificationsEnabled +
                ", createdAt=" + createdAt +
                '}';
    }
}
