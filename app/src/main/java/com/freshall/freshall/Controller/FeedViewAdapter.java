package com.freshall.freshall.Controller;

import com.freshall.freshall.Model.Post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.firebase.ui.auth.AuthUI;
import com.freshall.freshall.Model.User;
import com.freshall.freshall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedViewAdapter extends ArrayAdapter<String> {

    public FeedViewAdapter(@NonNull Context context, Post[] posts, String[] strings) {
        super(context, R.layout.feedview_listitem, strings);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customRow = layoutInflater.inflate(R.layout.feedview_listitem, parent, false);

        String currentPost = getItem(position);
        TextView postTitle = (TextView) customRow.findViewById(R.id.post_title);
        TextView postSeller = (TextView) customRow.findViewById(R.id.seller_name);
        TextView postDescription = (TextView) customRow.findViewById(R.id.post_description);
        ImageView postPhoto = (ImageView) customRow.findViewById(R.id.post_photo);

        postTitle.setText(currentPost);
        postPhoto.setImageResource(R.drawable.baseline_person_black_24dp);
        return null;
    }
}
