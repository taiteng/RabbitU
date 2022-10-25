package com.example.rabbitu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private View rootview;

    Animation atg, btgone, btgtwo, btgthree, roundingalone;
    ImageView backImg, arrowImg;
    Chronometer timerChronometer;
    Button startBtn, stopBtn, resetBtn;

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview =  inflater.inflate(R.layout.fragment_home, container, false);

        atg = AnimationUtils.loadAnimation(getActivity().getBaseContext().getApplicationContext(), R.anim.atg);
        btgone = AnimationUtils.loadAnimation(getActivity().getBaseContext().getApplicationContext(), R.anim.btgone);
        btgtwo = AnimationUtils.loadAnimation(getActivity().getBaseContext().getApplicationContext(), R.anim.btgtwo);
        btgthree = AnimationUtils.loadAnimation(getActivity().getBaseContext().getApplicationContext(), R.anim.btgthree);
        roundingalone = AnimationUtils.loadAnimation(getActivity().getBaseContext().getApplicationContext(), R.anim.roundingalone);

        backImg = rootview.findViewById(R.id.back_image);
        arrowImg = rootview.findViewById(R.id.arrow_image);
        timerChronometer = rootview.findViewById(R.id.timer_chronometer);
        startBtn = rootview.findViewById(R.id.button_start);
        stopBtn = rootview.findViewById(R.id.button_stop);
        resetBtn = rootview.findViewById(R.id.button_reset);

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

                timerChronometer.setBase(SystemClock.elapsedRealtime());
                timerChronometer.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                arrowImg.clearAnimation();

                timerChronometer.stop();
                long elapsedMillis = SystemClock.elapsedRealtime() - timerChronometer.getBase();
                double seconds = elapsedMillis/1000.00;
                Toast.makeText(getActivity().getBaseContext().getApplicationContext(), "Seconds: " + seconds, Toast.LENGTH_SHORT).show();

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

        return rootview;
    }
}