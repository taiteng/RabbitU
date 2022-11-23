package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    EditText edTitle, edDescription;
    Button btn_keep;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edTitle = findViewById(R.id.edTitle);
        edDescription = findViewById(R.id.edDescription);
        btn_keep = findViewById(R.id.btn_keep);

        btn_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName = edTitle.getText().toString();
                String getDesc = edDescription.getText().toString();

                if(getName.isEmpty()){
                    edTitle.setError("Enter Title");
                }else if (getDesc.isEmpty()){
                    edDescription.setError("Enter Description");
                }else{
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Notes").push().setValue(new ModelNotes(getName,getDesc)).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             Toast.makeText(AddActivity.this,"Notes added ! ",Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(AddActivity.this,Notes.class));
                             finish();
                         }
                    }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(AddActivity.this," Failed to add Notes ! ",Toast.LENGTH_SHORT).show();
                         }
                    });
                }
            }
        });
    }
}