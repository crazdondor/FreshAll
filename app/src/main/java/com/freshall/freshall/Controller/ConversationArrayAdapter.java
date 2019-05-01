package com.freshall.freshall.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.Model.Conversation;
import com.freshall.freshall.Model.ConversationPreview;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConversationArrayAdapter extends ArrayAdapter<ConversationPreview> {

    private Context mContext;
    private ArrayList<ConversationPreview> mConversationPreviews;

    public ConversationArrayAdapter(Context context, ArrayList<ConversationPreview> conversationPreviews) {
        super(context,0, conversationPreviews);
        mContext = context;
        mConversationPreviews = conversationPreviews;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ConversationPreview conversationPreview = getItem(position);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm MM/dd/yyyy");
        Date date = new Date(conversationPreview.getLastMessageTimestamp());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
        }

        // Lookup view for data population
        TextView conversationContent, conversationSender, conversationTime;
        conversationContent = (TextView) convertView.findViewById(R.id.message_content);
        conversationSender = (TextView) convertView.findViewById(R.id.message_sender);
        conversationTime = (TextView) convertView.findViewById(R.id.message_time);

        // Populate the data into the template view using the data object
        conversationContent.setText(conversationPreview.getLastMessageText());
        conversationSender.setText(conversationPreview.getRecipientName());
        conversationTime.setText(dateFormat.format(date));


        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}