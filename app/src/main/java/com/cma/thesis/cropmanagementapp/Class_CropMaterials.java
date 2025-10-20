package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 11/25/2018.
 */

public class Class_CropMaterials
{
    public Class_CropMaterials(int id, String materials) {
        this.id = id;
        this.materials = materials;
    }

    int id;
    String materials;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }
}
