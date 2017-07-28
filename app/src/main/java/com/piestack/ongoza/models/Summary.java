package com.piestack.ongoza.models;

public class Summary {
    private String id;
    private String stheme;
    private String hrs_bds;
    private String hrs_travel;
    private String partners;

    public Summary() {
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getStheme() {
        return stheme;
    }
 
    public void setStheme(String stheme) {
        this.stheme = stheme;
    }

    public String getHrs_bds() {
        return hrs_bds;
    }

    public void setHrs_bds(String hrs_bds) {
        this.hrs_bds = hrs_bds;
    }

    public String getHrs_travel() {
        return hrs_travel;
    }

    public void setHrs_travel(String hrs_travel) {
        this.hrs_travel = hrs_travel;
    }

    public String getPartners() {
        return partners;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }
}