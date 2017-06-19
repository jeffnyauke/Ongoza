package com.piestack.ongoza.models;

public class Report {
    private int id;
    private String from;
    private String subject;
    private String message;
    private String timestamp;
    private String picture;
    private boolean isImportant;
    private boolean isRead;
    private int color = -1;
 
    public Report() {
    }
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
 
    public String getTitle() {
        return from;
    }
 
    public void setTitle(String title) {
        this.from = title;
    }
 
    public String getSubject() {
        return subject;
    }
 
    public void setSubject(String subject) {
        this.subject = subject;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
 
    public String getTimestamp() {
        return timestamp;
    }
 
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
 
    public boolean isImportant() {
        return isImportant;
    }
 
    public void setImportant(boolean important) {
        isImportant = important;
    }
 
    public String getPicture() {
        return picture;
    }
 
    public void setPicture(String picture) {
        this.picture = picture;
    }
 
    public boolean isRead() {
        return isRead;
    }
 
    public void setRead(boolean read) {
        isRead = read;
    }
 
    public int getColor() {
        return color;
    }
 
    public void setColor(int color) {
        this.color = color;
    }
}