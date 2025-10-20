package com.cma.thesis.cropmanagementapp;

/**
 * Created by Manzano on 11/26/2018.
 */

public class Class_Fertilizer {
    String category,chemical;

    public Class_Fertilizer(String category, String chemical) {
        this.category = category;
        this.chemical = chemical;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChemical() {
        return chemical;
    }

    public void setChemical(String chemical) {
        this.chemical = chemical;
    }

}
