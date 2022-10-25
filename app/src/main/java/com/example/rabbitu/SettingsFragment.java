package com.example.rabbitu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    private View rootview;

    FirebaseAuth mAuth;
    Button LogoutBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        LogoutBtn = rootview.findViewById(R.id.btnLogout);

        LogoutBtn.setOnClickListener(view->{
            mAuth.signOut();
            startActivity(new Intent(getActivity().getBaseContext().getApplicationContext(),Login.class));
        });

        return rootview;
    }

    //Check the user logout or not
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user == null){
//            startActivity(new Intent(Setting.this,Login.class));
//        }
//    }
}