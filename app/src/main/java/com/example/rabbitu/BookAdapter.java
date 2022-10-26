package com.example.rabbitu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Book> mBooks;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.mBooks = books;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view =layoutInflater.inflate(R.layout.book_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book=mBooks.get(position);
        holder.textView.setText(book.getTitle());
        holder.imageView.setImageResource(book.getImage());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.book_title);
            imageView=itemView.findViewById(R.id.book_cover);
        }
    }
}
