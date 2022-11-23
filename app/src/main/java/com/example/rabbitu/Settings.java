package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private FirebaseUser user ;
    private DatabaseReference reference, musicReference ;
    private String userID;
    private ArrayList<Music> musicArrayList = new ArrayList<>();
    private String equippedMusicID = "";
    private int coinsGet = 0;
    private boolean isMuteMusic = false;
    private boolean isActive = false;

    BottomNavigationView mBottomNavigationView;
    FirebaseAuth mAuth;
    Button LogoutBtn, MusicBtn;
    TextView mail, name, phoneNumber, email, coins;
    Switch MuteSwitch;
    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        musicReference = FirebaseDatabase.getInstance().getReference("MusicStorage");
        userID = user.getUid();

        LogoutBtn = findViewById(R.id.button_logout);
        MuteSwitch = findViewById(R.id.mute_switch);
        MusicBtn = findViewById(R.id.button_music);
        mail = findViewById(R.id.userEmail);

        name = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        coins = findViewById(R.id.coinsValue);

        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item5);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        //Get the details on google account
        if (account!=null) {
            String Mail = account.getEmail();
            mail.setText(Mail);
        }

        LogoutBtn.setOnClickListener(view->{
            mAuth.signOut();
            signOut();
            startActivity(new Intent(Settings.this,Login.class));
        });

        //connect database and retrieve data
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equippedMusicID = snapshot.child("equippedMusicID").getValue(String.class);
                isMuteMusic = snapshot.child("isMuteMusic").getValue(boolean.class);

                if(isMuteMusic == true){
                    MuteSwitch.setChecked(true);
                }
                else{
                    MuteSwitch.setChecked(false);
                }

                coinsGet = snapshot.child("coins").getValue(int.class);
                coins.setText(String.valueOf(coinsGet));

                String fullNameTxt = snapshot.child("fullName").getValue(String.class);
                String emailTxt = snapshot.child("email").getValue(String.class);
                String phoneNumberTxt = snapshot.child("phoneNumber").getValue(String.class);

                name.setText(fullNameTxt);
                email.setText(emailTxt);
                phoneNumber.setText(phoneNumberTxt);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this,"Something went wrong" ,Toast.LENGTH_SHORT).show();
            }
        });

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item2:
                        finish();
                        startActivity(new Intent(Settings.this,BookActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item3:
                        finish();
                        startActivity(new Intent(Settings.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(Settings.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        return true;
                }

                return false;
            }
        });

        MusicBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showMusic();
            }
        });

        MuteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean switchState = MuteSwitch.isChecked();
                if(switchState == true){
                    reference.child(userID).child("isMuteMusic").setValue(true);
                }
                else{
                    reference.child(userID).child("isMuteMusic").setValue(false);
                }
            }
        });
    }

    void signOut() {
        signInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(Settings.this,Login.class));
            }
        });
    }

    /**
     * Show available music and allow user to equip music
     */
    public void showMusic(){

        //Initialization of variables to create a dialog box
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogBuilder.setView(inflater.inflate(R.layout.settings_music, null));
        dialog = dialogBuilder.create();

        //Display the dialog box
        dialog.show();

        //Initialization of elements in the dialog box
        GridView musicGridView = dialog.findViewById(R.id.musicGridView);

        //Retrieve data from Firebase
        musicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Clear the arraylist
                musicArrayList.clear();

                //Loop to retrieve all the data in Music_Storage
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    //Retrieve the data for each music
                    String musicID = dataSnapshot.child("musicID").getValue(String.class);
                    String musicName = dataSnapshot.child("musicName").getValue(String.class);
                    String musicAudio = dataSnapshot.child("musicAudio").getValue(String.class);

                    //Insert the music data into a Music object
                    Music music = new Music(musicID, musicName, musicAudio);

                    //Add the Music object to the arraylist
                    musicArrayList.add(music);
                }

                //Set the gridview for the musicArrayList
                MusicGVAdapter adapter = new MusicGVAdapter(Settings.this, musicArrayList, equippedMusicID);
                musicGridView.setAdapter(adapter);
            }

            //Display database error if any
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Unable to connected to database. Please try again. Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        //Change the equipped music when an item in the grid is clicked
        musicGridView.setOnItemClickListener((parent, view, position, id) -> {
            //Get the music that was clicked
            Music music = musicArrayList.get(position);

            //Change the equipped music in the database
            reference.child(userID).child("equippedMusicID").setValue(music.getMusicID());
            reference.child(userID).child("equippedMusicAudio").setValue(music.getMusicAudio());
            dialog.dismiss();
        });
    }
}