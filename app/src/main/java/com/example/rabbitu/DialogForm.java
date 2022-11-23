package com.example.rabbitu;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogForm extends DialogFragment {

    String title, desc,key, choice ;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm(String title, String desc, String key, String choice) {
        this.title = title;
        this.desc = desc;
        this.key = key;
        this.choice = choice;
    }

    TextView edtitle, edDesc;
    Button btn_keep;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_add,container,false);
        edtitle = view.findViewById(R.id.edTitle);
        edDesc = view.findViewById(R.id.edDescription);
        btn_keep = view.findViewById(R.id.btn_keep);

        edtitle.setText(title);
        edDesc.setText(desc);

        btn_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edtitle.getText().toString();
                String Desc = edDesc.getText().toString();

                if(choice.equals("Change")){
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("Notes").child(key).setValue(new ModelNotes(title,Desc)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Note Updated", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "Fail to Update", Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        }
    }
}
