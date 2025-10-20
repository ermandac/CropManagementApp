package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 8/11/2018.
 */

public class testa {
    private int id;
    private String crop_name;
    private String description;
   // private double image;
    private String science_name;
    private String duration;

    public testa(int id, String crop_name, String description, String science_name, String duration) {
        this.id = id;
        this.crop_name = crop_name;
        this.description = description;
        //this.image = image;
        this.science_name = science_name;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return crop_name;
    }

    public String getShortdesc() {
        return description;
    }



    public String getPrice() {
        return science_name;
    }

    public String getImage() {
        return duration;
    }
}
