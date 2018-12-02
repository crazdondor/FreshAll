package com.freshall.freshall.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<Post> allPosts;
    private ArrayList<Post> userPosts;
    private ArrayAdapter<Post> arrayAdapter;
    private ListView listView;
    private String username;

    private DatabaseReference mPostDatabaseReference;
    public ChildEventListener mPostChildEventListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
//                    mTextMessage.setText(R.string.title_home);
                    Intent homeIntent = new Intent(ProfileActivity.this, FeedActivity.class);
                    startActivity(homeIntent);
                    break;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
//                    mTextMessage.setText(R.string.title_notifications);
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

        Intent feedIntent = getIntent();
        username = (String) feedIntent.getStringExtra("username");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView nameText = (TextView) findViewById(R.id.nameText);
        nameText.setText(username);

        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        mPostDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");

        userPosts = new ArrayList<>();

        listView = (ListView) findViewById(R.id.userPosts);

        arrayAdapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_2, android.R.id.text1, userPosts){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(userPosts.get(position).getTitle());
                text2.setText(userPosts.get(position).getDescription());
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        mPostDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot stores the Post
                Post post =
                        dataSnapshot.getValue(Post.class);
                // add it to the list, notify adapter
                userPosts.add(post);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPostChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the Post
                Post post =
                        dataSnapshot.getValue(Post.class);
                // add it to the list, notify adapter
                userPosts.add(post);
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

        mPostDatabaseReference.addChildEventListener(mPostChildEventListener);
    }
}
