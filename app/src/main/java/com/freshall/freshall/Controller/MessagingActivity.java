package com.freshall.freshall.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.Model.ConversationPreview;
import com.freshall.freshall.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagingActivity extends AppCompatActivity {

    private String conversationID;
    private String recipientID;
    private String recipientName;
    private String mCurrentUserID;
    private ArrayList<ChatMessage> chatMessageList = new ArrayList<>();
    private MessagesArrayAdapter messagesArrayAdapter;
    private ListView messageListView;

    // firebase fields
    public FirebaseDatabase mFirebaseDatabase;
    public DatabaseReference mConversationStorageReference;
    public DatabaseReference mMessagesDatabaseReference;
    public DatabaseReference mSenderReference;
    public DatabaseReference mRecipientReference;
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    public ValueEventListener mMessagesValueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent messagingIntent = getIntent();
        conversationID = messagingIntent.getStringExtra("conversationID");
        recipientID = messagingIntent.getStringExtra("recipientID");
        recipientName = messagingIntent.getStringExtra("recipientName");

        // initialize the firebase references
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mCurrentUserID = mFirebaseUser.getUid();
        mSenderReference = mFirebaseDatabase.getReference()
                .child("users").child(mCurrentUserID)
                .child("conversationIDs").child(conversationID);
        mRecipientReference = mFirebaseDatabase.getReference()
                .child("users").child(recipientID)
                .child("conversationIDs").child(conversationID);
        mConversationStorageReference = mFirebaseDatabase.getReference()
                .child("conversations").child(conversationID);
        mMessagesDatabaseReference = mConversationStorageReference.child("messages");

        // initialize List and Adapter
        messageListView = (ListView) findViewById(R.id.message_listview);
        messagesArrayAdapter = new MessagesArrayAdapter(this, chatMessageList);
        messageListView.setAdapter(messagesArrayAdapter);

        // populate chatMessageList
        mMessagesValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessageList.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = messageSnapshot.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
                    messagesArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mMessagesDatabaseReference.addValueEventListener(mMessagesValueEventListener);

    }

    public void onSendButtonClick(View view) {

        EditText editText = (EditText)findViewById(R.id.edittext_chatbox);
        String messageContent = editText.getText().toString();

        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Please enter a message first", Toast.LENGTH_SHORT).show();
        } else {
            // get timestamp
            long messageCreatedAt = System.currentTimeMillis();
            ChatMessage chatMessage = new ChatMessage(mCurrentUserID,
                    mFirebaseUser.getDisplayName(),
                    messageContent,
                    messageCreatedAt);

            Log.d("MessagingActivity", "database ref: " + mConversationStorageReference.toString());
            editText.setText("");

            ConversationPreview senderPreview = new ConversationPreview(messageContent,
                    messageCreatedAt, recipientName, recipientID, conversationID);
            ConversationPreview recipientPreview = new ConversationPreview(messageContent,
                    messageCreatedAt, mFirebaseUser.getDisplayName(), mCurrentUserID, conversationID);


            mMessagesDatabaseReference.push().setValue(chatMessage);
            mSenderReference.setValue(senderPreview);
            mRecipientReference.setValue(recipientPreview);
            mConversationStorageReference.child("lastMessageTimestamp").setValue(messageCreatedAt);
            mConversationStorageReference.child("lastMessageText").setValue(messageContent);
        }
    }
}