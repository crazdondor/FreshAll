package com.freshall.freshall.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freshall.freshall.Model.ChatMessage;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagesArrayAdapter extends RecyclerView.Adapter {
    private static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private ArrayList<ChatMessage> mMessageList;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

    public MessagesArrayAdapter(Context context, ArrayList<ChatMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) mMessageList.get(position);

        if (message.getSenderID().equals(mFirebaseUser.getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.sent_message_content);
            timeText = (TextView) itemView.findViewById(R.id.sent_message_time);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            // Format the stored timestamp into a readable String using format constant at top of file.
            timeText.setText(simpleTimeFormat.format(message.getCreatedAt()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.received_message_content);
            timeText = (TextView) itemView.findViewById(R.id.received_message_time);
            nameText = (TextView) itemView.findViewById(R.id.received_message_sender);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            // Format the stored timestamp using simpleTimeFormat defined above.
            timeText.setText(simpleTimeFormat.format(message.getCreatedAt()));
            nameText.setText(message.getFullName());
        }
    }
}