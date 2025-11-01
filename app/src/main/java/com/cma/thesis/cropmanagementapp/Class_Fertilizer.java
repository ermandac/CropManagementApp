package com.cma.thesis.cropmanagementapp;

import java.util.List;

/**
 * Created by Manzano on 11/26/2018.
 * Updated for Firestore migration - expanded to include full fertilizer data
 */

public class Class_Fertilizer {
    private String id;
    private String name;
    private String npk;
    private String category;
    private String description;
    private String benefits;
    private List<String> bestFor;
    private String application;
    private String timing;
    private String precautions;
    private String priceRange;
    private boolean isOrganic;
    private String popularity;

    // Empty constructor required for Firestore
    public Class_Fertilizer() {
    }

    // Legacy constructor for backward compatibility
    public Class_Fertilizer(String category, String chemical) {
        this.category = category;
        this.name = chemical;
    }

    // Full constructor
    public Class_Fertilizer(String id, String name, String npk, String category, String description, 
                           String benefits, List<String> bestFor, String application, String timing, 
                           String precautions, String priceRange, boolean isOrganic, String popularity) {
        this.id = id;
        this.name = name;
        this.npk = npk;
        this.category = category;
        this.description = description;
        this.benefits = benefits;
        this.bestFor = bestFor;
        this.application = application;
        this.timing = timing;
        this.precautions = precautions;
        this.priceRange = priceRange;
        this.isOrganic = isOrganic;
        this.popularity = popularity;
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

    public String getNpk() {
        return npk;
    }

    public void setNpk(String npk) {
        this.npk = npk;
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

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public List<String> getBestFor() {
        return bestFor;
    }

    public void setBestFor(List<String> bestFor) {
        this.bestFor = bestFor;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public boolean isOrganic() {
        return isOrganic;
    }

    public void setOrganic(boolean organic) {
        isOrganic = organic;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    // Legacy method for backward compatibility
    public String getChemical() {
        return name;
    }

    public void setChemical(String chemical) {
        this.name = chemical;
    }
}
