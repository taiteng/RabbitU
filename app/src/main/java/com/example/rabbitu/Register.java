package com.example.rabbitu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText name ;
    EditText email ;
    EditText phone ;
    EditText password ;
    Button registerBtn ;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        changeStatusBarColor();

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        phone = findViewById(R.id.editTextMobile);
        password = findViewById(R.id.editTextPassword);

        registerBtn = findViewById(R.id.RegisterButton);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(view->{
            createUser();
        });
    }


    private void createUser(){
        String fullnameTxt = name.getText().toString();
        String emailTxt = email.getText().toString();
        String phoneTxt = phone.getText().toString();
        String passwordTxt = password.getText().toString();


        if (TextUtils.isEmpty(fullnameTxt)){
            name.setError("Email cannot be empty");
            name.requestFocus();
        }
        else if (TextUtils.isEmpty(emailTxt)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }
        else if (TextUtils.isEmpty(phoneTxt)){
            phone.setError("Phone Number cannot be empty");
            phone.requestFocus();
        }
        else if (TextUtils.isEmpty(passwordTxt)){
            password.setError("Password cannot be empty");
            password.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this,"User Registered successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,Login.class));
                    }
                    else{
                        Toast.makeText(Register.this,"Registered Error : " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    //Open back login activity
    public void onLoginClick(View view){
        Intent intent = new Intent(Register.this,Login.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
