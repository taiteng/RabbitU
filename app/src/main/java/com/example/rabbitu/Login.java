package com.example.rabbitu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    EditText emailLogin ;
    EditText passwordLogin ;
    TextView registerNowTxt ;
    Button loginBtn ;

    FirebaseAuth mAuth;


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

//        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Login.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });



        emailLogin = findViewById(R.id.editTextEmailLogin);
        passwordLogin = findViewById(R.id.editTextPasswordLogin);
        registerNowTxt = findViewById(R.id.registerNow);
        loginBtn = findViewById(R.id.LoginButton);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(view->{
            loginUser();
        });



        registerNowTxt.setOnClickListener(view->{
            startActivity(new Intent(Login.this,Register.class));
        });





//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final String emailTxt = email.getText().toString();
//                final String passwordTxt = password.getText().toString();
//
//
//                if (emailTxt.isEmpty() || passwordTxt.isEmpty()){
//                    Toast.makeText(Login.this, "Please enter your email or password !", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Intent intent = new Intent(Login.this,MainActivity.class);
//             startActivity(intent);
//                }
//
//
//            }
//        });



    }

    private void loginUser(){
        String emailTxt = emailLogin.getText().toString();
        String passwordTxt = passwordLogin.getText().toString();


        if (TextUtils.isEmpty(emailTxt)){
            emailLogin.setError("Email cannot be empty");
            emailLogin.requestFocus();
        }
        else if (TextUtils.isEmpty(passwordTxt)){
            passwordLogin.setError("Password cannot be empty");
            passwordLogin.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Login.this,"User Log In successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(Login.this,"Log In Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }



    //Link & open  to register activity
    public void onRegisterClick(View View){
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



}
