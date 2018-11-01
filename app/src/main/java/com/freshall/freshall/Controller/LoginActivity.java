package com.freshall.freshall.Controller;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.freshall.freshall.R;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    static final int SIGN_IN_REQUEST = 1;

    String userName = "Anonymous";
    List<ChatMessage> chatMessageList;
    ArrayAdapter<ChatMessage> arrayAdapter;
    ListView listView;

    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    ChildEventListener mMessagesChildEventListener;

    // firebase authentication fields
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)
                findViewById(R.id.listView);
        chatMessageList = new ArrayList<>();
        chatMessageList.add(new ChatMessage());
        arrayAdapter = new ArrayAdapter<ChatMessage>(
                this,
                android.R.layout.simple_list_item_1,
                chatMessageList
        );
        listView.setAdapter(arrayAdapter);

        // initialize the firebase references
        mFirebaseDatabase =
                FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        mMessagesChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // called for each message already in/new message added to our db
                // dataSnapshot stores the ChatMessage
                ChatMessage chatMessage =
                        dataSnapshot.getValue(ChatMessage.class);
                // add it to our list and notify our adapter
                chatMessageList.add(chatMessage);
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


        // server side setup
        // 1. enable authentication providers like
        // email or google or facebook etc.
        // today we will do email and google
        // 2. return the default values for db
        // read and write to be authenticated
        // client side setup
        // 3. declare a FirebaseAuth.AuthStateListener
        // listens for authentication events
        // signed in and signed out are our two states
        // 4. if the user is signed in...
        // let's get their user name
        // wire up our childeventlistener mMessagesChildEventListener
        // 5. if the user is not signed in...
        // start an activity using FirebaseUI to
        // log our user in
        // 6. wire up the AuthStateListener in onResume()
        // and detach it onPause()
        // 7. add support for the user logging out
        // with an options menu action

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // two auth states: signed in and signed out
                // get the get current user, if there is one
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in:
                    setupUserSignedIn(user);
                } else {
                    // user is signed out:
                    // start an activity using FirebaseUI to log our user in
                    mMessagesDatabaseReference.removeEventListener(mMessagesChildEventListener);
                    Intent intent = AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                    )
                            ).build();
                    startActivityForResult(intent, SIGN_IN_REQUEST);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "You are now signed in", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user backed out of the sign in activity, so exit
                finish();
            }
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
        super.onPause();
        // remove the authstatelistener
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        setupUserSignedOut();
    }

    private void setupUserSignedIn(FirebaseUser user) {
        // get the user's name
        userName = user.getDisplayName();
        // listen for database changes with childeventlistener
        // wire it up!
        mMessagesDatabaseReference.addChildEventListener(mMessagesChildEventListener);
    }

    private void setupUserSignedOut() {
        userName = "Anonymous";
        chatMessageList.clear();
        arrayAdapter.notifyDataSetChanged();
        mMessagesDatabaseReference.removeEventListener(mMessagesChildEventListener);
    }
}