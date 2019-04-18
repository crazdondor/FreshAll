/**
 * This program is the Controller for the post view screen.
 * After selecting a seller's post on the feed screen, the user will see this activity containing:
 * - post picture and post details
 * - contact information of the seller
 * - an add-to-favorites button
 * If a seller selects their own post, the user will see this activity containing:
 * - an edit post button
 * - a delete post button
 * - a mark-as-sold button
 *
 * @sources
 * How to download images from Firebase Storage: (lines 143-171)
 * {@link https://firebase.google.com/docs/storage/android/download-files}
 *
 * @authors Kevin Bruce, Quinlan Bingham
 * @version v1.0 3/4/17
 */

package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Objects;

public class PostViewerActivity extends AppCompatActivity {

    private int EDIT_POST_REQUEST = 4;
    private Intent mFeedIntent;
    private String mPostID;
    private String mCurrentUserID;
    private Post selectedPost;
    private User currentUser;

    /* UI member variables */

    private TextView titleText;
    private TextView descText;
    private TextView priceText;
    private TextView locationText;
    private TextView quantityText;
    private TextView sellerText;
    private FloatingActionButton fab;
    private Button markSoldButton;
    private Button deleteButton;
    private ImageView imageView;

    /* Firebase member variables */
    public DatabaseReference mPostReference;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;
    public StorageReference mStorageReference;
    public StorageReference mPhotoReference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
                    Intent homeIntent = new Intent(PostViewerActivity.this, FeedActivity.class);
                    startActivity(homeIntent);
                    return true;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
                    Intent messagingIntent = new Intent(PostViewerActivity.this, MessagingActivity.class);
                    startActivity(messagingIntent);
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // get post that was clicked to populate view
        mFeedIntent = getIntent();
        mPostID = (String) mFeedIntent.getSerializableExtra("mPostID");
        mCurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mPostReference = FirebaseDatabase.getInstance().getReference().child("posts").child(mPostID);
        mStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://freshall-5c50e.appspot.com");

        // the next two lines might not be necessary
        selectedPost = (Post) mFeedIntent.getSerializableExtra("selectedPost");
        currentUser = (User) mFeedIntent.getSerializableExtra("user");


        titleText = (TextView) findViewById(R.id.titleText);
        descText = (TextView) findViewById(R.id.description);
        priceText = (TextView) findViewById(R.id.price);
        locationText = (TextView) findViewById(R.id.locationText);
        quantityText = (TextView) findViewById(R.id.quantity);
        sellerText = (TextView) findViewById(R.id.sellerName);
        imageView = (ImageView) findViewById(R.id.postPhoto);
        fab = (FloatingActionButton) findViewById(R.id.favorite);
        markSoldButton = (Button) findViewById(R.id.postSold);
        deleteButton = (Button) findViewById(R.id.deletePost);


        // if user clicked own post, FAB is edit button and mark sold button visible
        // TODO: fix bug where app crashes on cancel edit post

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                titleText.setText(post.getTitle());
                descText.setText("Description: " + post.getDescription());
                priceText.setText("Price: " + post.getPricePerQuantity().toString());
                locationText.setText("Location: " + post.getLocation());
                quantityText.setText("Quantity: " + post.getQuantity() + " " + selectedPost.getQuantityType());
                sellerText.setText(post.getSeller().getFullName());

                mPhotoReference = mStorageReference.child("images/posts/" + post.getPostID() + ".jpg");
                setImageView(mPhotoReference);

                if(post.getSellerID() == mCurrentUserID){
                    fab.setImageResource(R.drawable.ic_edit);
                    markSoldButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("PostViewerActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);




        // TODO: fix bug where image must be selected on edit

    }

    public void setImageView(StorageReference photoReference){
        try {
            final File localFile = File.createTempFile("images", "jpg");

            photoReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    String filePath = localFile.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle errors
                    imageView.setImageResource(R.drawable.baseline_add_box_black_24dp);
                }
            });
        }
        catch (IOException ioe){
            Toast.makeText(getApplicationContext(), "File creation failed", Toast.LENGTH_SHORT).show();
        }
    }


    // when FAB is clicked to add to favorites, shows toast
    public void fab_clicked(View view) {
        // if seller selects own post, FAB displays edit button
        // on edit button clicked, send data from post to create_new_post to populate view
        currentUser = (User) mFeedIntent.getSerializableExtra("user");
        if (selectedPost.getSeller().getEmail().equals(currentUser.getEmail())) {
            Intent editPostIntent = new Intent(PostViewerActivity.this, CreateNewPostActivity.class);
            editPostIntent.putExtra("current_post", selectedPost);
            startActivityForResult(editPostIntent, EDIT_POST_REQUEST);
        }

        // if not current user's post, FAB displays favorite button
        // on favorite button clicked, add post to favorites
        // TODO: create favorites list and add selectedPost to list
        else {
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessageClicked(View view) {
        // if seller selects own post, FAB displays edit button
        // on edit button clicked, send data from post to create_new_post to populate view
        currentUser = (User) mFeedIntent.getSerializableExtra("user");
        if (selectedPost.getSeller().getEmail().equals(currentUser.getEmail())) {
            Intent editPostIntent = new Intent(PostViewerActivity.this, CreateNewPostActivity.class);
            editPostIntent.putExtra("current_post", selectedPost);
            startActivityForResult(editPostIntent, EDIT_POST_REQUEST);
        }

        else {
            Toast.makeText(this, "MessagesActivity opened", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeletePost(Post post) {
        remove(post);
        finish();
    }

    public void remove(Post post){
        String uuid = post.getPostID();
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
                resultPost.setSeller(currentUser);
                String uuid = resultPost.getPostID();
                DeletePost(resultPost);
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

        //return selectedpost to feed view to be removed
        Intent returnIntent = new Intent();
        returnIntent.putExtra("sold_post", selectedPost);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}