package com.freshall.freshall.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingActivity extends AppCompatActivity {

    private String conversationID;
    private ArrayList<ChatMessage> chatMessageList;
    private MessagesArrayAdapter messagesArrayAdapter;
    private RecyclerView messageRecycler;

    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mMessagesDatabaseReference;
    DatabaseReference mConversationDatabaseReference;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = mAuth.getCurrentUser();

    ChildEventListener mMessagesChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent messagingIntent = getIntent();
        conversationID = messagingIntent.getStringExtra("conversationID");

        // initialize the firebase references
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mConversationDatabaseReference = mFirebaseDatabase.getReference().child(conversationID).getParent();
        mMessagesDatabaseReference = mConversationDatabaseReference.child("chatMessages");

        chatMessageList = new ArrayList<>();
        chatMessageList.add(new ChatMessage());

        messageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        messagesArrayAdapter = new MessagesArrayAdapter(this, chatMessageList);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messagesArrayAdapter);

        mMessagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                // add it to our list and notify our adapter
                chatMessageList.add(chatMessage);
                messagesArrayAdapter.notifyDataSetChanged();

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


    }


    public void onSendButtonClick(View view) {
        EditText editText = (EditText)
                findViewById(R.id.edittext_chatbox);
        String messageContent = editText.getText().toString();
        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Please enter a message first", Toast.LENGTH_SHORT).show();
        } else {
            // get Timestamp object
            Timestamp messageCreatedAt = new Timestamp(System.currentTimeMillis());
            // create new message object
            ChatMessage chatMessage = new ChatMessage(firebaseUser.getUid(),
                    firebaseUser.getDisplayName(),
                    messageContent,
                    messageCreatedAt);
            mMessagesDatabaseReference.push().setValue(chatMessage);
            editText.setText("");
            // create a map from string to object for updating conversation fields in firebase
            Map<String, Object> conversationValues = new HashMap<String,Object>();
            conversationValues.put("lastMessageText", messageContent);
            conversationValues.put("lastMessageTimestamp", messageCreatedAt);
            mConversationDatabaseReference.updateChildren(conversationValues);
        }
    }

}
