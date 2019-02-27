package com.freshall.freshall.Controller;

//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.media.Image;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.freshall.freshall.Model.Post;
//import com.freshall.freshall.R;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private ArrayList<Post> allPosts;
//    private ArrayList<Post> userPosts;
//    private ArrayAdapter<Post> arrayAdapter;
//    private ListView listView;
//    private String username;
//
//    private DatabaseReference mPostDatabaseReference;
//    public ChildEventListener mPostChildEventListener;
//
//    ImageView imageView;
//
//
//    static final int REQUEST_IMAGE_CAPTURE = 0;
////    static final int WRITE_EXTERNAL_REQUEST = 0;
////    static final int CHOOSE_IMAGE = 101;
//
//    Uri uriProfileImage;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    Log.i("navigation", "home button press");
////                    mTextMessage.setText(R.string.title_home);
//                    Intent homeIntent = new Intent(ProfileActivity.this, FeedActivity.class);
//                    startActivity(homeIntent);
//                    break;
//                case R.id.nav_message:
//                    Log.i("navigation", "message button press");
////                    mTextMessage.setText(R.string.title_notifications);
////                    Intent messagingIntent = new Intent(ProfileActivity.this, MessagingActivity.class);
////                    startActivity(messagingIntent);
//                    break;
//                case R.id.nav_user:
//                    Log.i("navigation", "user button press");
//                    break;
//            }
//            return false;
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//
//        Intent feedIntent = getIntent();
//        username = (String) feedIntent.getStringExtra("userName");
//
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        TextView nameText = (TextView) findViewById(R.id.nameText);
//        nameText.setText(username);
//
//        Menu menu = navigation.getMenu();
//        MenuItem menuItem = menu.getItem(2);
//        menuItem.setChecked(true);
//
//        Button btnCam = (Button) findViewById(R.id.btnCam);
//
//
//        btnCam.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                takePicture();
//            }
//        });
////        includesForCreateReference();
////        uploadImageToFirebase();
////        showExistingPhotos();
//    }
////    public void includesForCreateReference(){
////        FirebaseStorage storage = FirebaseStorage.getInstance();
////
////        StorageReference storageRef = storage.getReference("gs://freshall-5c50e.appspot.com");
////        StorageReference imagesRef = storageRef.child("images");
////
////    }
//    private void takePicture(){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//    }
////    public void takeAndSavePicture() {
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_REQUEST
////            );
////        } else {
////            // we have permission
////        }
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////
////        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            uriProfileImage = data.getData();
////            try{
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
////                imageView.setImageBitmap(bitmap);
////
////            } catch (IOException e){
////                e.printStackTrace();
////            }
////        }
////    }
////
////    private void showExistingPhotos(){
////        Intent intent = new Intent();
////        intent.setType("image/*");
////        intent.setAction(Intent.ACTION_GET_CONTENT);
////        startActivityForResult(Intent.createChooser(intent, "Select Image"), CHOOSE_IMAGE);
////    }
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////        if (requestCode == WRITE_EXTERNAL_REQUEST) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                // try again
////                takeAndSavePicture();
////            }
////            else {
////                Toast.makeText(this, "We need permission!", Toast.LENGTH_SHORT).show();
////            }
////        }
////    }
////    private void uploadImageToFirebase(){
////        FirebaseStorage storage = FirebaseStorage.getInstance();
////        StorageReference profileImage = FirebaseStorage.getInstance().getReference();
////    }
////
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode == REQUEST_IMAGE_CAPTURE){
//            if(resultCode == Activity.RESULT_OK){
//                ImageView imageView = (ImageView) findViewById(R.id.profpic);
//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }
//
//    public void captureImage(View camButton){
//
//    }
//}

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener /*  implementing click listener */ {
    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        imageView = (ImageView) findViewById(R.id.imageView);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        //if the clicked button is choose
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }
    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference photoRef = storageReference.child("images/profilePictures/"+ UUID.randomUUID() +".jpg");
            photoRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }




}