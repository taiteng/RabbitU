package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    FirebaseAuth mAuth;
    BottomNavigationView mBottomNavigationView;
    Animation atg, btgone, btgtwo, btgthree, roundingalone;
    ImageView backImg, arrowImg;
    Chronometer timerChronometer;
    Button startBtn, stopBtn, resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item1);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        return true;
                    case R.id.item2:
                        startActivity(new Intent(MainActivity.this,Book.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item3:
                        startActivity(new Intent(MainActivity.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        startActivity(new Intent(MainActivity.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        startActivity(new Intent(MainActivity.this,Settings.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }

                return false;
            }
        });

        atg = AnimationUtils.loadAnimation(MainActivity.this, R.anim.atg);
        btgone = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgone);
        btgtwo = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgtwo);
        btgthree = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgthree);
        roundingalone = AnimationUtils.loadAnimation(MainActivity.this, R.anim.roundingalone);

        backImg = findViewById(R.id.back_image);
        arrowImg = findViewById(R.id.arrow_image);
        timerChronometer = findViewById(R.id.timer_chronometer);
        startBtn = findViewById(R.id.button_start);
        stopBtn = findViewById(R.id.button_stop);
        resetBtn = findViewById(R.id.button_reset);

        backImg.startAnimation(btgone);
        arrowImg.startAnimation(btgtwo);
        timerChronometer.startAnimation(atg);
        startBtn.startAnimation(btgone);
        stopBtn.startAnimation(btgtwo);
        resetBtn.startAnimation(btgthree);

        stopBtn.setAlpha(0);
        resetBtn.setAlpha(0);

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                arrowImg.startAnimation(roundingalone);

                stopBtn.animate().alpha(1).setDuration(300).start();
                startBtn.animate().alpha(0).setDuration(300).start();

                if(resetBtn.getAlpha() == 1){
                    resetBtn.animate().alpha(0).setDuration(300).start();
                }

                if(timerChronometer.getBase() == 0){
                    timerChronometer.setBase(SystemClock.elapsedRealtime());
                    timerChronometer.start();
                }
                else{
                    long resumeSeconds = timerChronometer.getBase();
                    timerChronometer.setBase(SystemClock.elapsedRealtime() + resumeSeconds);
                    timerChronometer.start();
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                arrowImg.clearAnimation();

                timerChronometer.stop();
                long elapsedMillis = SystemClock.elapsedRealtime() - timerChronometer.getBase();
                double seconds = elapsedMillis/1000.00;
                Toast.makeText(MainActivity.this, "Seconds: " + seconds, Toast.LENGTH_SHORT).show();

                startBtn.animate().alpha(1).setDuration(300).start();
                resetBtn.animate().alpha(1).setDuration(300).start();
                stopBtn.animate().alpha(0).setDuration(300).start();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                timerChronometer.setBase(SystemClock.elapsedRealtime());

                startBtn.animate().alpha(1).setDuration(300).start();
                resetBtn.animate().alpha(0).setDuration(300).start();

                if(stopBtn.getAlpha() == 1){
                    stopBtn.animate().alpha(0).setDuration(300).start();
                }
            }
        });
    }
}