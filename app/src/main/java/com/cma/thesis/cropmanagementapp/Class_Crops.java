package com.cma.thesis.cropmanagementapp;



public class Class_Crops
{
    //private byte[] image;
    int id;
    int category_id;
    String cropname;
    String desc;
    String scienceName;
    String duration;
    String varieties;
    String soil_climate;
    String season;
    String materials ;
    String main_field ;
    String weedControl;
    String irrigation;
    String growth_management;
    String harvesting;
    String image;



    public Class_Crops(int id, int category_id, String cropname, String desc, String scienceName, String duration, String varieties, String soil_climate,
                       String season, String main_field, String irrigation, String growth_management, String harvesting,String image, String weedControl, String materials) {

        this.id = id;
        this.category_id = category_id;
        this.cropname = cropname;
        this.desc = desc;
        this.scienceName = scienceName;
        this.duration = duration;
        this.varieties = varieties;
        this.soil_climate = soil_climate;
        this.season = season;
        this.materials = materials;
        this.main_field = main_field;
        this.weedControl = weedControl;
        this.irrigation = irrigation;
        this.growth_management = growth_management;
        this.harvesting = harvesting;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getscienceName() {
        return scienceName;
    }

    public void setscienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVarieties() {
        return varieties;
    }

    public void setVarieties(String varieties) {
        this.varieties = varieties;
    }

    public String getSoil_climate() {
        return soil_climate;
    }

    public void setSoil_climate(String soil_climate) {
        this.soil_climate = soil_climate;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getMain_field() {
        return main_field;
    }

    public void setMain_field(String main_field) {
        this.main_field = main_field;
    }

    public String getweedControl() {
        return weedControl;
    }

    public void setweedControl(String weed_contro) {
        this.weedControl = weed_contro;
    }

    public String getIrrigation() {
        return irrigation;
    }

    public void setIrrigation(String irrigation) {
        this.irrigation = irrigation;
    }

    public String getGrowth_management() {
        return growth_management;
    }

    public void setGrowth_management(String growth_management) {
        this.growth_management = growth_management;
    }

    public String getHarvesting() {
        return harvesting;
    }

    public void setHarvesting(String harvesting) {
        this.harvesting = harvesting;
    }
}
