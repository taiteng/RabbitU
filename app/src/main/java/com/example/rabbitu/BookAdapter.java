package com.example.rabbitu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {
    private Context context;
    private List<File> bookFiles;

    public BookAdapter(Context context, List<File> pdfFiles) {
        this.context = context;
        this.bookFiles = pdfFiles;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(context).inflate(R.layout.book_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.textView.setText(bookFiles.get(position).getName());
        holder.textView.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return bookFiles.size();
    }
}
