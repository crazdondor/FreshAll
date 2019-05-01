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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FeedActivity extends AppCompatActivity {

    static final int SIGN_IN_REQUEST = 1;
    static final int NEW_ITEM_REQUEST = 2;
    static final int EDIT_POST_REQUEST = 4;
    static final int VIEW_POST_REQUEST = 10;

    private User user;
    private FirebaseUser mUser;
    public String userName;

    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mPostDatabaseReference;
    public ChildEventListener mPostChildEventListener;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseAuth.AuthStateListener mAuthStateListener;
    public final Query mPostsQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("posts").orderByChild("postDate");

    private ConnectivityManager connectivityManager;

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
                    Intent messageIntent = new Intent(FeedActivity.this, ConversationListActivity.class);
                    startActivity(messageIntent);
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
        Log.i("ONCREATE", "ONCREATE STARTING");
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

        loadData();

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
                viewPost.putExtra("user", user.getFullName());
                startActivityForResult(viewPost, VIEW_POST_REQUEST);
            }
        });

        // initialize the firebase references
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();

        mPostDatabaseReference =
                mFirebaseDatabase.getReference()
                        .child("posts");

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            // online mode, attach listeners
            mPostChildEventListener = setChildEventListener();

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
        } else {
            // offline mode, restore feed from savedInstanceState

        }

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

    protected ChildEventListener setChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("Added child", dataSnapshot.getValue(Post.class).getTitle());
                // dataSnapshot stores the Post
                Post post = dataSnapshot.getValue(Post.class);
                // add it to the list, notify adapter
                postsArrayList.add(post);
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
                resultPost.setSellerEmail(user.getEmail());
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
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        } else {
            Log.i("DataSaving", "loading data");
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("postList", null);
            Type type = new TypeToken<ArrayList<Post>>() {}.getType();
            postsArrayList = gson.fromJson(json, type);

            if (postsArrayList.isEmpty()) {
                Log.i("DataSaving", "empty list");
            }

            if (postsArrayList == null) {
                Log.i("DataSaving", "null list");
                postsArrayList = new ArrayList<Post>();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause(); // remove the authstatelistener
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
            setupUserSignedOut();
        }
        saveData();

    }

    private void saveData() {
        Log.i("DataSaving", "saving data");
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(postsArrayList);
        editor.putString("postList", json);
        editor.apply();
        Log.i("DataSaving", "jsonData = " + json);
    }

    private void loadData() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            postsArrayList = new ArrayList<>();
        } else {
            Log.i("DataSaving", "loading data");
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("post list", null);
            Type type = new TypeToken<ArrayList<Post>>() {}.getType();
            postsArrayList = gson.fromJson(json, type);

            if (postsArrayList == null) {
                Log.i("DataSaving", "null list");
                postsArrayList = new ArrayList<Post>();
            }
        }
    }

    private void setupUserSignedIn(FirebaseUser user) {
        Log.i("auth", user.getDisplayName());
//        userName = user.getDisplayName(); // get the user's name
        this.user = new User(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
        this.userName = this.user.getFullName();
        mPostsQuery
                .addChildEventListener(mPostChildEventListener);
    }

    private void setupUserSignedOut() {
        userName = "Anonymous";
        //postsArrayList.clear();
        //arrayAdapter.notifyDataSetChanged();
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
        } else if (id == R.id.action_deleteaccount) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(FeedActivity.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Deleting your account will completely remove your ability to use the app. " +
                    "Are you sure you want to delete your account?");
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(FeedActivity.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FeedActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
}
