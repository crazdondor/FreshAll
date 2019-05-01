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
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ConversationListActivity extends AppCompatActivity {

    static final int VIEW_CONVERSATION_REQUEST = 1;


    private List<Conversation> conversationList;
    private ArrayAdapter<Conversation> arrayAdapter;
    private ListView conversationsListView;

    // database member variables
    public FirebaseDatabase mFirebaseDatabase;
    public String mCurrentUserID;
    public DatabaseReference mConversationsDatabaseReference;
    public FirebaseAuth mFirebaseAuth;
    private ChildEventListener mConversationChildEventListener;
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
                .child("conversations");

        // Set up nav bar
        setupNavBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        conversationsListView = (ListView) findViewById(R.id.conversation_list);
        setupConversationList();

    }

    public void setupConversationList(){
//        conversationList = new ArrayList<>();
//        arrayAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                conversationList
//        );
//        conversationsListView.setAdapter(arrayAdapter);
//
//        conversationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Log.i("listview", "conversation item clicked");
//
//                Intent viewConversation = new Intent(getApplicationContext(), MessagingActivity.class);
//
//                // add post that was clicked to intent, then start post viewer activity
//                Conversation selectedConversation = (Conversation) adapterView.getAdapter().getItem(position);
//                viewConversation.putExtra("conversationID", selectedConversation.getConversationID());
//                Integer recipientIDindex = selectedConversation.getMemberIDs().indexOf(mCurrentUserID)+1%2;
//                Log.d("0 or 1: ", recipientIDindex.toString());
//                viewConversation.putExtra("recipientID", selectedConversation.getMemberIDs().get(recipientIDindex));
//                startActivityForResult(viewConversation, VIEW_CONVERSATION_REQUEST);
//            }
//        });
//
//        mConversationChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                // dataSnapshot stores the Post
//                Conversation conversation = dataSnapshot.getValue(Conversation.class);
//                Log.d("dataSnapshot ", dataSnapshot.toString());
//
//                conversationList.add(conversation);
//                Log.d("ConversationList:  ",conversationList.toString());
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mConversationsDatabaseReference.addChildEventListener(mConversationChildEventListener);
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
