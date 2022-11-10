package com.example.rabbitu;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser user ;
    private DatabaseReference reference ;
    private String userID = "";
    private static boolean isReset = true;
    private String equippedMusicID = "";
    public static long mlastStopTime = 0;
    public static Context currentContext;
    public int secondsStudied = 0;
    public int totalSecondsStudied = 0;
    public int secondsPaused = 0;
    public int millisPaused = 0;
    public int coins = 0;
    public int coinsEarned = 0;
    private boolean isMuteMusic = false;
    private String musicAudio = "";
    private static  final long START_TIME_IN_MILLIS = 600000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning = false;

    MediaPlayer mediaPlayer = new MediaPlayer();
    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    BottomNavigationView mBottomNavigationView;
    Animation atg, btgone, btgtwo, btgthree, roundingalone;
    ImageView backImg, arrowImg;
    Chronometer timerChronometer;
    Button startBtn, pauseBtn, resetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentContext = MainActivity.this;

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item1);

        //default media volume
        mediaPlayer.setVolume(0, 0);

        //connect database and retrieve data
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equippedMusicID = snapshot.child("equippedMusicID").getValue(String.class);
                musicAudio = snapshot.child("equippedMusicAudio").getValue(String.class);
                isMuteMusic = snapshot.child("isMuteMusic").getValue(boolean.class);
                coins = snapshot.child("coins").getValue(int.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();
            }
        });

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

        //animate layout
        atg = AnimationUtils.loadAnimation(MainActivity.this, R.anim.atg);
        btgone = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgone);
        btgtwo = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgtwo);
        btgthree = AnimationUtils.loadAnimation(MainActivity.this, R.anim.btgthree);
        roundingalone = AnimationUtils.loadAnimation(MainActivity.this, R.anim.roundingalone);

        backImg = findViewById(R.id.back_image);
        arrowImg = findViewById(R.id.arrow_image);
        timerChronometer = findViewById(R.id.timer_chronometer);
        startBtn = findViewById(R.id.button_start);
        pauseBtn = findViewById(R.id.button_pause);
        resetBtn = findViewById(R.id.button_reset);

        backImg.startAnimation(btgone);
        arrowImg.startAnimation(btgtwo);
        timerChronometer.startAnimation(atg);
        startBtn.startAnimation(btgone);
        pauseBtn.startAnimation(btgtwo);
        resetBtn.startAnimation(btgthree);

        pauseBtn.setAlpha(0);
        resetBtn.setAlpha(0);

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //start animation
                arrowImg.startAnimation(roundingalone);

                //show and hide buttons
                pauseBtn.animate().alpha(1).setDuration(300).start();
                startBtn.animate().alpha(0).setDuration(300).start();

                if(resetBtn.getAlpha() == 1){
                    resetBtn.animate().alpha(0).setDuration(300).start();
                }

                //reset or resume
                if(isReset){
                    timerChronometer.setBase(SystemClock.elapsedRealtime());
                    endTimerPause();
                }
                else{
                    long intervalOnPause = (SystemClock.elapsedRealtime() - mlastStopTime);
                    timerChronometer.setBase(timerChronometer.getBase() + intervalOnPause);
                    isReset = false;
                }
                timerChronometer.start();

                //start media player
                try{
                    //Pass the audio URL to the Media Player
                    mediaPlayer.setDataSource(musicAudio);
                    mediaPlayer.setOnPreparedListener(mp -> {
                        //Start playing the audio
                        mp.start();

                        //Loop the audio
                        mp.setLooping(true);

                        if (isMuteMusic){
                            mp.setVolume(0,0);
                        }
                        else {
                            mp.setVolume(1,1);
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //stop animation
                arrowImg.clearAnimation();

                //stop chronometer
                timerChronometer.stop();

                //keep pause seconds in check
                mlastStopTime = SystemClock.elapsedRealtime();

                //calculate seconds
                secondsStudied = calculateSeconds();
                totalSecondsStudied += secondsStudied;

                //count coins earned
                coinsEarned = countCoins();
                coins += coinsEarned;

                //show and hide buttons
                startBtn.animate().alpha(1).setDuration(300).start();
                startBtn.setText("Resume");
                resetBtn.animate().alpha(1).setDuration(300).start();
                pauseBtn.animate().alpha(0).setDuration(300).start();

                //stop and reset media player
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();

                startTimerPause();

                //not reset
                isReset = false;
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //reset chronometer
                timerChronometer.setBase(SystemClock.elapsedRealtime());

                //show and hide buttons
                startBtn.animate().alpha(1).setDuration(300).start();
                startBtn.setText("Start");
                resetBtn.animate().alpha(0).setDuration(300).start();

                if(pauseBtn.getAlpha() == 1){
                    pauseBtn.animate().alpha(0).setDuration(300).start();
                }

                //stop and reset media player
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();

                endTimerPause();

                //reset
                isReset = true;

                //show dialog
                succeededStudy(totalSecondsStudied, secondsPaused, coinsEarned);
            }
        });
    }

    /**
     * Calculates the seconds studied by the user
     */
    public int calculateSeconds(){
        mlastStopTime = SystemClock.elapsedRealtime();
        long elapsedMillis = mlastStopTime - timerChronometer.getBase();
        int seconds = (int)(elapsedMillis/1000.00);

        return seconds;
    }

    /**
     * Calculates the coins earned by the user
     */
    public int countCoins(){
        long elapsedMillis = SystemClock.elapsedRealtime() - timerChronometer.getBase();
        double seconds = elapsedMillis/1000.00;
        //int coinsGet = (int)seconds/60;
        int coinsGet = (int)seconds/1;

        return coinsGet;
    }

    /**
     * Shows a dialog box with details of the chronometer
     */
    public void succeededStudy(int second, int pause, int coin){
        //Create the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.study_success, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog box
        dialog.show();

        //Initialization of the elements in timer_success.xml
        TextView totalStudyMinutes = dialog.findViewById(R.id.totalStudyMinutes);
        TextView totalPauseMinutes = dialog.findViewById(R.id.totalPauseMinutes);
        TextView totalCoinsEarned = dialog.findViewById(R.id.totalCoinsEarned);
        ImageButton exitButton = dialog.findViewById(R.id.exitIcon);

        //Displays the timer details on the alert dialog box
        totalStudyMinutes.setText(String.valueOf(second));
        totalPauseMinutes.setText(String.valueOf(pause));
        totalCoinsEarned.setText(String.valueOf(coin));

        //Insert coins into database
        reference.child("coins").setValue(coins);

        //Closes the alert dialog box
        exitButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    //Start timer for pausing study session
    private void startTimerPause(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                //show dialog
                failedStudy();
            }
        }.start();

        mTimerRunning = true;
    }

    //End timer for pausing study session
    private void endTimerPause(){
        if(mTimerRunning){
            mCountDownTimer.cancel();
            mTimerRunning = false;
            millisPaused = (int)START_TIME_IN_MILLIS - (int)mTimeLeftInMillis;
            secondsPaused = millisPaused/1000;
            mTimeLeftInMillis = START_TIME_IN_MILLIS;
        }
    }

    /**
     * Shows a dialog box with details of the chronometer
     */
    public void failedStudy(){
        //Create the alert dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.study_failed, null));
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Display the alert dialog box
        dialog.show();

        //Initialization of the elements in timer_failed.xml
        TextView totalPauseMinutes = dialog.findViewById(R.id.totalPauseMinutes);
        ImageButton exitButton = dialog.findViewById(R.id.exitIcon);

        //Displays the failed details on the alert dialog box
        totalPauseMinutes.setText(String.valueOf(10));

        //Closes the alert dialog box
        exitButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    /**
     * Put the selection of menu on item1 on resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        //connect database and retrieve data
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equippedMusicID = snapshot.child("equippedMusicID").getValue(String.class);
                musicAudio = snapshot.child("equippedMusicAudio").getValue(String.class);
                isMuteMusic = snapshot.child("isMuteMusic").getValue(boolean.class);
                coins = snapshot.child("coins").getValue(int.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();
            }
        });

        mBottomNavigationView.setSelectedItemId(R.id.item1);
    }
}