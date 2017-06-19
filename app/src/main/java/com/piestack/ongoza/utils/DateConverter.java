package com.piestack.ongoza.utils;

import android.widget.Toast;

import java.util.Date;

public class DateConverter {

    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}