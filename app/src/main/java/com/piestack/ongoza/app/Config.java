package com.piestack.ongoza.app;

/**
 * Created by Jeffrey Nyauke on 6/9/2017.
 */

public class Config {

    //private static String mBaseUrl = "http://ongoza.piestack.co.ke/app/";
    private static String mBaseUrl = "http://data.ongozayouth.org/app/";

    public static String loginUrl = mBaseUrl + "login.php";
    public static String updateUrl = mBaseUrl + "resetPassword.php";
    public static String partnersUrl = mBaseUrl + "get/getPartners.php";
    public static String dataUrl = mBaseUrl + "get/getData.php";
    public static String oipUrl = mBaseUrl + "post/postIP.php";
    public static String reporstUrl = mBaseUrl + "get/getReport.php";
    public static String themesUrl = mBaseUrl + "get/getSupportTheme.php";
    public static String countiesUrl = mBaseUrl + "get/getCounty.php";
    public static String summariesUrl = mBaseUrl + "get/getSummary.php";
    public static String postUrl = mBaseUrl + "post/postBDA.php";
    public static String local = "7777777777";
}
