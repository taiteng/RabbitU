package com.example.rabbitu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class OpenBook extends AppCompatActivity {
    PDFView pdfViewer;
    FloatingActionButton warpPage;
    BottomNavigationView mBottomNavigationView;
    String fileURL = "";
    public int bookPage = 0;
    public String bookURL, bookPAGE;

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences data = getSharedPreferences("isBookActive", 0);
        bookURL = data.getString("BookURL", "Null");
        bookPAGE = data.getString("BookPage", "Null");

        if(fileURL.equals(bookURL)){
            warpPage.setVisibility(View.VISIBLE);
        }else{
            warpPage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_book);
        pdfViewer=findViewById(R.id.pdfViewer);
        fileURL=getIntent().getStringExtra("FileURL");

        warpPage = findViewById(R.id.warp_page);
        warpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileURL.equals(bookURL)){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OpenBook.this);
                    alertDialogBuilder.setMessage("Go to last read page?");
                            alertDialogBuilder.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            int page = Integer.valueOf(bookPAGE);
                                            pdfViewer.jumpTo(page);
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    Toast.makeText(OpenBook.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (isConnected()) {
            Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(OpenBook.this);
            builder.setTitle("No Internet Connection")
                    .setMessage("Go to Setting ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(OpenBook.this,"Go Back TO HomePage!",Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating dialog box
            AlertDialog dialog  = builder.create();
            dialog.show();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fileURL = getIntent().getStringExtra("FileURL");
        }
        new OpenBook.RetrievePDFStream().execute(fileURL);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationView.setSelectedItemId(R.id.item2);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.item1:
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item2:
                        return true;
                    case R.id.item3:
                        finish();
                        startActivity(new Intent(OpenBook.this,Leaderboard.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item4:
                        finish();
                        startActivity(new Intent(OpenBook.this,Notes.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                    case R.id.item5:
                        finish();
                        startActivity(new Intent(OpenBook.this,Settings.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        return true;
                }

                return false;
            }
        });
    }

    private boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    class RetrievePDFStream extends AsyncTask<String, Void, InputStream> {

        ProgressDialog progressDialog;
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(OpenBook.this);
            progressDialog.setTitle("Getting the book content...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {

                URL urlx = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) urlx.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfViewer.fromStream(inputStream).load();
            progressDialog.dismiss();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)//means home default hai kya yesok
        {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        bookPage = pdfViewer.getCurrentPage();

        SharedPreferences data = getSharedPreferences("isBookActive", 0);
        SharedPreferences.Editor editor = data.edit();
        editor.putString("BookURL", fileURL);
        editor.putString("BookPage", String.valueOf(bookPage));
        editor.commit();
    }
}