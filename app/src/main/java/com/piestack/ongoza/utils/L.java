package com.piestack.ongoza.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.piestack.ongoza.BuildConfig;

/**
 * Created by AhmedEltaher on 01/01/17.
 */

public class L {
    public static void d(String tag, String massage) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, massage);
        }
    }

    public static void i(String tag, String massage) {
        if (BuildConfig.DEBUG) {
            Logger.i(tag, massage);
        }
    }

    public static void v(String tag, String massage) {
        if (BuildConfig.DEBUG) {
            Logger.v(tag, massage);
        }
    }

    public static void e(String tag, String massage) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, massage);
        }
    }

    public static void json(String tag, String massage) {
        if (BuildConfig.DEBUG) {
            Logger.i(tag);
            Logger.json(massage);
        }
    }
}
