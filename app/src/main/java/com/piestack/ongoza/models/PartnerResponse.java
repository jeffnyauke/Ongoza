package com.piestack.ongoza.models;

/**
 * Created by Jeffrey Nyauke on 6/8/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartnerResponse {

    @SerializedName("partner")
    @Expose
    private List<Partner> partner = null;

    public List<Partner> getPartner() {
        return partner;
    }

    public void setPartner(List<Partner> partner) {
        this.partner = partner;
    }

}