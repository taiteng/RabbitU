package com.example.rabbitu;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookAdapter extends FirebaseRecyclerAdapter<PdfClass,BookAdapter.ViewHolder> {
    public BookAdapter(@NonNull FirebaseRecyclerOptions<PdfClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position, @NonNull PdfClass model) {
        holder.title.setText(String.valueOf(model.getName()));
        holder.author.setText(String.valueOf(model.getAuthor()));
        holder.coin.setText(String.valueOf(model.getCoin()));
        Glide.with(holder.cover.getContext()).load(model.getImageUrl()).into(holder.cover);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.card.getContext(),OpenBook.class);
                intent.putExtra("FileURL",model.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.card.getContext().startActivity(intent);

            }
        });

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent,false);
        return new BookAdapter.ViewHolder(view);
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title,author,coin;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.book_cover);
            title = itemView.findViewById(R.id.book_title);
            card=itemView.findViewById(R.id.book_cardView);
            author=itemView.findViewById(R.id.book_author);
            coin=itemView.findViewById(R.id.book_coin);
        }
    }
}

