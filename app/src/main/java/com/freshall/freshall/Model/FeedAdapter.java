package com.freshall.freshall.Model;

import android.content.Context;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.freshall.freshall.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private ArrayList<Post> postList = new ArrayList<>();

    //constructor
    public FeedAdapter(Context context, ArrayList<Post> postArrayList) {
        super(context, 0, postArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        View feedItem = convertView;

        if(feedItem == null) {
            Log.d("GETVIEW", "getView: in if statement");
            feedItem = LayoutInflater.from(mContext).inflate(R.layout.feed_array_adapter,
                    parent, false);
        }

        Post currentPost = postList.get(position);

        ImageView image = (ImageView)feedItem.findViewById(R.id.postPhoto);
        image.setImageResource(currentPost.getPostPhoto());

        TextView title = (TextView) feedItem.findViewById(R.id.textView_title);
        title.setText(currentPost.getTitle());

        TextView price = (TextView) feedItem.findViewById(R.id.textView_price);
        price.setText(currentPost.getPricePerQuantity());

        return feedItem;
    }
}
