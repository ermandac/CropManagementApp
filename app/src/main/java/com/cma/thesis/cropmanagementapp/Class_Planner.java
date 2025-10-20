package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 9/9/2018.
 */

public class Class_Planner {
    int id;
    String cropid;
    String startdate;

    public Class_Planner(int id, String cropid, String startdate) {
        this.id = id;
        this.cropid = cropid;
        this.startdate = startdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCropid() {
        return cropid;
    }

    public void setCropid(String cropid) {
        this.cropid = cropid;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
}
