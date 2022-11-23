package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class Admin extends AppCompatActivity {

    FloatingActionButton editPopUp, bookBtn, musicBtn;
    public boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editPopUp = findViewById(R.id.edit_popup);
        bookBtn = findViewById(R.id.btn_book);
        musicBtn = findViewById(R.id.btn_music);

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
    }

    private void showFABMenu(){
        isMenuOpen = true;
        musicBtn.animate().translationY(-getResources().getDimension(R.dimen.standard_65));
        bookBtn.animate().translationY(-getResources().getDimension(R.dimen.standard_120));
    }

    private void closeFABMenu(){
        isMenuOpen = false;
        musicBtn.animate().translationY(0);
        bookBtn.animate().translationY(0);
    }
}
