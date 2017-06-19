package com.piestack.ongoza.models;

public class Options {
    private int id;
    private String title;
    private String subtitle;
    private int picture;
    private boolean isImportant;
    private int color = -1;

    public Options() {
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getSubtitle() {
        return subtitle;
    }
 
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

 
    public boolean isImportant() {
        return isImportant;
    }
 
    public void setImportant(boolean important) {
        isImportant = important;
    }
 
    public int getPicture() {
        return picture;
    }
 
    public void setPicture(int picture) {
        this.picture = picture;
    }
 
    public int getColor() {
        return color;
    }
 
    public void setColor(int color) {
        this.color = color;
    }
}