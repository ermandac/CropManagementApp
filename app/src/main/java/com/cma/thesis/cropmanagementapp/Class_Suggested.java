package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 11/18/2018.
 */

public class Class_Suggested {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    private int id;
    private String cropname;
    private String image;
    private String month;

    public Class_Suggested(int id, String cropname, String image, String month) {
        this.id = id;
        this.cropname = cropname;
        this.image = image;
        this.month = month;
    }

}
