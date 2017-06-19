package com.piestack.ongoza.models;

/**
 * Created by Jeffrey Nyauke on 6/2/2017.
 */

public class BDA {
    private int id;
    private int report_id;
    private int county_id;
    private int modality_id;
    private int oip_id;
    private int p_id;
    private int pd_id;
    private int mode_id;
    private int sub_id;
    private int s_id;
    private int travel;
    private int dor;
    private int duration_weeks;
    private int e_needs;
    private int hours_inprocess;
    private int active;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
