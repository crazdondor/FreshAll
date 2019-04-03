package com.freshall.freshall.Controller;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FavoritesFeedActivity extends AppCompatActivity {
    private User user;
    public String userName;
    static final int VIEW_POST_REQUEST = 10;
    String TAG = "FavesFeed";

    public ChildEventListener mPostChildEventListener;
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mFavoritesDatabaseReference;
    public DatabaseReference mPostDatabaseReference;

    ArrayList<Post> postsArrayList;
    PostsArrayAdapter arrayAdapter;

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
        }

        // get references to views
        ListView postsListView = (ListView) findViewById(R.id.postsList);
        TextView noPostsMessage = (TextView) findViewById(R.id.noPostsText);

        // initialize posts array list
        postsArrayList = new ArrayList<>();

        // create array adapter to display title and description of posts
        arrayAdapter = new PostsArrayAdapter(this, postsArrayList);

        // set array adapter
        postsListView.setAdapter(arrayAdapter);


        // set list item click listener to open post viewer activity
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("listview", "listview item clicked");
                Intent viewPost = new Intent(getApplicationContext(), PostViewerActivity.class);

                // add post that was clicked to intent, then start post viewer activity
                Post selectedPost = (Post) adapterView.getAdapter().getItem(position);
                viewPost.putExtra("selectedPost", selectedPost);
                viewPost.putExtra("user", user);
                startActivityForResult(viewPost, VIEW_POST_REQUEST);
            }
        });

        // get references to database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFavoritesDatabaseReference = mFirebaseDatabase.getReference().child("favorites");
        mPostDatabaseReference = mFirebaseDatabase.getReference().child("posts");

        // define database listener for items added to favorites
        mPostChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the Post
                HashMap<String, Object> favorites_entry = (HashMap<String, Object>) dataSnapshot.getValue();
//                Log.d(TAG, "onChildAdded: " + userName);

                // if favorites post belongs to current user, get the post
                // add post to the list, notify adapter
                if (favorites_entry.keySet().contains(userName)){
                    Collection values = favorites_entry.values();

                    for (Object value : values) {
                        String value_string = value.toString();

                        Query query = mFirebaseDatabase.getReference().child("posts").orderByKey().equalTo(value_string);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Log.d(TAG, "onChildAdded: " + dataSnapshot.getChildren());
//                                Post post = (Post) dataSnapshot;
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
                        });

                        Log.d(TAG, "onChildAdded: " + value_string);
                    }

                }
//                else {
//
//                }
//                postsArrayList.add(post);
                arrayAdapter.notifyDataSetChanged();
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
        mFavoritesDatabaseReference.addChildEventListener(mPostChildEventListener);
    }

}
