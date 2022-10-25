package com.example.rabbitu;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public CardView cardView;


    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        textView=itemView.findViewById(R.id.book_title);
        cardView=cardView.findViewById(R.id.book_cardView);
    }
}
