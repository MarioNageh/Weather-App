package com.musala.app.weather.modules;

import android.graphics.drawable.Drawable;

public class WeatherProperties {

    private String pro_name;
    private String pro_value;
    private int img_drawable;

    public WeatherProperties(String pro_name, String pro_value, int img_drawable) {
        this.pro_name = pro_name;
        this.pro_value = pro_value;
        this.img_drawable = img_drawable;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_value() {
        return pro_value;
    }

    public void setPro_value(String pro_value) {
        this.pro_value = pro_value;
    }

    public int getImg_drawable() {
        return img_drawable;
    }

    public void setImg_drawable(int img_drawable) {
        this.img_drawable = img_drawable;
    }
}