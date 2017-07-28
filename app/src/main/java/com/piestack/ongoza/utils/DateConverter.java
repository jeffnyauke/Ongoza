package com.piestack.ongoza.utils;

import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class DateConverter {

    private static PrettyTime p = new PrettyTime();

    private static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp*1000);
    }

    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    public static String toPrettyDate(Long timestamp) {
        return timestamp == null ? null : p.format(toDate(timestamp));
    }
}