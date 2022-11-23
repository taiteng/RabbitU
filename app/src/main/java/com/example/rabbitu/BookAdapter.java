package com.example.rabbitu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends FirebaseRecyclerAdapter<PdfClass,BookAdapter.ViewHolder> {
    private DatabaseReference reference ;
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private String userID ;
    private FirebaseUser user ;
    FirebaseAuth mAuth;
    List<String> bookList;
    private DatabaseReference coinReference;
    private int coin;
    private Context mContext;


    public BookAdapter(@NonNull FirebaseRecyclerOptions<PdfClass> options) {
        super(options);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position, @NonNull PdfClass model) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        bookList=new ArrayList<>();

        reference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID).child("bookList");
        coinReference=FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID).child("coins");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {


                    bookList.add(String.valueOf(ds.getValue()));
                }

                    holder.title.setText(String.valueOf(model.getName()));
                    holder.author.setText(String.valueOf(model.getAuthor()));

                    holder.coin.setText(String.valueOf(model.getCoin()));
                    Glide.with(holder.cover.getContext()).load(model.getImageUrl()).into(holder.cover);


                    if(bookList.contains(model.getName())) {
                        holder.card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(holder.card.getContext(), OpenBook.class);
                                intent.putExtra("FileURL", model.getUrl());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                holder.card.getContext().startActivity(intent);

                            }
                        });

                    }
                    else{
                        holder.card.setCardBackgroundColor(R.color.grey);

                        coinReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                coin=dataSnapshot.getValue(int.class);
                                if(coin>=model.getCoin()){
                                    holder.card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(holder.cover.getContext());
                                            builder.setMessage("Do you want to buy the book?")
                                                    .setTitle("Confirmation Dialog")
                                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            bookList.add(model.getName());
                                                            FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID).child("bookList").push().setValue(model.getName());

                                                            FirebaseDatabase.getInstance(firebaseURL).getReference().child("Users").child(userID).child("coins").setValue(coin-model.getCoin());

                                                            mContext = holder.card.getContext();
                                                            final Intent intent = new Intent(mContext, BookActivity.class);
                                                            mContext.startActivity(intent);
                                                        }
                                                    })
                                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // CANCEL
                                                        }
                                                    });
                                            // Create the AlertDialog object and return it
                                             AlertDialog alertDialog=builder.create();
                                             alertDialog.show();

                                        }
                                    });
                                }
                                else{
                                    holder.card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(holder.cover.getContext(),"Read more books to earn more moneys.",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(holder.card.getContext(),"Something went wrong" ,Toast.LENGTH_SHORT).show();
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

