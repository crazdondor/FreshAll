package com.freshall.freshall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "feed activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void letIn(View loginButton) {
        Log.d(TAG, "letIn: IN!");
        Intent startFeed = new Intent(this, FeedActivity.class);
        startActivity(startFeed);
    }
}
