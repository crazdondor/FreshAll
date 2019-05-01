package com.freshall.freshall.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.Model.Conversation;
import com.freshall.freshall.Model.ConversationPreview;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ConversationListActivity extends AppCompatActivity {

    private ArrayList<ConversationPreview> conversationPreviews;
    private ConversationArrayAdapter conversationArrayAdapter;
    private ListView conversationsListView;

    // database member variables
    public FirebaseDatabase mFirebaseDatabase;
    public String mCurrentUserID;
    public DatabaseReference mConversationsDatabaseReference;
    public FirebaseAuth mFirebaseAuth;
    private ValueEventListener mConversationsValueEventListener;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        // initialize the firebase references
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserID = mFirebaseAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mConversationsDatabaseReference = mFirebaseDatabase.getReference()
                .child("users")
                .child(mCurrentUserID)
                .child("conversationIDs");

        // Set up nav bar
        setupNavBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Set up conversation list
        conversationPreviews = new ArrayList<>();
        conversationsListView = (ListView) findViewById(R.id.conversation_list);
        conversationArrayAdapter = new ConversationArrayAdapter(this, conversationPreviews);
        conversationsListView.setAdapter(conversationArrayAdapter);

        mConversationsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot conversationSnapshot : dataSnapshot.getChildren()) {
                    ConversationPreview conversationPreview = conversationSnapshot.getValue(ConversationPreview.class);
                    conversationPreviews.add(conversationPreview);
                    conversationArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mConversationsDatabaseReference.addValueEventListener(mConversationsValueEventListener);

        // set conversation onClick listener to open messaging activity
        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("ConversationList", "listview item clicked");

                // add post that was clicked to intent, then start post viewer activity
                ConversationPreview preview = (ConversationPreview) adapterView.getAdapter().getItem(position);

                Intent messagingIntent = new Intent(getApplicationContext(), MessagingActivity.class);
                messagingIntent.putExtra("conversationID", preview.getConversationID());
                messagingIntent.putExtra("recipientID", preview.getRecipientID());
                messagingIntent.putExtra("recipientName", preview.getRecipientName());

                startActivity(messagingIntent);
            }
        });



    }

    private void setupNavBar(){
        // create task bar to access messaging and profile activities
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Log.i("navigation", "home button press");
                        Intent feedIntent = new Intent(ConversationListActivity.this, FeedActivity.class);
                        startActivity(feedIntent);
                        break;
                    case R.id.nav_message:
                        Log.i("navigation", "message button press");
                        break;
                    case R.id.nav_user:
                        Log.i("navigation", "user button press");
                        Intent profileIntent = new Intent(ConversationListActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("userName", mFirebaseAuth.getCurrentUser().getDisplayName());
                        startActivity(profileIntent);
                        break;
                }
                return false;
            }
        };
    }

}
