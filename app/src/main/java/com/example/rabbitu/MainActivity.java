package com.example.rabbitu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    BottomNavigationView mBottomNavigationView;

    HomeFragment mHomeFragment = new HomeFragment();
    BookFragment mBookFragment = new BookFragment();
    LeaderboardFragment mLeaderboardFragment = new LeaderboardFragment();
    NotesFragment mNotesFragment = new NotesFragment();
    SettingsFragment mSettingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, mHomeFragment).commit();

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragmentContainerView, mHomeFragment).commit();
                        return true;
                    case R.id.item2:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragmentContainerView, mBookFragment).commit();
                        return true;
                    case R.id.item3:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragmentContainerView, mLeaderboardFragment).commit();
                        return true;
                    case R.id.item4:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragmentContainerView, mNotesFragment).commit();
                        return true;
                    case R.id.item5:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragmentContainerView, mSettingsFragment).commit();
                        return true;
                }

                return false;
            }
        });
    }
}