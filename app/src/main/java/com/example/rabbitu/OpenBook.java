package com.example.rabbitu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class OpenBook extends AppCompatActivity {
    PDFView pdfViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_book);
        pdfViewer=findViewById(R.id.pdfViewer);

        String getFile=getIntent().getStringExtra("PdfName");
        pdfViewer.fromAsset(getFile).load();
    }
}