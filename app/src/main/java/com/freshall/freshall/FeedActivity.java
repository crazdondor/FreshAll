package com.freshall.freshall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private TextView mTextMessage;
    public TextView noPostsMessage;
    public ListView postsListView;
    public List<Post> postsArrayList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        postsListView = (ListView) findViewById(R.id.postsList);
        noPostsMessage = (TextView) findViewById(R.id.noPostsText);
        // tried to add message if no posts were available, but not working
//        if (postsListView.getAdapter().getCount() == 0) {
////            postsListView.setVisibility(View.GONE); // won't work bc reference to empty object
////            noPostsMessage.setVisibility(View.VISIBLE);
//            Log.d("onCreate", "if statement works");
//        }
        postsArrayList = new ArrayList<Post>();
    }

    public void addNewPost(View addButton) {

        Intent goToNewPost = new Intent(this, CreateNewPost.class);
        startActivity(goToNewPost);
        // currently adds same text for each item, but need to join with Kevin's New Post
        final String postTitle = "Food Item";
        Post newPost = new Post(postTitle, "This is a new food post.", "seller");

        // create new post & add to array
        postsArrayList.add(newPost);

        // create array adapter to display title and description of posts
        // will need to make a custom array adapter probably to display photo + post descriptions
        ArrayAdapter<Post> arrayAdapter = new ArrayAdapter<Post>(this, android.R.layout.simple_list_item_2, android.R.id.text1, postsArrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(postsArrayList.get(position).getTitle());
                text2.setText(postsArrayList.get(position).getDescription());
                return view;
            }
        };
        // set array adapter
        postsListView.setAdapter(arrayAdapter);

        // set list item click listener to open post viewer activity
        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("list item", "onItemClick: clicked!");
                Intent viewPost = new Intent(getApplicationContext(), PostViewerActivity.class);
                startActivity(viewPost);
            }
        });
    }

    private static final String TAG = "feed activity";

}
