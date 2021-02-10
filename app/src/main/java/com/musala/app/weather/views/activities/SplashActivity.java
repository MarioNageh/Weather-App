package com.musala.app.weather.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.musala.app.weather.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {
    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);


    }
}
