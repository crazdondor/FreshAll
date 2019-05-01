package com.freshall.freshall.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FavoritesFeedActivity extends AppCompatActivity {
    private User user;
    public String userName;
    static final int VIEW_POST_REQUEST = 10;
    String TAG = "FavesFeed";

    public ValueEventListener mPostValueEventListener;
    public FirebaseDatabase mFirebaseDatabase;
    public FirebaseAuth mFirebaseAuth;
    public DatabaseReference mFavoritesDatabaseReference;
    public DatabaseReference mPostDatabaseReference;

    ArrayList<Post> postsArrayList;
    ArrayList<String> favoriteIDList;
    PostsArrayAdapter postsArrayAdapter;
    ArrayAdapter<String> postTitleAdapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_feed);



        // get user's full name to check against in Firebase
        // eventually change to checking for a more unique value
        Intent profileIntent = getIntent();
        String mCurrentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        try {
            user = (User) profileIntent.getSerializableExtra("user");
            userName = user.getFullName();
        }
        catch (Exception e) {
            Log.d(TAG, "Exception:" + e);
            Toast.makeText(this, "No user available", Toast.LENGTH_SHORT).show();
            finish();
        }

        // get references to Firebase favorites database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFavoritesDatabaseReference = mFirebaseDatabase.getReference().child("users").
                child(mCurrentUserID).child("favoriteIDs");
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("posts");

        // get reference to ListView
        final ListView postsListView = (ListView) findViewById(R.id.postsList);

        // initialize posts array list
        postsArrayList = new ArrayList<>();
        favoriteIDList = new ArrayList<>();

        // create array adapter to display title and description of posts
        postsArrayAdapter = new PostsArrayAdapter(this, postsArrayList);
        postTitleAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        // populate Post list
        mPostValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot favoritesSnapshot : dataSnapshot.getChildren()) {
                    String favoriteID = favoritesSnapshot.getKey();
                    favoriteIDList.add(favoriteID);
                }
                // set array adapter
//                postsListView.setAdapter(postsArrayAdapter);
                postsListView.setAdapter(postTitleAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mFavoritesDatabaseReference.addValueEventListener(mPostValueEventListener);


        // set list item click listener to open post viewer activity
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent viewPost = new Intent(getApplicationContext(), PostViewerActivity.class);

                // add post that was clicked to intent, then start post viewer activity
                Post selectedPost = (Post) adapterView.getAdapter().getItem(position);
                viewPost.putExtra("selectedPost", selectedPost);
                viewPost.putExtra("user", user);
                startActivityForResult(viewPost, VIEW_POST_REQUEST);
            }
        });
    }
}