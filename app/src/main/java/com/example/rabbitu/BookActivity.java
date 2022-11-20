package com.example.rabbitu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rabbitu.databinding.ActivityBookBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ActivityBookBinding mBookBinding;
    DatabaseReference databaseReference;
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    BottomNavigationView mBottomNavigationView;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("BookStorage");
        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item2);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions <PdfClass> options=new FirebaseRecyclerOptions.Builder<PdfClass>().setQuery(
                databaseReference,PdfClass.class
        ).build();

//        ArrayList<Book> books = new ArrayList<>();
//        books.add(new Book("Java", "Java Book.pdf", R.drawable.java_book));
//        books.add(new Book("Coding Theory", "Coding Theory.pdf", R.drawable.coding_theory));
        bookAdapter = new BookAdapter( options);

        recyclerView.setAdapter(bookAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item2:
                        return true;
                    case R.id.item3:
                        finish();
                        startActivity(new Intent(BookActivity.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(BookActivity.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        finish();
                        startActivity(new Intent(BookActivity.this,Settings.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }

                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        bookAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookAdapter.stopListening();
    }
}