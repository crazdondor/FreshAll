package com.freshall.freshall.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
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

    public ChildEventListener mPostChildEventListener;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mFavoritesDatabaseReference;
    public DatabaseReference mPostDatabaseReference;
    public final Query mFavoritesQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("favorites");
    public final Query mPostsQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("posts");

    ArrayList<Post> postsArrayList;
    PostsArrayAdapter postsArrayAdapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_feed);

        // get user's full name to check against in Firebase
        Intent profileIntent = getIntent();
        try {
            user = (User) profileIntent.getSerializableExtra("user");
            userName = user.getFullName();
        }
        catch (Exception e) {
            Toast.makeText(this, "No user available", Toast.LENGTH_SHORT).show();
            finish();
        }

        // get references to view
        ListView postsListView = (ListView) findViewById(R.id.postsList);

        // initialize posts array list
        postsArrayList = new ArrayList<>();

        // create array adapter to display title and description of posts
        postsArrayAdapter = new PostsArrayAdapter(this, postsArrayList);

        // set array adapter in list view
        postsListView.setAdapter(postsArrayAdapter);

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

        // get references to Firebase favorites database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFavoritesDatabaseReference = mFirebaseDatabase.getReference().child("favorites");
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("posts");

        //
        mFavoritesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                Log.d(TAG, "onDataChange: post = " + post.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // getting post failed, log a messageLog.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(FavoritesFeedActivity.this, "Failed to load post",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // define database listener for items added to favorites
        mPostChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the favorites entry
                HashMap<String, String> favorites_entry = (HashMap<String, String>) dataSnapshot.getValue();

                // if favorites post belongs to current user, get the post
                // add post to the list, notify adapter
                if (favorites_entry.keySet().contains(userName)){
                    // get reference to values for given key
                    Collection<String> values_collection = favorites_entry.values();

                    //values_collection should have one object (a post)
                    for (String value : values_collection) {
                        try {
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(value).child("description");

                            Log.d(TAG, "onChildAdded: " + FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child(value).child("description"));


//                            postsArrayList.add(post);
                            postsArrayAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){
                            Log.d(TAG, "onChildAdded: exception" + e);
                        }

                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // attach listener
        mFavoritesQuery.addChildEventListener(mPostChildEventListener);
    }

//    private Post objectToPost(Object object) {
//        Post new_post = new Post();
//        Log.d(TAG, "objectToPost: " + object.toString());
//        return new_post;
//    }

}