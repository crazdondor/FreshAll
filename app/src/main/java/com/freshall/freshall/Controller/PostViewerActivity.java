package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PostViewerActivity extends AppCompatActivity {

    private int EDIT_POST_REQUEST = 4;
    private TextView mTextMessage;
    private Intent feedIntent;
    private Post selectedPost;
    User currentUser;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;
    public DatabaseReference mFavoritesDatabaseReference;

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
                    profileIntent.putExtra("username", currentUser.getFullName());
                    profileIntent.putExtra("user", currentUser);
                    startActivity(profileIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_viewer);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // get post that was clicked to populate view
        feedIntent = getIntent();
        selectedPost = (Post) feedIntent.getSerializableExtra("selectedPost");
        currentUser = (User) feedIntent.getSerializableExtra("user");

        // if user clicked own post, FAB is edit button and mark sold button visible
        if (selectedPost.getSellerEmail().equals(currentUser.getEmail())) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.favorite);
            fab.setImageResource(R.drawable.ic_edit);

            Button markSoldButton = (Button) findViewById(R.id.postSold);
            markSoldButton.setVisibility(View.VISIBLE);

            Button deleteButton = (Button) findViewById(R.id.deletePost);
            deleteButton.setVisibility(View.VISIBLE);
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
        sellerText.setText(selectedPost.getSeller());

        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://freshall-5c50e.appspot.com");
        StorageReference photoRef = storageReference.child("images/posts/"
                + selectedPost.getUuid()
                + ".jpg");

        try {
            final File localFile = File.createTempFile("images", "jpg");
            final ImageView imageView =  (ImageView) findViewById(R.id.postPhoto);

            photoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    String filePath = localFile.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_add_box_black_24dp);
                }
            });
        } catch (IOException io) {

        }
    }

    // when FAB is clicked to add to favorites, shows toast
    public void fab_clicked(View view) {
        // if seller selects own post, FAB displays edit button
        // on edit button clicked, send data from post to create_new_post to populate view
        currentUser = (User) feedIntent.getSerializableExtra("user");
        if (selectedPost.getSellerEmail().equals(currentUser.getEmail())) {
            Intent editPostIntent = new Intent(PostViewerActivity.this, CreateNewPostActivity.class);
            editPostIntent.putExtra("current_post", selectedPost);
            startActivityForResult(editPostIntent, EDIT_POST_REQUEST);
        }

        // if not current user's post, FAB displays favorite button
        // on favorite button clicked, add post to favorites
        else {
            // can't use email as value in Firebase because contains '.'
            // for now, assuming full name is unique - when merged with dev, use user uid
//            String user_email = currentUser.getEmail();
            String user_name = currentUser.getFullName();

            mFavoritesDatabaseReference = mFirebaseDatabase.getReference().child("favorites").push();
            mFavoritesDatabaseReference.child(user_name).setValue(selectedPost.getUuid());

            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();

            //TODO: add code to change button and remove post from favorites if already in favorites
            // currently will continue adding it to favorites
        }
    }

    public void DeletePost(View view) {
        remove(selectedPost);
        finish();
    }
    public void DeleteOldPost(){
        remove(selectedPost);
        finish();
    }
    public void remove(Post post){
        String uuid = post.getUuid();
        DatabaseReference removing  = FirebaseDatabase.getInstance().getReference("posts").child(uuid);
        removing.removeValue();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();
        mPostDatabaseReference =
                mFirebaseDatabase.getReference().child("posts");

        // on edit result, delete old post and create new one
        if (requestCode == EDIT_POST_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Post resultPost = (Post) data.getSerializableExtra("new_post");
                resultPost.setSeller(currentUser.getFullName());
                resultPost.setSellerEmail(currentUser.getEmail());
                String uuid = resultPost.getUuid();
                DeleteOldPost();
                mPostDatabaseReference.child(uuid).setValue(resultPost);
            }
            // after post is created, return to FeedView to open new post viewer
            setResult(RESULT_OK);
            finish();
        }
    }

    //when mark as sold button is clicked, remove post from feed
    public void markSold(View view) {
        selectedPost.setIsSold(true);
//        Log.d("post sold", "markSold: marked as sold");

        //return selectedpost to feed view to be removed
        Intent returnIntent = new Intent();
        returnIntent.putExtra("sold_post", selectedPost);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}