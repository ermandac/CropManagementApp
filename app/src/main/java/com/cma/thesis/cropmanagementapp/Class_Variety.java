package com.cma.thesis.cropmanagementapp;

/**
 * Created by Mimoy on 11/24/2018.
 */

public class Class_Variety
{
    int cropid;
    String variety;

    public Class_Variety(int cropid, String variety) {
        this.cropid = cropid;
        this.variety = variety;
    }

    public int getCropid() {
        return cropid;
    }

    public void setCropid(int cropid) {
        this.cropid = cropid;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
}
