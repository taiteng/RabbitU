package com.example.rabbitu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Music model) {
        holder.mID.setText(model.getMusicID());
        holder.mName.setText(model.getMusicName());
        holder.mAudio.setText(model.getMusicAudio());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView mID, mName, mAudio;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            mID = itemView.findViewById(R.id.txt_id);
            mName = itemView.findViewById(R.id.txt_name);
            mAudio = itemView.findViewById(R.id.txt_audio);
        }
    }
}
