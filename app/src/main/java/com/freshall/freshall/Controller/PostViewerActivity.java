package com.freshall.freshall.Controller;

import com.freshall.freshall.R;
import com.freshall.freshall.Model.Post;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                case R.id.nav_home:
                    Log.i("navigation", "home button press");
//                    mTextMessage.setText(R.string.title_home);
                    Intent homeIntent = new Intent(PostViewerActivity.this, FeedActivity.class);
                    startActivity(homeIntent);
                    return true;
                case R.id.nav_message:
                    Log.i("navigation", "message button press");
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.nav_user:
                    Log.i("navigation", "user button press");
                    Intent profileIntent = new Intent(PostViewerActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
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

        // set price text
        TextView priceText = (TextView) findViewById(R.id.price);
        priceText.setText(selectedPost.getPricePerQuantity().toString());

        // set location text
        TextView locationText = (TextView) findViewById(R.id.locationText);
        locationText.setText(selectedPost.getLocation());

        // set quantity text
        String quantity = selectedPost.getQuantity() + " " + selectedPost.getQuantityType();
        TextView quantityText = (TextView) findViewById(R.id.quantity);
        quantityText.setText(quantity);

        // set user text
        TextView sellerText = (TextView) findViewById(R.id.sellerName);
        sellerText.setText(selectedPost.getSeller());
    }

    // when FAB is clicked to add to favorites, shows toast
    public void addToFavorites(View view) {
        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
    }
}