package com.freshall.freshall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PostViewerActivity extends AppCompatActivity {

    private TextView mTextMessage;

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
        setContentView(R.layout.activity_post_viewer);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // get post that was clicked to populate view
        Intent feedIntent = getIntent();
        Post selectedPost = (Post) feedIntent.getSerializableExtra("selectedPost");

        // set title text
        TextView titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(selectedPost.getTitle());

        // set description text
        TextView descText = (TextView) findViewById(R.id.description);
        descText.setText(selectedPost.getDescription());
    }

    // when FAB is clicked to add to favorites, shows toast
    public void addToFavorites(View view) {
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
    }
}
