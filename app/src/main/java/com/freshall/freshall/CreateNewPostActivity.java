package com.freshall.freshall;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_post);

        // set adapter to populate quantity type spinner
        Spinner quantityTypeSpinner = (Spinner) findViewById(R.id.quantityType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.quantity_type_spinner_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityTypeSpinner.setAdapter(adapter);
    }

    // when confirm button is clicked, create new post object with entered fields
    public void createPost(View view) {
        Post newPost = new Post();

        // if title text exists, add to new post
        EditText titleEditor = (EditText) findViewById(R.id.titleText);
        String title = titleEditor.getText().toString();
        if (!title.equals("")) { // if not empty
            newPost.setTitle(title);
        }

        // if description text exists, add to new post
        EditText descriptionEditor = (EditText) findViewById(R.id.description);
        String description = descriptionEditor.getText().toString();
        if (!description.equals("")) {
            newPost.setDescription(description);
        }

        // return post to PostFeed activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("new_post", newPost);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
