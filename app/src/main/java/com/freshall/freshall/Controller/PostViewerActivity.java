package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class PostViewerActivity extends AppCompatActivity {

    private int EDIT_POST_REQUEST = 4;
    private TextView mTextMessage;
    private Intent feedIntent;
    private Post selectedPost;
    User currentUser;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
//                    mTextMessage.setText(R.string.title_home);
                    Intent homeIntent = new Intent(PostViewerActivity.this, FeedActivity.class);
                    startActivity(homeIntent);
                    return true;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.nav_user:
                    Log.i("navigation", "user button press");
                    Intent profileIntent = new Intent(PostViewerActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_viewer);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // get post that was clicked to populate view
        feedIntent = getIntent();
        selectedPost = (Post) feedIntent.getSerializableExtra("selectedPost");
        currentUser = (User) feedIntent.getSerializableExtra("user");

        // if user clicked own post, FAB is edit button; else FAB is fave button
        if (selectedPost.getSeller().getEmail().equals(currentUser.getEmail())) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.favorite);
            fab.setImageResource(R.drawable.ic_edit);
        }

        // set title text
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(selectedPost.getTitle());

        // set description text
        TextView descText = (TextView) findViewById(R.id.description);
        descText.setText(selectedPost.getDescription());

        // set price text
        TextView priceText = (TextView) findViewById(R.id.price);
        priceText.setText("Price: " + selectedPost.getPricePerQuantity().toString());

        // set location text
        TextView locationText = (TextView) findViewById(R.id.locationText);
        locationText.setText(selectedPost.getLocation());

        // set quantity text
        String quantity = selectedPost.getQuantity() + " " + selectedPost.getQuantityType();
        TextView quantityText = (TextView) findViewById(R.id.quantity);
        quantityText.setText(quantity);

        // set user text
        TextView sellerText = (TextView) findViewById(R.id.sellerName);
        sellerText.setText(selectedPost.getSeller().getFullName());
    }

    // when FAB is clicked to add to favorites, shows toast
    public void fab_clicked(View view) {
        currentUser = (User) feedIntent.getSerializableExtra("user");

        // if seller selects own post, display edit button instead of favorites
        // on edit button clicked, send data from post to create_new_post to populate with
        if (selectedPost.getSeller().getEmail().equals(currentUser.getEmail())) {
            Intent editPostIntent = new Intent(PostViewerActivity.this, CreateNewPostActivity.class);
            editPostIntent.putExtra("current_post", selectedPost);
            startActivityForResult(editPostIntent, EDIT_POST_REQUEST);
        }

        // if not current user's post, add post to favorites
        else {
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();
        mPostDatabaseReference =
                mFirebaseDatabase.getReference().child("posts");

        // on edit result, delete old post and create new one
        // TODO: delete old post
        if (requestCode == EDIT_POST_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Post resultPost = (Post) data.getSerializableExtra("new_post");
                resultPost.setSeller(currentUser);
                mPostDatabaseReference.push().setValue(resultPost);
            }
            // after post is created, return to FeedView to open new post viewer
            setResult(RESULT_OK);
            finish();
        }
    }
}
