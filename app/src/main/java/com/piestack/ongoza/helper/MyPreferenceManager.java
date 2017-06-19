package com.piestack.ongoza.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.piestack.ongoza.LoginActivity;
import com.piestack.ongoza.models.User;
import com.piestack.ongoza.utils.StringUtils;

/**
 * Created by Jeffrey Nyauke on 6/9/2017.
 */

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ongoza_user";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_COUNTY_ID = "user_county_id";
    private static final String KEY_USER_CREATED_AT = "user_created_at";
    private static final String KEY_USER_ASSIGNED_AT = "user_assigned_at";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }


    public void storeUser(User user) {

        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, StringUtils.isNullorEmpty(user.getName()) ? null : user.getName());
        editor.putString(KEY_USER_EMAIL, StringUtils.isNullorEmpty(user.getEmail()) ? null : user.getEmail());
        editor.putInt(KEY_USER_COUNTY_ID, user.getCounty_id());
        editor.putString(KEY_USER_CREATED_AT, StringUtils.isNullorEmpty(user.getCreatedAt()) ? null : user.getCreatedAt());
        editor.putString(KEY_USER_ASSIGNED_AT, StringUtils.isNullorEmpty(user.getAssignedAt()) ? null : user.getAssignedAt());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName());

    }

    public User getUser() {
        if (pref.getString(KEY_USER_NAME, null) != null) {
            Integer id, county_id;
            String name, email, createdAt, assignedAt;

            id = pref.getInt(KEY_USER_ID, 0);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            county_id = pref.getInt(KEY_USER_COUNTY_ID, 0);
            createdAt = pref.getString(KEY_USER_CREATED_AT, null);
            assignedAt = pref.getString(KEY_USER_ASSIGNED_AT, null);

            User user = new User(id, name, email, county_id, createdAt,assignedAt);

            return user;
        }
        return null;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }


}
