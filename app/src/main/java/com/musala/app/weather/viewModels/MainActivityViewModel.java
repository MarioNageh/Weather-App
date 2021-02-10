package com.musala.app.weather.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.musala.app.weather.modules.WeatherObject;
import com.musala.app.weather.utils.NetworkOperations;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<WeatherObject> weatherMutableLiveData=new MutableLiveData<>();
    private MutableLiveData<String> errorResponseMutableLiveData=new MutableLiveData<>();

    public MutableLiveData<WeatherObject> getWeatherMutableLiveData() {
        return weatherMutableLiveData;
    }

    public MutableLiveData<String> getErrorResponseMutableLiveData() {
        return errorResponseMutableLiveData;
    }

    public void getWeatherData(HashMap<String,String> queryParamsUrl) {
        NetworkOperations.getApi().getWeatherData(queryParamsUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<WeatherObject>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(WeatherObject weather) {
                weatherMutableLiveData.postValue(weather);
            }

            @Override
            public void onError(Throwable e) {
                Log.w("EEE",e.getMessage());
                errorResponseMutableLiveData.postValue("Error Occur while getting data or City not Found try again");
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
