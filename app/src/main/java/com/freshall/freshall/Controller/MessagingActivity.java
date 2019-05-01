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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.R;

//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagingActivity extends AppCompatActivity {

    private String conversationID;
    private String recipientID;
    private String mCurrentUserID;
    private ArrayList<ChatMessage> chatMessageList;
    private ListAdapter messagesArrayAdapter;
    private ListView messageListView;

    // firebase fields
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mConversationDatabaseReference;
    public DatabaseReference mMessagesDatabaseReference;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    public ValueEventListener mMessagesChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent messagingIntent = getIntent();
        conversationID = messagingIntent.getStringExtra("conversationID");
        recipientID = messagingIntent.getStringExtra("recipientID");


        // initialize the firebase references
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mCurrentUserID = mFirebaseUser.getUid();
        mConversationDatabaseReference = mFirebaseDatabase.getReference()
                .child("conversations").child(conversationID);
        mMessagesDatabaseReference = mConversationDatabaseReference.child("messages");

        // initialize List and Adapter
        chatMessageList = new ArrayList<>();
        messageListView = (ListView) findViewById(R.id.message_listview);
        messagesArrayAdapter = new MessagesArrayAdapter(this, chatMessageList);

        // populate chatMessageList
        mMessagesChildEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = messageSnapshot.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
                }
                messageListView.setAdapter(messagesArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mMessagesDatabaseReference.addValueEventListener(mMessagesChildEventListener);

    }

    public void onSendButtonClick(View view) {

        EditText editText = (EditText)findViewById(R.id.edittext_chatbox);
        String messageContent = editText.getText().toString();

        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Please enter a message first", Toast.LENGTH_SHORT).show();
        } else {
            // get timestamp
            long messageCreatedAt = System.currentTimeMillis();
            // create new message object
            ChatMessage chatMessage = new ChatMessage(mCurrentUserID,
                    mFirebaseUser.getDisplayName(),
                    messageContent,
                    messageCreatedAt);
            chatMessageList.add(chatMessage);
            mMessagesDatabaseReference.push().setValue(chatMessage);

            Log.d("MessagingActivity", "mConversationDatabaseReference: " + mConversationDatabaseReference.toString());

            editText.setText("");
            mConversationDatabaseReference.child("lastMessageTimestamp").setValue(messageCreatedAt);
            mConversationDatabaseReference.child("lastMessageText").setValue(messageContent);
        }
    }



}
