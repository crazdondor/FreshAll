package com.freshall.freshall.Controller;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.freshall.freshall.R;
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

    // comment change

    static final String TAG = "MainActivity";
    static final int SIGN_IN_REQUEST = 1;


    String userName = "Anonymous";
    List<ChatMessage> chatMessageList;
    ArrayAdapter<ChatMessage> arrayAdapter;
    ListView listView;

    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    ChildEventListener mMessagesChildEventListener;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        mMessagesDatabaseReference =
                mFirebaseDatabase.getReference()
                        .child("messages");
        mMessagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // dataSnapshot stores the ChatMessage
                ChatMessage chatMessage =
                        dataSnapshot.getValue(ChatMessage.class);
                // add it to the list, notify adapter
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
        // 1. enable google authentication provider
        // 2. return the default values for db read/write
        // 3. declare a FirebaseAuth.AuthStateListener
        // 4. get username on sign in
        // 5. if the user is not signed in, start sign in activity with google
        // 6. wire up the AuthStateListener in onResume(), detach in onPause()
        // 7. add support for the user logging out

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
                    mMessagesDatabaseReference.removeEventListener(mMessagesChildEventListener);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "You are now signed in", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
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
        mMessagesDatabaseReference
                .addChildEventListener(mMessagesChildEventListener);
    }

    private void setupUserSignedOut() {
        userName = "Anonymous";
        chatMessageList.clear();
        arrayAdapter.notifyDataSetChanged();
        mMessagesDatabaseReference.removeEventListener(mMessagesChildEventListener);
    }


    public void onSendButtonClick(View view) {
        // push text up to "messages"
        EditText editText = (EditText)
                findViewById(R.id.editText);
        String currText = editText.getText().toString();
        if (currText.isEmpty()) {
            Toast.makeText(this, "Please enter a message first", Toast.LENGTH_SHORT).show();
        }
        else {
            ChatMessage chatMessage = new
                    ChatMessage(userName,
                    currText);
            mMessagesDatabaseReference
                    .push()
                    .setValue(chatMessage);
            editText.setText("");
        }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
