package com.musala.app.weather.views.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.musala.app.weather.R;
import com.musala.app.weather.adapters.PropertiesAdapter;
import com.musala.app.weather.databinding.ActivityMainBinding;
import com.musala.app.weather.modules.WeatherObject;
import com.musala.app.weather.modules.WeatherProperties;
import com.musala.app.weather.utils.Constant;
import com.musala.app.weather.utils.NetworkOperations;
import com.musala.app.weather.utils.Preferences;
import com.musala.app.weather.utils.Utils;
import com.musala.app.weather.viewModels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends BaseActivity implements BaseActivity.IPermissions {
    public static final int DELAY_MILLIS = 2000;
    private ActivityMainBinding binding;

    //GPS
    LocationManager lm;
    LocationListener locationListener;
    boolean isRequestedToUpdates = false;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    MutableLiveData<Location> myLocation = new MutableLiveData<>();
    boolean gotoActivity = false;
    AlertDialog alertDialog;
    //    Ui
    PropertiesAdapter propertiesAdapter = new PropertiesAdapter();
    //
    MainActivityViewModel mainActivityViewModel;
    boolean toggleSearch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        animateClouds();
        //set Listeners
        setListeners();
        // Get Last Weather Data
        updateUi(Preferences.getInstance().getWeather());
        //init view model
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        //init observes
        initObserves();

        // Start GPs Checks
        startGPS();



    }

    private void setListeners() {
        binding.appBar.txtSearch.animate().translationX(+1000);

        binding.appBar.imgSearch.setOnClickListener(v -> {
            if (!toggleSearch){
                showSearchInput();
            }else {
                hideSearchInput();
            }
        });
        binding.appBar.txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.progressBar.setVisibility(View.VISIBLE);

                    hideKeyboardFrom(MainActivity.this,binding.appBar.txtSearch);
                    String name=v.getText().toString();
                    hideSearchInput();
                    binding.appBar.cityName.setText(name);
                    getData(name);
                }
                return true;
            }
        });
    }

    public void getData(String name) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("units", Constant.UNITS);
        hashMap.put("appid", Constant.API_KEY);
        hashMap.put("q", name);
        if (NetworkOperations.isNetworkConnected(MainActivity.this))
        mainActivityViewModel.getWeatherData(hashMap);
        else {
            showInternetErrorDialog(()->{
               getData(name);
            });
        }
    }
    private void getData(Location location) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("units", Constant.UNITS);
        hashMap.put("appid", Constant.API_KEY);
        hashMap.put("lat", location.getLatitude() + "");
        hashMap.put("lon", location.getLongitude() + "");
        if (NetworkOperations.isNetworkConnected(this)) {
            mainActivityViewModel.getWeatherData(hashMap);
        } else {
            // if no internet founded we will
            showInternetErrorDialog(() -> {
               getData(location);
            });
        }
    }

    private void hideSearchInput() {
        binding.appBar.cityName.setVisibility(View.VISIBLE);
        binding.appBar.txtSearch.animate().translationX(+1000)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.appBar.txtSearch.setVisibility(View.INVISIBLE);

                    }
                });
        toggleSearch = false;
    }

    private void showSearchInput() {
        binding.appBar.txtSearch.setVisibility(View.VISIBLE);
        binding.appBar.txtSearch.setText("");
        binding.appBar.txtSearch.animate().translationX(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        binding.appBar.cityName.setVisibility(View.INVISIBLE);
                    }
                });
        toggleSearch = true;
    }

    public void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void initObserves() {

        mainActivityViewModel.getErrorResponseMutableLiveData().observe(this, s -> {
            binding.progressBar.setVisibility(View.GONE);
            showErrorDialog(new IDialog() {
                @Override
                public void onOkPressed() {
                    // to  return the city name from last data registered
                    updateUi(Preferences.getInstance().getWeather());

                }
            },getString(R.string.error),s);
        });

        mainActivityViewModel.getWeatherMutableLiveData().observe(this, weather -> {
            Preferences.getInstance().SaveWeather(weather);
            binding.progressBar.setVisibility(View.GONE);
            updateUi(weather);

        });

        myLocation.observe(this, location -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            getData(location);
        });
    }


    private void animateClouds() {
        int reverseCloud = -1;
        float screenWithCloudStop = .8f;
        int duration = 60000;

        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.cloud, "translationX", (float) (Utils.getInstance().getWidth(this) * screenWithCloudStop));
        animation.setDuration(duration);
        animation.start();
        animation = ObjectAnimator.ofFloat(binding.cloudTwo, "translationX", (float) (Utils.getInstance().getWidth(this) * screenWithCloudStop * reverseCloud));
        animation.setDuration(duration);
        animation.start();
    }

    private void initViews() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Preferences.getInstance().InitializePreferences(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.rvWeatherProps.setLayoutManager(linearLayoutManager);
        binding.rvWeatherProps.setAdapter(propertiesAdapter);
    }

    public void updateUi(WeatherObject weather) {
        if (weather == null)
            return;
        binding.appBar.cityName.setText(weather.getName());
        binding.txtMaxTemp.setText((int) (Math.ceil(weather.getMain().getTempMax())) + "");
        binding.txtMinTemp.setText((int) (Math.floor(weather.getMain().getTempMin())) + "");
        binding.txtTemp.setText((int) (Math.floor(weather.getMain().getTemp())) + "");
        binding.txtDescription.setText(weather.getWeather().get(0).getMain());
        ArrayList<WeatherProperties> weatherProperties = new ArrayList<>();
        weatherProperties.add(new WeatherProperties("Clouds", weather.getClouds().getAll() + "%", R.drawable.clouds));
        weatherProperties.add(new WeatherProperties("Humidity", weather.getMain().getHumidity() + "%", R.drawable.ic_humidity));
        weatherProperties.add(new WeatherProperties("Wind Speed", weather.getWind().getSpeed() + "m/s", R.drawable.ic_wind));
        propertiesAdapter.swipeAdapter(weatherProperties);
    }

    //# region  [ GPS ]
    private void startGPS() {
        setDefaultPermissionArray(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION});
        setPermissions(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (CheckPermissionList()) {

        } else {
            requestPermissions();
        }

    }


    @Override
    public void OnPermissionRequestedDone(boolean b) {
        if (b) {
            //here we have access to this Permission
            requestLocationUpdates();
        } else {
            // this Permission has denyed
            showErrorDialog(() -> {
                if (CheckPermissionList()) {
                } else {
                    requestPermissions();
                }
            }, R.string.permission_needed, R.string.we_need_permission_to_location);
        }
    }


    // because the handler deprecated since api level 30
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkGpsEnabled();
            handler.postDelayed(this, DELAY_MILLIS);
        }
    };

    public void startCheckGpsEnabled() {
        handler.postDelayed(runnable, DELAY_MILLIS);
    }

    public void stopCheckGpsEnabled() {
        handler.removeCallbacks(runnable);
    }

    public void checkGpsEnabled() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            //        TODO Remove It

            // notify user
            if (alertDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage(R.string.gps_network_not_enabled)
                        .setNegativeButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                stopCheckGpsEnabled();
                                gotoActivity = true;
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }).setCancelable(false);
                alertDialog = builder.create();

            }

            if (alertDialog.isShowing())
                return;
            else
                alertDialog.show();
        }
    }

    public void requestLocationUpdates() {
        startCheckGpsEnabled();
        binding.progressBar.setVisibility(View.VISIBLE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation.postValue(location);
                lm.removeUpdates(locationListener);
                isRequestedToUpdates = false;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
                , 1, .01f, locationListener);
        isRequestedToUpdates = true;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (gotoActivity)
            startCheckGpsEnabled();
        if (isRequestedToUpdates) {
            gotoActivity = false;
            checkGpsEnabled();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isRequestedToUpdates) {
            lm.removeUpdates(locationListener);
        }
    }

    //# endregion
}