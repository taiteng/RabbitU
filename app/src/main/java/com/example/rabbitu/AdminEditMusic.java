package com.example.rabbitu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminEditMusic extends AppCompatActivity {

    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private DatabaseReference databaseReference ;
    private StorageReference storageReference ;
    private Uri filepath;
    private String filename;
    public String music_id, music_name, music_audio, preMusicID;
    public boolean isVisible;

    EditText musicName, musicID, musicAudio;
    Button updateButton, uploadButton;
    ImageButton exitButton;
    ImageView checkedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_music);

        if(getIntent().hasExtra("musicID") && getIntent().hasExtra("musicName") && getIntent().hasExtra("musicAudio")){
            music_id = getIntent().getStringExtra("musicID");
            music_name = getIntent().getStringExtra("musicName");
            music_audio = getIntent().getStringExtra("musicAudio");
        }

        databaseReference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("MusicStorage");
        storageReference = FirebaseStorage.getInstance().getReference();
        musicName = findViewById(R.id.editMusicName);
        musicID = findViewById(R.id.editMusicID);
        musicAudio = findViewById(R.id.editMusicAudio);
        updateButton = findViewById(R.id.updateButton);
        exitButton = findViewById(R.id.exitIcon);
        uploadButton = findViewById(R.id.uploadButton);
        checkedImage = findViewById(R.id.checkedImage);

        checkedImage.setVisibility(View.INVISIBLE);

        isVisible = false;
        preMusicID = music_id;

        musicName.setText(music_name);
        musicID.setText(music_id);
        musicAudio.setText(music_audio);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminEditMusic.this,AdminMusic.class);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMusic();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                music_name = musicName.getText().toString();
                music_id = musicID.getText().toString();
                music_audio = musicAudio.getText().toString();

                if (TextUtils.isEmpty(music_name)){
                    musicName.setError("Name cannot be empty");
                    musicName.requestFocus();
                }
                else if(TextUtils.isEmpty(music_id)){
                    musicID.setError("ID cannot be empty");
                    musicID.requestFocus();
                }
                else if(TextUtils.isEmpty(music_audio)){
                    musicAudio.setError("Audio link cannot be empty");
                    musicAudio.requestFocus();
                }
                else{
                    if(isVisible){
                        updateMusicToFirebaseStorage();
                    }
                    else{
                        updateMusicToFirebaseDatabase();
                    }
                }
            }
        });
    }

    public void uploadMusic(){
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select MP3 file."),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            filename = filepath.toString();
            filename = filename.substring(filename.lastIndexOf("%")+1);
            filename = filename.substring(0, filename.indexOf("."));
            musicAudio.setText(filepath.toString());
            checkedImage.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(AdminEditMusic.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateMusicToFirebaseStorage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating...");
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("MusicStorage").child(preMusicID).removeValue();

        StorageReference reference = storageReference.child(filename + ".mp3");

        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri musicUri = uriTask.getResult();

                Music music = new Music(music_id, music_name, musicUri.toString());

                FirebaseDatabase.getInstance().getReference("MusicStorage")
                        .child(music_id).setValue(music).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminEditMusic.this,"Music Updated successfully",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AdminEditMusic.this,"Fail to update! Try again!",Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(AdminEditMusic.this,AdminMusic.class);
                        startActivity(intent);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Updating "+(int)progress+"%");
            }
        });
    }

    public void updateMusicToFirebaseDatabase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating...");
        progressDialog.show();

        FirebaseDatabase.getInstance().getReference().child("MusicStorage").child(preMusicID).removeValue();

        Music music = new Music(music_id, music_name, music_audio);

        FirebaseDatabase.getInstance().getReference("MusicStorage")
                .child(music_id).setValue(music).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AdminEditMusic.this,"Music Updated successfully",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AdminEditMusic.this,"Fail to update! Try again!",Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(AdminEditMusic.this,AdminMusic.class);
                startActivity(intent);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.setMessage("Updated!");
            }
        });
    }
}
