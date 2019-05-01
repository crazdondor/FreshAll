/**
 * This program is the Controller for the feed screen.
 * After logging in, the user will see this activity containing:
 * - a list of items for sale with their photos and titles
 * - a search bar
 * - a create-new-post button
 * If a seller selects their own post, the user will see this activity containing:
 * - an edit post button
 * - a delete post button
 * - a mark-as-sold button
 *
 * @sources
 * How to download images from Firebase Storage: (lines 143-171)
 * {@link https://firebase.google.com/docs/storage/android/download-files}
 *
 * @authors Bennett Falkenberg, Angela Rae, Kevin Bruce, Amy Larson
 * @version v1.0 3/4/17
 */

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;
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

public class FeedActivity extends AppCompatActivity {

    static final int SIGN_IN_REQUEST = 1;
    static final int NEW_ITEM_REQUEST = 2;
    static final int EDIT_POST_REQUEST = 4;
    static final int VIEW_POST_REQUEST = 10;

    //member variables
    private User user;
    private FirebaseUser mUser;
    public String userName;
    private TextView noPostsMessage;
    private ListView postsListView;
    private ArrayList<Post> postsArrayList;
    private PostsArrayAdapter arrayAdapter;
    private SearchView searchModule;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;


    // database member variables
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;
    public ChildEventListener mPostChildEventListener;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    public final Query mPostsQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("posts").orderByChild("postDate");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // initialize the firebase references
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();

        mPostDatabaseReference =
                mFirebaseDatabase.getReference()
                        .child("posts");

        // Set up tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up nav bar
        setupNavBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set up drop down menu
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);


        // Set up post list
        noPostsMessage = (TextView) findViewById(R.id.noPostsText);
        postsListView = (ListView) findViewById(R.id.postsList);

        postsArrayList = new ArrayList<Post>();
        arrayAdapter = new PostsArrayAdapter(this, postsArrayList);
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
                viewPost.putExtra("user", mFirebaseAuth.getCurrentUser().getDisplayName());

                startActivityForResult(viewPost, VIEW_POST_REQUEST);
            }
        });

        mPostChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the Post
                Log.i("Added child", dataSnapshot.getValue(Post.class).getTitle());
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
//                    arrayAdapter.notifyDataSetChanged();
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

        mPostDatabaseReference.addChildEventListener(mPostChildEventListener);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    setupUserSignedIn(user);
                } else {
                    setupUserSignedOut();
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
                resultPost.setSeller(user.getFullName());
                String uuid = resultPost.getPostID();
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
        //setupUserSignedOut();
    }

    private void setupUserSignedIn(FirebaseUser user) {
        Log.i("auth", user.getDisplayName());
//        userName = user.getDisplayName(); // get the user's name
        this.user = new User(user.getDisplayName(), user.getUid(), user.getEmail(), user.getPhoneNumber());
        this.userName = this.user.getFullName();
        mPostsQuery
                .addChildEventListener(mPostChildEventListener);
    }

    private void setupUserSignedOut() {
        userName = "Anonymous";
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

    private void setupNavBar(){
        // create task bar to access messaging and profile activities
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Log.i("navigation", "home button press");
                        break;
                    case R.id.nav_message:
                        Log.i("navigation", "message button press");
                        Intent messagesIntent = new Intent(FeedActivity.this, ConversationListActivity.class);
                        startActivity(messagesIntent);
                        break;
                    case R.id.nav_user:
                        Log.i("navigation", "user button press");
                        Intent profileIntent = new Intent(FeedActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("userName", userName);
                        startActivity(profileIntent);
                        break;
                }
                return false;
            }
        };
    }

}

