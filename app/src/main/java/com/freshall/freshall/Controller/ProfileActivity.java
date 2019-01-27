package com.freshall.freshall.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ImageView;
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

    ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 0;

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

    public void captureImage(View camButton){

    }
}