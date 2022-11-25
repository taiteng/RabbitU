package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends AppCompatActivity {

    BottomNavigationView mBottomNavigationView;
    int firstPosition = 0;
    ListView leaderboardListView;
    TextView lName1,lName2, lName3; //Top 3 Name variable
    TextView lText1, lText2, lText3; //Top 3 minutes spent
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<String> coinList = new ArrayList<>();
    ArrayList<String> topNameList = new ArrayList<>();
    ArrayList<String> topCoinList = new ArrayList<>();
    DatabaseReference df;
    Animation animation;

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

        animation = AnimationUtils.loadAnimation(this, R.anim.leaderboard_list_scroll);
        LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(Leaderboard.this, nameList, coinList);
        leaderboardListView.setAdapter(leaderboardAdapter);

        df = FirebaseDatabase.getInstance().getReference().child("Users");
        Query orderQuery = df.orderByChild("coins");

        orderQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String nameValue = snapshot.child("fullName").getValue().toString();
                String coinsValue = snapshot.child("coins").getValue().toString();

                //Not including admin in the leaderboard
                if(nameValue.equals("admin")){

                }else{
                    nameList.add(nameValue);
                    coinList.add(coinsValue);
                }

                leaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                leaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Check for once complete loading the data
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");

                //Reverse the list so we can get highest to lowest value
                Collections.reverse(nameList);
                Collections.reverse(coinList);

                //Add the names and the coins to the top list
                for(int i=0; i<3; i++){
                    topNameList.add(nameList.get(i));
                    topCoinList.add(coinList.get(i));
                }

                //Set the value to the top three
                lName1.setText(topNameList.get(0));
                lText1.setText(topCoinList.get(0));
                lName2.setText(topNameList.get(1));
                lText2.setText(topCoinList.get(1));
                lName3.setText(topNameList.get(2));
                lText3.setText(topCoinList.get(2));

                //Remove the top 3 from the original list
                //since we have added to the top list
                for(int i=0; i<3; i++){
                    nameList.remove(firstPosition);
                    coinList.remove(firstPosition);
                }

                leaderboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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