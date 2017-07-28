/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.piestack.ongoza.app;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatDelegate;

import com.piestack.ongoza.LoginActivity;
import com.piestack.ongoza.R;
import com.piestack.ongoza.helper.MyPreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Created by janisharali on 27/01/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mInstance = this;

        // Initialize Realm
        Realm.init(mInstance);
        RealmConfiguration config = new RealmConfiguration.Builder()  .name("ongoza.realm")
                .schemaVersion(8)
                .build();
        Realm.setDefaultConfiguration(config);



    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }

    public void logout() {
        pref.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}
