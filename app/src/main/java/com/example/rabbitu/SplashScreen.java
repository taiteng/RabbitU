package com.example.rabbitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivitySplashscreenBinding;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashscreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashscreenBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 2700);
    }
}
