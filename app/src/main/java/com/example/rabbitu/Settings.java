package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    private FirebaseUser user ;
    private DatabaseReference reference ;

    private String userID;


    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;
    Button LogoutBtn, MusicBtn;
    TextView mail, name,phoneNumber,email;

    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        LogoutBtn = findViewById(R.id.button_logout);
        MusicBtn = findViewById(R.id.button_music);
        mail = findViewById(R.id.userEmail);


        name = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item5);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //Get the details on google account
        if (account!=null){
            String Mail = account.getEmail();
            mail.setText(Mail);
        }



        LogoutBtn.setOnClickListener(view->{
            mAuth.signOut();
            signOut();
            startActivity(new Intent(Settings.this,Login.class));
        });



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullNameTxt = userProfile.fullName;
                    String emailTxt = userProfile.email;
                    String phoneNumberTxt = userProfile.phoneNumber;

                    name.setText(fullNameTxt);
                    email.setText(emailTxt);
                    phoneNumber.setText(phoneNumberTxt);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();
            }
        });







        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item2:
                        finish();
                        startActivity(new Intent(Settings.this,BookActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item3:
                        finish();
                        startActivity(new Intent(Settings.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(Settings.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        return true;
                }

                return false;
            }
        });
    }

     void signOut() {
         signInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 finish();
                 startActivity(new Intent(Settings.this,Login.class));
             }
         });
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