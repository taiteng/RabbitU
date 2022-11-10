package com.example.rabbitu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminBook extends AppCompatActivity {

    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private FirebaseUser user ;
    private DatabaseReference reference ;
    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID);
    }
}
