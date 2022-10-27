package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    ListView leaderboardListView;
    TextView lName1,lName2, lName3; //Top 3 Name variable
    TextView lText1, lText2, lText3; //Top 3 minutes spent
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> minList = new ArrayList<>();
    ArrayList<String> topNameList = new ArrayList<>();
    ArrayList<String> topMinList = new ArrayList<>();
    DatabaseReference df;
    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item3);

        leaderboardListView = findViewById(R.id.list_leaderboard);
        lName1 = findViewById(R.id.first_leaderboard_name);
        lText1 = findViewById(R.id.first_leaderboard_text);
        lName2 = findViewById(R.id.second_leaderboard_name);
        lText2 = findViewById(R.id.second_leaderboard_text);
        lName3 = findViewById(R.id.third_leaderboard_name);
        lText3 = findViewById(R.id.third_leaderboard_text);


        //Testing Logics

        //Add top 3 data
        topNameList.add("Rank 1");
        topNameList.add("Rank 2");
        topNameList.add("Rank 3");
        topMinList.add("10 min");
        topMinList.add("9 min");
        topMinList.add("8 min");

        //Add other rankings
        nameList.add("Rank 4");
        nameList.add("Rank 5");
        nameList.add("Rank 6");
        minList.add("7 min");
        minList.add("6 min");
        minList.add("5 min");

        lName1.setText(topNameList.get(0));
        lText1.setText(topMinList.get(0));
        lName2.setText(topNameList.get(1));
        lText2.setText(topMinList.get(1));
        lName3.setText(topNameList.get(2));
        lText3.setText(topMinList.get(2));


        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(Leaderboard.this, nameList, minList);
        leaderboardListView.setAdapter(leaderboardAdapter);





//        df = FirebaseDatabase.getInstance().getReference();
//
//        df.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                String value = snapshot.getValue(String.class);
//                arrayList.
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        leaderboardListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + nameList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        startActivity(new Intent(Leaderboard.this,BookActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item3:
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(Leaderboard.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        finish();
                        startActivity(new Intent(Leaderboard.this,Settings.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }

                return false;
            }
        });
    }
}