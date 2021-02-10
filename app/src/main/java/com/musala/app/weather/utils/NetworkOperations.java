package com.musala.app.weather.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.musala.app.weather.retrofit.IWeatherApi;
import com.musala.app.weather.retrofit.RetrofitClient;
import com.musala.app.weather.views.activities.BaseActivity;

public class NetworkOperations {

    public static IWeatherApi getApi() {
        return RetrofitClient.getClient().create(IWeatherApi.class);

    }

    //Return True -> Connected of False -> Disconnected
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null)
            return true;
        else {
            return false;
        }
    }
}
