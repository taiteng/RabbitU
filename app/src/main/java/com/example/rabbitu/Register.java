package com.example.rabbitu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityRegisterBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Register extends AppCompatActivity {
    private static final String TAG = "FacebookAuthentication";
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    FirebaseAuth mAuth;
    EditText name ;
    EditText email ;
    EditText phone ;
    EditText password ;
    Button registerBtn ;
    String temp, currentUserUID;
    CallbackManager callbackManager;
    Boolean isNew;
    LoginButton facebookLogin;
    DatabaseReference df;

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
        facebookLogin = findViewById(R.id.facebook_login);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions("email", "public_profile");
        registerBtn = findViewById(R.id.RegisterButton);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(view->{
            createUser();
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        //If user did not logout, go to home page
        if(accessToken != null && accessToken.isExpired() == false){
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        }

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d(TAG, "onSuccess" + e);
            }
        });


    }

    //After successfully Google Log In and then Link to the Home page
    void HomeActivity() {
        finish();
        Intent intent = new Intent(Register.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "sign in with credential: successful");
                    Toast.makeText(Register.this, "Authentication Success with Facebook", Toast.LENGTH_SHORT).show();

                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    currentUserUID = currentUser.getUid();

                    checkIfNewUser(currentUser);
                }else{
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Toast.makeText(Register.this, "Authentication Failed with Facebook", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkIfNewUser(FirebaseUser currentUser){
        df = FirebaseDatabase.getInstance().getReference("Users");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp = dataSnapshot.getKey().toString();
                    if (temp.equals(currentUserUID.toString())){
                        HomeActivity();
                        isNew = false;
                        break;
                    }
                    else {
                        isNew = true;
                    }
                }

                if(isNew){
                    createFacebookUser(currentUser); //create new user and navigate to home
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });
    }

    private void createFacebookUser(FirebaseUser user){
        if(user != null){
            String fullName = user.getDisplayName();
            String email = user.getEmail();
            String phone = "0123456789";
            String equippedMusicID = "Lofi1";
            String equippedMusicAudio = "https://firebasestorage.googleapis.com/v0/b/rabbitu-ae295.appspot.com/o/Eric%20Godlow%20Beats%20-%20follow%20me.mp3?alt=media&token=a5723865-1f95-4e46-818d-935192f427f0";
            int coins = 0;

            List<String> bookList=new ArrayList<>();
            bookList.add("Computer Science");
            bookList.add("Computer System");

            User registerUser = new User (fullName, phone, email, equippedMusicID, equippedMusicAudio, coins, false, bookList);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(registerUser).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this,"User Registered successfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this,OnBoarding.class);
                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(Register.this,"Fail to Register ! Try again ! ",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


        }
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
                        String equippedMusicID = "Lofi1";
                        String equippedMusicAudio = "https://firebasestorage.googleapis.com/v0/b/rabbitu-ae295.appspot.com/o/Eric%20Godlow%20Beats%20-%20follow%20me.mp3?alt=media&token=a5723865-1f95-4e46-818d-935192f427f0";
                        int coins = 0;
                        List<String> bookList=new ArrayList<>();
                        bookList.add("Computer Science");
                        bookList.add("Computer System");
                        User user = new User (fullnameTxt, phoneTxt, emailTxt, equippedMusicID, equippedMusicAudio, coins, false, bookList);

                        FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Register.this,"User Registered successfully",Toast.LENGTH_SHORT).show();
                                            //startActivity(new Intent(Register.this,Login.class));

                                            Intent intent = new Intent(Register.this, Login.class);
                                            intent.putExtra("isNewUser", true);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(Register.this,"Fail to Register ! Try again ! ",Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


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
