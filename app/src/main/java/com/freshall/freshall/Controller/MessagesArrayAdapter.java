package com.freshall.freshall.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesArrayAdapter extends ArrayAdapter<ChatMessage> {

    private Context mContext;
    private ArrayList<ChatMessage> mMessageList;

    public MessagesArrayAdapter(Context context, ArrayList<ChatMessage> chatMessageList) {
        super(context,0, chatMessageList);
        mContext = context;
        mMessageList = chatMessageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ChatMessage chatMessage = getItem(position);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        Date date = new Date(chatMessage.getCreatedAt());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        // Lookup view for data population
        TextView messageContent, messageSender, messageTime;
        messageContent = (TextView) convertView.findViewById(R.id.message_content);
        messageSender = (TextView) convertView.findViewById(R.id.message_sender);
        messageTime = (TextView) convertView.findViewById(R.id.message_time);

        // Populate the data into the template view using the data object
        messageContent.setText(chatMessage.getContent());
        messageSender.setText(chatMessage.getFullName());
        messageTime.setText(dateFormat.format(date));

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}