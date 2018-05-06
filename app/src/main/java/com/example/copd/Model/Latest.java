package com.example.copd.Model;

/**
 * Created by asus on 2018/4/18.
 */

public class Latest {
    public static String level = null;
    public static String current_time = null;
    public  static  String fev1=null;
    public static  String heart_rate=null;
    public static String boold_rate_up = null;
    public  static  String boold_rate_down = null;
    public static  String breath_rate = null;
    public static  String BMI = null;
    public static String cough_level = null;
    public  static  String tempurature = null;
    public static String relivate = null;

    public Latest(){

    }

    public static void setLevel(String level) {
        Latest.level = level;
    }

    public static void setCurrent_time(String current_time) {
        Latest.current_time = current_time;
    }

    public static void setFev1(String fev1) {
        Latest.fev1 = fev1;
    }

    public static void setBoold_rate_down(String boold_rate_down) {
        Latest.boold_rate_down = boold_rate_down;
    }

    public static void setBoold_rate_up(String boold_rate_up) {
        Latest.boold_rate_up = boold_rate_up;
    }

    public static void setBreath_rate(String breath_rate) {
        Latest.breath_rate = breath_rate;
    }

    public static void setHeart_rate(String heart_rate) {
        Latest.heart_rate = heart_rate;
    }

    public static void setBMI(String BMI) {
        Latest.BMI = BMI;
    }

    public static void setCough_level(String cough_level) {
        Latest.cough_level = cough_level;
    }

    public static void setRelivate(String relivate) {
        Latest.relivate = relivate;
    }

    public static void setTempurature(String tempurature) {
        Latest.tempurature = tempurature;
    }
}
