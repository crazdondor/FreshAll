package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.Post;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

import com.freshall.freshall.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FeedViewAdapter extends ArrayAdapter<Post> {

    // constructor
    public FeedViewAdapter(Context context, int textViewResourceId, ArrayList<Post> posts) {
        super(context, textViewResourceId, posts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customRow = layoutInflater.inflate(R.layout.feedview_listitem, parent, false);

        Post currentPost = getItem(position);
        TextView postTitle = (TextView) customRow.findViewById(R.id.post_title);
        TextView postSeller = (TextView) customRow.findViewById(R.id.seller_name);
        TextView postDescription = (TextView) customRow.findViewById(R.id.post_description);
        ImageView postPhoto = (ImageView) customRow.findViewById(R.id.post_photo);

        postTitle.setText(currentPost.getTitle());
        postSeller.setText((CharSequence) currentPost.getSeller().toString());
        postDescription.setText(currentPost.getDescription());

//        Later: get reference to photo saved by uuid in Firebase storage
        //postPhoto.setImageResource(currentPost.get);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference photo = storageRef.child(currentPost.getUuid()+".jpg");

        postPhoto.setImageResource(R.drawable.baseline_person_black_24dp);

        return customRow;
    }
}
