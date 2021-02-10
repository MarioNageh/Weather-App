package com.musala.app.weather.retrofit;


import com.musala.app.weather.modules.WeatherObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IWeatherApi {


    @POST("data/2.5/weather")
    Observable<WeatherObject> getWeatherData(@QueryMap Map<String, String> params);
}
