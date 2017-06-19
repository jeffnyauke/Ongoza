package com.piestack.ongoza.models;

/**
 * Created by Jeffrey Nyauke on 6/7/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    // TODO: 6/9/2017 Return id from internet

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("county_id")
    @Expose
    private Integer county_id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("assigned_at")
    @Expose
    private String assignedAt;

    public User() {
    }

    public User(Integer id, String name, String email, Integer county_id, String createdAt, String assignedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.county_id = county_id;
        this.createdAt = createdAt;
        this.assignedAt = assignedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCounty_id() {
        return county_id;
    }

    public void setCounty_id(Integer county_id) {
        this.county_id = county_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(String assignedAt) {
        this.assignedAt = assignedAt;
    }

}