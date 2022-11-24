package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEditBook extends AppCompatActivity {
    RecyclerView mRecyclerView;
    AdminBookAdapter adapter;
    DatabaseReference databaseReference;
    ExtendedFloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_book);
        mRecyclerView=findViewById(R.id.admin_recycler_view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("BookStorage");

        FirebaseRecyclerOptions <PdfClass> options=new FirebaseRecyclerOptions.Builder<PdfClass>().setQuery(
                databaseReference,PdfClass.class
        ).build();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdminBookAdapter(options);

        mRecyclerView.setAdapter(adapter);

        mFloatingActionButton = findViewById(R.id.add_book);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEditBook.this,AdminBook.class);
                startActivity(intent);
            }
        });
    }

    //Back button
    public void onBackClick(View View){
        Intent intent = new Intent(AdminEditBook.this,Admin.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}