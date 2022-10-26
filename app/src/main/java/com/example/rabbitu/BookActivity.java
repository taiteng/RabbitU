package com.example.rabbitu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.rabbitu.databinding.ActivityBookBinding;

import java.io.File;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ActivityBookBinding mBookBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book);
        recyclerView=findViewById(R.id.recycler_view);
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("Java", new File("./Java Book.pdf"), R.drawable.book_icon));
        BookAdapter bookAdapter = new BookAdapter(this, books);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bookAdapter);


    }
}