package com.musala.app.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.musala.app.weather.modules.Weather;
import com.musala.app.weather.modules.WeatherObject;

import static android.content.Context.MODE_PRIVATE;

public class Preferences {

    private final String PREFS_NAME = "MUSALA";
    private final String LAST_WEATHER = "LastWeather";



    private static Preferences instance;
    private SharedPreferences appPreference;
    private Editor preferenceEditor;


    public static Preferences getInstance() {
        //For Handling MutiThread Opreations
        if (instance == null) {
            synchronized (Preferences.class) {
                // If Other Thread Enter The Loop
                if (instance == null) {
                    instance = new Preferences();
                    Log.d("Pref","Installed");
                }
            }
        }
        return instance;
    }

    private Preferences() {
    }

    public void InitializePreferences(Context context) {
        this.appPreference = context.getSharedPreferences(this.PREFS_NAME, MODE_PRIVATE);
    }


    public WeatherObject getWeather() {
        return new Gson().fromJson(this.appPreference.getString(this.LAST_WEATHER, null),WeatherObject.class);
    }

    public void SaveWeather(WeatherObject wea) {
        this.preferenceEditor = this.appPreference.edit();
        this.preferenceEditor.putString(this.LAST_WEATHER, new Gson().toJson(wea));
        this.preferenceEditor.commit();
    }


}
