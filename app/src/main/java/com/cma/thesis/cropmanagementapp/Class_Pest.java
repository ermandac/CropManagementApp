package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 9/9/2018.
 */

public class Class_Pest {
    int id;
    String pestname;
    String pesticide;
    String image;

    public Class_Pest(int id, String pestname, String image) {

        this.id = id;
        this.pestname = pestname;
        //this.pesticide = pesticide;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPestname() {
        return pestname;
    }

    public void setPestname(String pestname) {
        this.pestname = pestname;
    }

    public String getPesticide() {
        return pesticide;
    }

    public void setPesticide(String pesticide) {
        this.pesticide = pesticide;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
