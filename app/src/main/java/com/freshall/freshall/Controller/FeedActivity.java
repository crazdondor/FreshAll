package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.Post;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.auth.AuthUI;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    static final int SIGN_IN_REQUEST = 1;
    static final int NEW_ITEM_REQUEST = 2;
    static final int EDIT_POST_REQUEST = 4;
    static final int VIEW_POST_REQUEST = 10;

    private User user;
    public String userName;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;
    public ChildEventListener mPostChildEventListener;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    public final Query mPostsQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("posts").orderByChild("postDate");

    public TextView noPostsMessage;
    public ListView postsListView;
    public ArrayList<Post> postsArrayList;
    public PostsArrayAdapter arrayAdapter;
    public SearchView searchModule;

    // server side setup
    // 1. enable google authentication provider
    // 2. return the default values for db read/write
    // 3. declare a FirebaseAuth.AuthStateListener
    // 4. get username on sign in
    // 5. if the user is not signed in, start sign in activity with google
    // 6. wire up the AuthStateListener in onResume(), detach in onPause()
    // 7. add support for the user logging out


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
//                    mTextMessage.setText(R.string.title_home);
                    break;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
//                    mTextMessage.setText(R.string.title_notifications);
                    break;
                case R.id.nav_user:
                    Log.i("navigation", "user button press");
                    Intent profileIntent = new Intent(FeedActivity.this, ProfileActivity.class);
                    profileIntent.putExtra("username", userName);
//                    profileIntent.putExtra("user", user);
                    startActivity(profileIntent);
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        postsListView = (ListView) findViewById(R.id.postsList);
        noPostsMessage = (TextView) findViewById(R.id.noPostsText);

        postsArrayList = new ArrayList<Post>();

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

        // initialize the firebase references
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();

        mPostDatabaseReference =
                mFirebaseDatabase.getReference()
                        .child("posts");

        mPostChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the Post
                Post post = dataSnapshot.getValue(Post.class);
                // add it to the list, notify adapter
                postsArrayList.add(post);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);

                // if post changed to sold, remove from posts array list
                if (post.getIsSold()) {
//                    postsArrayList.remove(postsArrayList.get());
                    arrayAdapter.notifyDataSetChanged();
                }
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


        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    setupUserSignedIn(user);
                } else {
                    // user is signed out
                    mPostsQuery.removeEventListener(mPostChildEventListener);
                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()
                                    )
                            ).build();
                    startActivityForResult(intent, SIGN_IN_REQUEST);
                }
            }
        };

        searchModule = (SearchView) findViewById(R.id.searchBar);
        searchModule.setIconifiedByDefault(false);
        searchModule.setSubmitButtonEnabled(true);
        searchModule.setFocusable(false);
        searchModule.setQueryHint("Search");
        searchModule.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    // when user clicks FAB to add new post, start intent for CreateNewPostActivity
    public void addNewPost(View addButton) {
        Intent goToNewPost = new Intent(this, CreateNewPostActivity.class);
        startActivityForResult(goToNewPost, NEW_ITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //when user signs in, show toast message
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "You are now signed in", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }

        // when CreateNewPostActivity finishes, get the new post and add to feed list
        if (requestCode == NEW_ITEM_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Post resultPost = (Post) data.getSerializableExtra("new_post");
                resultPost.setSeller(user);
                String uuid = resultPost.getUuid();
                mPostDatabaseReference.child(uuid).setValue(resultPost); // add post to firebase
            }
        }

        // when PostViewer finishes from view, if post sold, remove from feed
        if (requestCode == VIEW_POST_REQUEST && resultCode == Activity.RESULT_OK) {

            Post sold_post = (Post) data.getSerializableExtra("sold_post");
            Log.d("view ended", "onActivityResult: " + sold_post.getIsSold());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // attach the authstatelistener
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause(); // remove the authstatelistener
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        setupUserSignedOut();
    }

    private void setupUserSignedIn(FirebaseUser user) {
//        userName = user.getDisplayName(); // get the user's name
        this.user = new User(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
        this.userName = this.user.getFullName();
        mPostsQuery.addChildEventListener(mPostChildEventListener);
    }

    private void setupUserSignedOut() {
        postsArrayList.clear();
        arrayAdapter.notifyDataSetChanged();
        mPostsQuery.removeEventListener(mPostChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_signout) {
            AuthUI.getInstance().signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
