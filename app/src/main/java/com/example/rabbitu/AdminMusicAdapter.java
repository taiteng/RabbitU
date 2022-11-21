package com.example.rabbitu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AdminMusicAdapter extends FirebaseRecyclerAdapter<Music, AdminMusicAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminMusicAdapter(@NonNull FirebaseRecyclerOptions<Music> options) {
        super(options);
    }

    private Context mContext;

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Music model) {
        holder.mID.setText(model.getMusicID());
        holder.mName.setText(model.getMusicName());
        holder.mAudio.setText(model.getMusicAudio());

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext = holder.editBtn.getContext();

                final Intent intent = new Intent(mContext, AdminEditMusic.class);
                intent.putExtra("musicID", model.getMusicID());
                intent.putExtra("musicName", model.getMusicName());
                intent.putExtra("musicAudio", model.getMusicAudio());
                mContext.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.mName.getContext());
                builder.setTitle("Are you SURE?");
                builder.setMessage("Deleted data can't be undone");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("MusicStorage").child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.mName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView mID, mName, mAudio;

        Button editBtn, deleteBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            mID = itemView.findViewById(R.id.txt_id);
            mName = itemView.findViewById(R.id.txt_name);
            mAudio = itemView.findViewById(R.id.txt_audio);
            editBtn = itemView.findViewById(R.id.editButton);
            deleteBtn = itemView.findViewById(R.id.deleteButton);
        }
    }
}
