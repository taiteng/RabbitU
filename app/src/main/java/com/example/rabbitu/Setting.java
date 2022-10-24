package com.example.rabbitu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Setting extends AppCompatActivity {


    private ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    Button LogoutBtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        changeStatusBarColor();


        //Testing on Logout Button
        mAuth = FirebaseAuth.getInstance();
        LogoutBtn = findViewById(R.id.btnLogout);

        LogoutBtn.setOnClickListener(view->{
            mAuth.signOut();
            startActivity(new Intent(Setting.this,Login.class));

        });


    }




    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }


    //Check the user logout or not
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user == null){
//            startActivity(new Intent(Setting.this,Login.class));
//        }
//    }



}
