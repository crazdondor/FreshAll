package com.freshall.freshall.Controller;

import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateNewPostActivity extends AppCompatActivity {
    boolean postHasTitle;
    Post selectedPost;

    private FusedLocationProviderClient mFusedLocationClient;

    static final int REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        // set adapter to populate quantity type spinner
        Spinner quantityTypeSpinner = (Spinner) findViewById(R.id.quantityType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quantity_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityTypeSpinner.setAdapter(adapter);

        // if editing post, populate layout with intent variables and add mark sold button
        Intent editIntent = getIntent();
        selectedPost = (Post) editIntent.getSerializableExtra("current_post");

        if (selectedPost != null) {

            EditText titleEditor = (EditText) findViewById(R.id.titleText);
            titleEditor.setText(selectedPost.getTitle());

            EditText descriptionEditor = (EditText) findViewById(R.id.description);
            descriptionEditor.setText(selectedPost.getDescription());

            EditText priceEditor = (EditText) findViewById(R.id.price);
            priceEditor.setText(selectedPost.getPricePerQuantity());

            TextView locationEditor = (TextView) findViewById(R.id.locationText);
            locationEditor.setText(selectedPost.getLocation());

            EditText quantityEditor = (EditText) findViewById(R.id.quantityNumber);
            quantityEditor.setText(selectedPost.getQuantity());

            // setSelection takes int value
            // TODO: figure out how to determine int value from String quantityType in Post obj
//        Spinner quantityTypeEditor = (Spinner) findViewById(R.id.quantityType);
//        quantityTypeEditor.setSelection(0);

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

    // when cancel button is clicked, finish activity without saving data
    public void cancelPost(View view) {
        setResult(RESULT_OK, null);
        finish();
    }

    // when confirm button is clicked, create new post object with entered fields
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

        newPost.setIsSold(false);

        // return post to PostFeed activity
        if (postHasTitle) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("new_post", newPost);
            setResult(RESULT_OK, returnIntent);
            finish();
        } else {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
        }
    }

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
}