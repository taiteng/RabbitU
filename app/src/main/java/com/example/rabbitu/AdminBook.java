package com.example.rabbitu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminBook extends AppCompatActivity {
    final String firebaseURL = "https://rabbitu-ae295-default-rtdb.firebaseio.com/";
    private FirebaseUser user ;
    private DatabaseReference databaseReference ;
    private StorageReference storageReference ;
    private String userID = "";
    Button uploadFile,uploadImage,upload;
    Uri book;
    EditText editText,authorText,coinText;
    ImageView tickImage,tickImage2;
    private Uri filepath;
    private Uri imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        databaseReference = FirebaseDatabase.getInstance(firebaseURL).getReference().child("BookStorage");
        storageReference= FirebaseStorage.getInstance().getReference();
        uploadFile =findViewById(R.id.uploadButton);
        uploadImage=findViewById(R.id.uploadImageButton);
        upload=findViewById(R.id.upload);
        editText=findViewById(R.id.bookName);
        authorText=findViewById(R.id.bookAuthor);
        coinText=findViewById(R.id.bookCost);
        tickImage=findViewById(R.id.tickImage);
        tickImage2=findViewById(R.id.tickImage2);

        tickImage.setVisibility(View.INVISIBLE);
        tickImage2.setVisibility(View.INVISIBLE);

        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFiles();
            }
        });
    }

    private void selectFile() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select book file."),1);
    }
    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image."),2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filepath=data.getData();
            uploadFile.setText(filepath.toString());
            tickImage.setVisibility(View.VISIBLE);

        }
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imagepath=data.getData();
            uploadImage.setText(filepath.toString());
            tickImage2.setVisibility(View.VISIBLE);

        }
    }

    private void uploadFiles() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference reference=storageReference.child("Uploads/"+System.currentTimeMillis()+".pdf");
        StorageReference imageReference=storageReference.child("Images/"+System.currentTimeMillis());

        imageReference.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                book=uriTask.getResult();


            }
        });

        reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri uri=uriTask.getResult();
                //getImage();
                PdfClass pdfClass=new PdfClass(editText.getText().toString(),uri.toString(),book.toString(),authorText.getText().toString(),Integer.valueOf(coinText.getText().toString()));
                databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);
                Toast.makeText(AdminBook.this,"File uploaded",Toast.LENGTH_LONG).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });
    }

    //Back button
    public void onBackClick(View View){
        Intent intent = new Intent(AdminBook.this,AdminEditBook.class);
        startActivity(intent);
    }
}
