package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notes extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;

    FloatingActionButton add ;

    AdapterNotes adapterNotes;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelNotes> listNotes;

    RecyclerView notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item4);



        add = findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Notes.this, "Nice" , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Notes.this,AddActivity.class));
            }
        });




        notes = findViewById(R.id.notes);

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        notes.setLayoutManager(mLayout);
        notes.setItemAnimator(new DefaultItemAnimator());

        notesData();





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
                        startActivity(new Intent(Notes.this,BookActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item3:
                        finish();
                        startActivity(new Intent(Notes.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(Notes.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        finish();
                        startActivity(new Intent(Notes.this,Settings.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }

                return false;
            }
        });


    }

    private void notesData() {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listNotes = new ArrayList<>();
                for(DataSnapshot item : snapshot.getChildren()){
                    ModelNotes mnt = item.getValue(ModelNotes.class);
                    mnt.setKey(item.getKey());
                    listNotes.add(mnt);
                }
                adapterNotes = new AdapterNotes(listNotes,Notes.this);
                notes.setAdapter(adapterNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}