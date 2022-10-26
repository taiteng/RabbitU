package com.example.rabbitu;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private static final String TAG = "MainActivity";
    private static boolean isOngoingTimer;
    private static boolean isLeavingApp = false;
    private static boolean isReset = true;
    public static long mlastStopTime = 0;
    public static Context currentContext;
    public static String userID = "";

    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    BottomNavigationView mBottomNavigationView;
    Animation atg, btgone, btgtwo, btgthree, roundingalone;
    ImageView backImg, arrowImg;
    Chronometer timerChronometer;
    Button startBtn, stopBtn, resetBtn;

    private boolean isPlayingMusic = false;
    private String musicAudio = "";
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentContext = MainActivity.this;

        mediaPlayer.setVolume(0, 0);

        mDatabaseReference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("MusicStorage");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Retrieve the image for the home page

                //Retrieve the number of coins (currency)

                //Retrieve the audio URL
                //musicAudio = snapshot.child("Lofi1").child("MusicAudio").getValue(String.class);

                //Set the text with the number of coins

                //Load the image for the home page
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        isPlayingMusic = true;
        musicAudio = "https://firebasestorage.googleapis.com/v0/b/rabbitu-ae295.appspot.com/o/Eric%20Godlow%20Beats%20-%20follow%20me.mp3?alt=media&token=a5723865-1f95-4e46-818d-935192f427f0";




        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item1);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        return true;
                    case R.id.item2:
                        startActivity(new Intent(MainActivity.this,BookActivity.class));
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

                if(isReset){
                    timerChronometer.setBase(SystemClock.elapsedRealtime());
                    timerChronometer.start();
                }
                else{
                    long intervalOnPause = (SystemClock.elapsedRealtime() - mlastStopTime);
                    timerChronometer.setBase(timerChronometer.getBase() + intervalOnPause);
                    timerChronometer.start();
                    isReset = false;
                }

                try{
                    //Pass the audio URL to the Media Player
                    mediaPlayer.setDataSource(musicAudio);
                    mediaPlayer.setOnPreparedListener(mp -> {
                        //Start playing the audio
                        mp.start();

                        //Loop the audio
                        mp.setLooping(true);

                        if (isPlayingMusic){
                            mp.setVolume(1,1);
                        }

                        else {
                            mp.setVolume(0,0);
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                arrowImg.clearAnimation();

                timerChronometer.stop();
                mlastStopTime = SystemClock.elapsedRealtime();
                long elapsedMillis = SystemClock.elapsedRealtime() - timerChronometer.getBase();
                double seconds = elapsedMillis/1000.00;
                Toast.makeText(MainActivity.this, "Seconds: " + seconds, Toast.LENGTH_SHORT).show();

                startBtn.animate().alpha(1).setDuration(300).start();
                resetBtn.animate().alpha(1).setDuration(300).start();
                stopBtn.animate().alpha(0).setDuration(300).start();

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();

                isReset = false;
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

                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();

                isReset = true;
            }
        });
    }
}