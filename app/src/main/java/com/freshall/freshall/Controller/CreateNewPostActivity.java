package com.freshall.freshall.Controller;

import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreateNewPostActivity extends AppCompatActivity {
    boolean postHasTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        // set adapter to populate quantity type spinner
        Spinner quantityTypeSpinner = (Spinner) findViewById(R.id.quantityType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quantity_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityTypeSpinner.setAdapter(adapter);

        // if editing post, populate layout with intent variables
        Intent editIntent = getIntent();
        Post selectedPost = (Post) editIntent.getSerializableExtra("current_post");

        if (selectedPost != null) {

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

            // setSelection takes int value
            // TODO: figure out how to determine int value from String quantityType in Post obj
//        Spinner quantityTypeEditor = (Spinner) findViewById(R.id.quantityType);
//        quantityTypeEditor.setSelection(0);
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
            EditText locationEditor = (EditText) findViewById(R.id.locationText);
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
}
