package com.musala.app.weather.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;

public class Utils {
    private static Utils instance;
    
    public static Utils getInstance(){
        if(instance==null){
            instance=new Utils();
        }
        return instance;
    }
    public int getWidth(Context context) {
        if (Build.VERSION.SDK_INT<=17)
            return -1;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = context.getDisplay();
        if (display != null) {
            display.getRealMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
        return -1;
    }

}
