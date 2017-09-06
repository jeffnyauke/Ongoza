package com.piestack.ongoza.utils;

/**
 * Created by admin on 9/29/2016.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String LATITUDE = "preLoad";
    private static final String LONGITUDE = "passcodeCheck";
    private static final String PREFS_NAME = "prefs";
    private static Prefs instance;
    private final SharedPreferences sharedPreferences;

    public Prefs(Context context) {

        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs with(Context context) {

        if (instance == null) {
            instance = new Prefs(context);
        }
        return instance;
    }

    public void setLatitude(String latitude) {

        sharedPreferences
                .edit()
                .putString(LATITUDE, latitude)
                .apply();
    }

    public String getLatitude(){
        return sharedPreferences.getString(LATITUDE,"0");
    }

    public void setLongitude(String longitude) {
        sharedPreferences
                .edit()
                .putString(LONGITUDE, longitude)
                .apply();
    }

    public String  getLongitude() {
            return sharedPreferences.getString(LONGITUDE, "0");
    }


}
