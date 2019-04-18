package com.freshall.freshall.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.Model.Conversation;
import com.freshall.freshall.R;

import java.util.ArrayList;
import java.util.List;


public class ConversationListActivity extends AppCompatActivity {

    static final int VIEW_CONVERSATION_REQUEST = 1;


    private List<Conversation> conversationList;
    private ArrayAdapter<Conversation> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);

        listView = (ListView) findViewById(R.id.conversation_list);
        conversationList = new ArrayList<>();
        conversationList.add(new Conversation());
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                conversationList
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.i("listview", "conversation item clicked");
                Intent viewConversation = new Intent(getApplicationContext(), MessagingActivity.class);

                // add post that was clicked to intent, then start post viewer activity
                Conversation selectedConversation = (Conversation) adapterView.getAdapter().getItem(position);
                viewConversation.putExtra("conversationID", selectedConversation.getConversationID());
                startActivityForResult(viewConversation, VIEW_CONVERSATION_REQUEST);
            }
        });

    }
}
