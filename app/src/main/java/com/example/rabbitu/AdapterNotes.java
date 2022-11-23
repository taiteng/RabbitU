package com.example.rabbitu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.MyViewHolder> {

    private List<ModelNotes> mList;
    private Activity activity ;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();


    public AdapterNotes(List<ModelNotes>mList, Activity activity){
        this.mList = mList;
        this.activity = activity;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item,parent,false);

        return new MyViewHolder(viewItem);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelNotes data = mList.get(position);
        holder.title_name.setText("Title : " + data.getTitle());
        holder.description_name.setText("Description : " + data.getDesc());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Notes").child(data.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Note Deleted !", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Fail to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Are you sure want to Delete ? " + data.getTitle());
                builder.show();
            }
        });


        holder.card_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager manager = ((AppCompatActivity)activity).getSupportFragmentManager();
                DialogForm dialolog = new DialogForm(
                        data.getTitle(),
                        data.getDesc(),
                        data.getKey(),
                        "Change"
                );
                dialolog.show(manager,"form");

                return true;
            }
        });




    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_name , description_name ;
        CardView card_content ;
        ImageView btn_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title_name = itemView.findViewById(R.id.title_name);
            description_name = itemView.findViewById(R.id.description_name);
            card_content = itemView.findViewById(R.id.card_content);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }

}
