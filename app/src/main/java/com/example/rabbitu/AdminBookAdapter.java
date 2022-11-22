package com.example.rabbitu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminBookAdapter extends FirebaseRecyclerAdapter<PdfClass,AdminBookAdapter.ViewHolder> {
    private DatabaseReference reference ;
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";


    public AdminBookAdapter(@NonNull FirebaseRecyclerOptions<PdfClass> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AdminBookAdapter.ViewHolder holder, int position, @NonNull PdfClass model) {
        holder.title.setText(String.valueOf(model.getName()));
        holder.author.setText(String.valueOf(model.getAuthor()));

        holder.coin.setText(String.valueOf(model.getCoin()));
        Glide.with(holder.cover.getContext()).load(model.getImageUrl()).into(holder.cover);
        reference = FirebaseDatabase.getInstance().getReference().child("BookStorage");

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=reference.orderByChild("name").equalTo(model.getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            dataSnapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.card.getContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_book_card, parent,false);
        return new AdminBookAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title,author,coin;
        CardView card;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.admin_book_cover);
            title = itemView.findViewById(R.id.admin_book_title);
            card=itemView.findViewById(R.id.admin_book_cardView);
            author=itemView.findViewById(R.id.admin_book_author);
            coin=itemView.findViewById(R.id.admin_book_coin);
            button=itemView.findViewById(R.id.admin_delete_button);
        }
    }
}

