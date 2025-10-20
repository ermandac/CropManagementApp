package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 11/24/2018.
 */

public class Class_Weeds
{
    public int getCropid() {
        return cropid;
    }

    public void setCropid(int cropid) {
        this.cropid = cropid;
    }

    public String getWeedcontrol() {
        return weedcontrol;
    }

    public void setWeedcontrol(String weedcontrol) {
        this.weedcontrol = weedcontrol;
    }

    private int cropid;
    private String weedcontrol;

    public Class_Weeds(int cropid, String weedcontrol) {
        this.cropid = cropid;
        this.weedcontrol = weedcontrol;
    }
}
