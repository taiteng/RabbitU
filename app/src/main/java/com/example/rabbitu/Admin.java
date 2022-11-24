package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    FirebaseAuth mAuth;
    FloatingActionButton editPopUp, bookBtn, musicBtn, logoutBtn;
    public boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        editPopUp = findViewById(R.id.edit_popup);
        bookBtn = findViewById(R.id.btn_book);
        musicBtn = findViewById(R.id.btn_music);
        logoutBtn = findViewById(R.id.btn_logout);

        editPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMenuOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        bookBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this,AdminEditBook.class));
            }
        });
        musicBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this,AdminMusic.class));
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Admin.this,Login.class));
                finish();
            }
        });
    }

    private void showFABMenu(){
        isMenuOpen = true;
        musicBtn.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        bookBtn.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
        logoutBtn.animate().translationY(-getResources().getDimension(R.dimen.standard_175));
    }

    private void closeFABMenu(){
        isMenuOpen = false;
        musicBtn.animate().translationY(0);
        bookBtn.animate().translationY(0);
        logoutBtn.animate().translationY(0);
    }
}
