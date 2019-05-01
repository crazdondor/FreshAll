package com.freshall.freshall.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Post> allPosts;
    private ArrayList<Post> userPosts;
    private ListView listView;
    private String username;
    private User user;
    public FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPostDatabaseReference;
    public ChildEventListener mProfileChildEventListener;

    public ListView profileListView;
    public ArrayList<Post> profileArrayList;
    public PostsArrayAdapter arrayAdapter;
    public final Query mPostsQuery = FirebaseDatabase.getInstance()
            .getReference()
            .child("posts").orderByChild("postDate");
    ImageView imageView;
    final String TAG = "Profile Activity";

    static final int REQUEST_IMAGE_CAPTURE = 0;

    Uri uriProfileImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
                    Intent homeIntent = new Intent(ProfileActivity.this, FeedActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
                    break;
                case R.id.nav_user:
                    Log.i("navigation", "user button press");
                    break;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Get user from intent that started activity
        Intent feedIntent = getIntent();
        username = (String) feedIntent.getStringExtra("username");
        user = (User) feedIntent.getSerializableExtra("user");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(username);

        Button favoritesButton = (Button) findViewById(R.id.favoritesButton);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFavorites(view);
            }
        });

        profileListView = (ListView) findViewById(R.id.profilePostsList);
        profileArrayList = new ArrayList<Post>();

        arrayAdapter = new PostsArrayAdapter(this,profileArrayList);
        profileListView.setAdapter(arrayAdapter);


        mProfileChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                Log.i("Added profile child", dataSnapshot.getValue(Post.class).getTitle());
                if(post.getSeller().equals(nameText.getText().toString())){
                    profileArrayList.add(post);
                    arrayAdapter.notifyDataSetChanged();
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

        mPostsQuery.addChildEventListener(mProfileChildEventListener);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        Button btnCam = (Button) findViewById(R.id.btnCam);


        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == Activity.RESULT_OK){
                ImageView imageView = (ImageView) findViewById(R.id.profpic);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onDestroy() {
        mPostsQuery.removeEventListener(mProfileChildEventListener);
        super.onDestroy();
    }

    /**
     * When user clicks "My Favorites" button on profile, starts favorites feed activity
     * @param view
     */
    public void goToFavorites(View view) {
        Intent favoritesIntent = new Intent(this, FavoritesFeedActivity.class);
        favoritesIntent.putExtra("user", user);
        Log.d(TAG, "goToFavorites: user = " + user);
        startActivity(favoritesIntent);
    }
}