package com.cma.thesis.cropmanagementapp;



public class Class_Procedure
{
    private int id;
    private int crop_id;
    private int daysNotif;
    private int step;
    private String procedure;
    private String datenofication;
    private String status;


    public Class_Procedure(int id, int crop_id, int daysNotif, int step, String procedure,String datenofication,String status) {
        this.id = id;
        this.crop_id = crop_id;
        this.daysNotif = daysNotif;
        this.step = step;
        this.procedure = procedure;
        this.datenofication = datenofication;
        this.status = status;

    }

    //setter getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCrop_id() {
        return crop_id;
    }

    public void setCrop_id(int crop_id) {
        this.crop_id = crop_id;
    }

    public int getDaysNotif() {
        return daysNotif;
    }

    public void setDaysNotif(int daysNotif) {
        this.daysNotif = daysNotif;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getdatenofication() {
    return datenofication;
}

    public void setdatenofication(String datenofication) {
        this.datenofication = datenofication;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}