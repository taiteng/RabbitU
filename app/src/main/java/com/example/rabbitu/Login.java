package com.example.rabbitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
//        setContentView(R.layout.activity_login);

//        Button loginBtn = findViewById(R.id.LoginButton);
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Login.this, MainActivity.class));
//            }
//        });
        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,MainActivity.class));
            }
        });
    }

    public void onRegisterClick(View View){
        startActivity(new Intent(this,Register.class));
    }
//    android:onClick="onLoginBtnClick"
//    public void onLoginBtnClick(View View){
//        startActivity(new Intent(this,MainActivity.class));
//    }
}
