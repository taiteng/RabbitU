package com.example.rabbitu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rabbitu.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Login extends AppCompatActivity {
    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    private static final String TAG = "FacebookAuthentication";
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private FirebaseAuth.AuthStateListener authStateListener;
    private ActivityLoginBinding binding;
    public String temp, currentUserUID;
    EditText emailLogin ;
    EditText passwordLogin ;
    TextView forgetTxt ;
    Button loginBtn ;
    ImageView googleSignIn;
    LoginButton facebookLogin;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;
    FirebaseAuth mAuth;
    CallbackManager callbackManager;
    Boolean isNew = false;
    DatabaseReference df;

    public String admin = "admin@gmail.com";

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

        emailLogin = findViewById(R.id.editTextEmailLogin);
        passwordLogin = findViewById(R.id.editTextPasswordLogin);
        loginBtn = findViewById(R.id.LoginButton);
        forgetTxt = findViewById(R.id.forgetpass);
        googleSignIn = findViewById(R.id.google_login);
        facebookLogin = findViewById(R.id.facebook_login);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions("email", "public_profile");

        mAuth = FirebaseAuth.getInstance();

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

        loginBtn.setOnClickListener(view->{
            loginUser();
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken("407560665615-4hie262til75rshdgsocquvrqrdnc67k.apps.googleusercontent.com")
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        //Reset Password through email (check on the spam email)
        forgetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset password ? ");
                passwordResetDialog.setMessage("Enter Your Email To Receive Reset Link. ");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login.this,"Reset Link Send To your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this,"Error ! Reset Link Cannot Send : " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //Close the Dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    //Validation on the Input Field
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

                        if(emailTxt.equals(admin)){
                            Intent intent = new Intent(Login.this,Admin.class);
                            startActivity(intent);
                        }
                        else{
                            startActivity(new Intent(Login.this,MainActivity.class));
                        }
                    }
                    else{
                        Toast.makeText(Login.this,"Log In Error : " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //Google Sign In
    void SignIn() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 1000);
    }
    //Google Log In
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);

                mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(),"Your Google account connect to our application ! ",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                signInTask.getResult(ApiException.class);
                HomeActivity();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong" + signInTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    //After successfully Google Log In and then Link to the Home page
    void HomeActivity() {
        finish();
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }

    //Link & open  to register activity
    public void onRegisterClick(View View){
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleFacebookToken(AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "sign in with credential: successful");
                    Toast.makeText(Login.this, "Authentication Success with Facebook", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    currentUserUID = mAuth.getCurrentUser().getUid();

                    //Validate if the user is exist in firebase
                    boolean isNewUser = checkIfNewUser();

                    if(!isNewUser){
                        HomeActivity();
                    }else{
                        updateUser(currentUser); //create new user and navigate to home
                    }


                }else{
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Toast.makeText(Login.this, "Authentication Failed with Facebook", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIfNewUser(){
        df = FirebaseDatabase.getInstance().getReference("Users");
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    temp = dataSnapshot.getKey().toString();
                    if (temp.equals(currentUserUID.toString())){
                        isNew = false;
                    }
                    else {
                        isNew = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
            }
        });

        return isNew;
    }

    private void updateUser(FirebaseUser user){
        if(user != null){
            String fullName = user.getDisplayName();
            String email = user.getEmail();
            String phone = user.getPhoneNumber();
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
                                Toast.makeText(Login.this,"User Registered successfully",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(Login.this,"Fail to Register ! Try again ! ",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            HomeActivity();
        }
    }




}
