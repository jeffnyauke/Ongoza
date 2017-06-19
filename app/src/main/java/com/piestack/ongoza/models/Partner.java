package com.piestack.ongoza.models;

/**
 * Created by Jeffrey Nyauke on 6/8/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Partner {

    @SerializedName("p_id")
    @Expose
    private String pId;
    @SerializedName("p_name")
    @Expose
    private String pName;
    @SerializedName("p_email")
    @Expose
    private String pEmail;
    @SerializedName("p_address")
    @Expose
    private String pAddress;
    @SerializedName("county_id")
    @Expose
    private String countyId;
    @SerializedName("l_name")
    @Expose
    private String lName;
    @SerializedName("l_phone")
    @Expose
    private String lPhone;
    @SerializedName("joined_at")
    @Expose
    private String joinedAt;
    @SerializedName("male")
    @Expose
    private String male;
    @SerializedName("fmale")
    @Expose
    private String fmale;
    @SerializedName("youth")
    @Expose
    private String youth;
    @SerializedName("status")
    @Expose
    private String status;

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPEmail() {
        return pEmail;
    }

    public void setPEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getPAddress() {
        return pAddress;
    }

    public void setPAddress(String pAddress) {
        this.pAddress = pAddress;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getLPhone() {
        return lPhone;
    }

    public void setLPhone(String lPhone) {
        this.lPhone = lPhone;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFmale() {
        return fmale;
    }

    public void setFmale(String fmale) {
        this.fmale = fmale;
    }

    public String getYouth() {
        return youth;
    }

    public void setYouth(String youth) {
        this.youth = youth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
