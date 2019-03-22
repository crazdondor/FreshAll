package com.freshall.freshall.Controller;

import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateNewPostActivity extends AppCompatActivity {
//    variables:
    boolean postHasTitle;
    Post selectedPost;
    private ImageView postPhotoView;
    private ProgressDialog progressDialog;
    private Uri filePath;
    private FusedLocationProviderClient mFusedLocationClient;
    static final int REQUEST_LOCATION = 1;
    private static final  int PICK_IMAGE_REQUEST = 2;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

//        get reference to photo view and set on click listener to allow user to select a photo
        postPhotoView = (ImageView) findViewById(R.id.postPhoto);
        postPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

//         set adapter to populate quantity type spinner
        Spinner quantityTypeSpinner = (Spinner) findViewById(R.id.quantityType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quantity_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityTypeSpinner.setAdapter(adapter);

        // if editing post, populate layout with intent variables and add mark sold button
        Intent editIntent = getIntent();
        selectedPost = (Post) editIntent.getSerializableExtra("current_post");

        if (selectedPost != null) {
            populateFields(selectedPost);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // location granted, get location
            final TextView locationText = (TextView) findViewById(R.id.locationText);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location == null) {
                                locationText.setText("Null location");
                            } else {
                                setLocationText(location);
                            }
                        }
                    });
        } else {
            // permission not granted, ask user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    /**
     * When photo view clicked, starts activity to select photo saved on phone
     */
    private void showFileChooser() {
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /**
     * Gets reference to XML views and sets values using values from current post
     */
    private void populateFields(Post currentPost) {
        EditText titleEditor = (EditText) findViewById(R.id.titleText);
        titleEditor.setText(selectedPost.getTitle());

        EditText descriptionEditor = (EditText) findViewById(R.id.description);
        descriptionEditor.setText(selectedPost.getDescription());

        EditText priceEditor = (EditText) findViewById(R.id.price);
        priceEditor.setText(selectedPost.getPricePerQuantity());

        EditText locationEditor = (EditText) findViewById(R.id.locationText);
        locationEditor.setText(selectedPost.getLocation());

        EditText quantityEditor = (EditText) findViewById(R.id.quantityNumber);
        quantityEditor.setText(selectedPost.getQuantity());

        postPhotoView = (ImageView) findViewById(R.id.postPhoto);
        if (postPhotoView.getDrawable() != null) {
            uploadPhoto(selectedPost);
        }

//        for quantity type spinner, setSelection() takes int value
//          gets reference to array adapter (strings list) to get position of saved string
        Spinner quantityTypeEditor = (Spinner) findViewById(R.id.quantityType);
        String postQuantityType = selectedPost.getQuantityType();
        int qtPosition = ((ArrayAdapter<String>)quantityTypeEditor.getAdapter()).getPosition(postQuantityType);
        quantityTypeEditor.setSelection(qtPosition);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=  null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postPhotoView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * when cancel button is clicked, finish activity without saving data
     * @param view
     */
    public void cancelPost(View view) {
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * when confirm button is clicked, create new post object with entered fields
     * if no title, post will not be created (toast message appears)
     * @param view
     */
    public void createPost(View view) {
        Post newPost = new Post();

        // if title text exists, add to new post
        EditText titleEditor = (EditText) findViewById(R.id.titleText);
        String title = titleEditor.getText().toString();
        if (!title.equals("")) { // if not empty
            newPost.setTitle(title);
            postHasTitle = true;
        } else {
            postHasTitle = false;

        }

        postPhotoView = (ImageView) findViewById(R.id.postPhoto);
        if (postPhotoView.getDrawable() != null) {
            uploadPhoto(newPost);
        }

        // if description text exists, add to new post
        EditText descriptionEditor = (EditText) findViewById(R.id.description);
        String description = descriptionEditor.getText().toString();
        if (!description.equals("")) {
            newPost.setDescription(description);
        }

        // if price text exists, add to new post
        EditText priceEditor = (EditText) findViewById(R.id.price);
        String price = priceEditor.getText().toString();
        if (!price.equals("")) {
            newPost.setPricePerQuantity(price);
        }

        // if location text exists, add to new post
        TextView locationEditor = (TextView) findViewById(R.id.locationText);
        String location = locationEditor.getText().toString();
        if (!location.equals("")) {
            newPost.setLocation(location);
        }

        // if quantity text exists, add to new post
        EditText quantityEditor = (EditText) findViewById(R.id.quantityNumber);
        String quantity = quantityEditor.getText().toString();
        if (!quantity.equals("")) {
            newPost.setQuantity(quantity);
        }

        // if quantity type exists, add to new post
        Spinner quantityTypeEditor = (Spinner) findViewById(R.id.quantityType);
        String quantityType = quantityTypeEditor.getSelectedItem().toString();
        if (!quantityType.equals("")) {
            newPost.setQuantityType(quantityType);
        }

        //post not yet sold
        newPost.setIsSold(false);

        // set post date to current date
        long postDate = new Date().getTime();
        postDate *= -1;
        newPost.setPostDate(postDate);

        // if post has title, return it to PostFeed activity,
        //  else, toast message to user
        if (postHasTitle) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("new_post", newPost);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     * @param location
     */
    private void setLocationText(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
        } catch (IOException ioException) {
            Log.e("Location", "Service not Available", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            Log.e("Location", "Bad Arguments", illegalArgumentException);
        }

        if (addresses == null || addresses.size() == 0) {
            // no address found
            Log.e("Location", "No addresses found");
        } else {
            // set location TextView
            TextView locationText = (TextView) findViewById(R.id.locationText);
            locationText.setText((CharSequence) addresses.get(0).getLocality());
        }
    }

    /**
     *
     * @param newPost
     */
    private void uploadPhoto(Post newPost) {
        //if there is a file to upload
        if (filePath != null) {
            //display a progress dialog for upload
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://freshall-5c50e.appspot.com");
            StorageReference photoRef = storageReference.child("images/posts/" + newPost.getUuid() + ".jpg");

            photoRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successful
                            progressDialog.dismiss();

                            //display a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successful
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}