package com.freshall.freshall.Controller;

import android.content.Intent;
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

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;

import java.util.ArrayList;

public class FavoritesFeedActivity extends AppCompatActivity {
    private User user;
    public String userName;
    static final int VIEW_POST_REQUEST = 10;
    String TAG = "FavesFeed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_feed);

//        Intent profileIntent = getIntent();
//        user = (User) profileIntent.getSerializableExtra("user");
//
//        ListView postsListView = (ListView) findViewById(R.id.postsList);
//        TextView noPostsMessage = (TextView) findViewById(R.id.noPostsText);
//
//        ArrayList<Post> postsArrayList;
//        if (user != null) {
//            postsArrayList = user.getFavorites();
//        }
//        else {
//            postsArrayList = new ArrayList<>();
//        }
//
//        // create array adapter to display title and description of posts
//        PostsArrayAdapter arrayAdapter = new PostsArrayAdapter(this, postsArrayList);
//
//        // set array adapter
//        try {
//            postsListView.setAdapter(arrayAdapter);
//        }
//        catch (Exception e) {
//        }

//         set list item click listener to open post viewer activity
//        postsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Log.i("listview", "listview item clicked");
//                Intent viewPost = new Intent(getApplicationContext(), PostViewerActivity.class);
//
//                // add post that was clicked to intent, then start post viewer activity
//                Post selectedPost = (Post) adapterView.getAdapter().getItem(position);
//                viewPost.putExtra("selectedPost", selectedPost);
//                viewPost.putExtra("user", user);
//                startActivityForResult(viewPost, VIEW_POST_REQUEST);
//            }
//        });

    }

}
