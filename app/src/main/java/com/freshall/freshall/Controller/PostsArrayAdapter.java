package com.freshall.freshall.Controller;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.freshall.freshall.Model.Post;
import com.freshall.freshall.R;

import java.util.ArrayList;

/**
 * Created by bennettfalkenberg on 2/26/19.
 */

public class PostsArrayAdapter extends BaseAdapter implements Filterable{

    public Context context;
    public ArrayList<Post> postArrayList;
    public ArrayList<Post> original;

    public PostsArrayAdapter(Context context, ArrayList<Post> postArrayList) {
        super();
        this.context = context;
        this.postArrayList = postArrayList;
    }

    public class PostHolder
    {
        TextView title;
        TextView description;
        TextView seller;
        ImageView photo;
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults mReturn = new FilterResults();
                final ArrayList<Post> results = new ArrayList<Post>();
                if (original == null) {
                    original = postArrayList;
                }
                if (constraint != null) {
                    if (original != null && original.size() > 0) {
                        for (final Post post : original) {
                            if (post.getTitle().toLowerCase().contains(constraint.toString()) ||
                                    post.getSeller().getFullName().toLowerCase().contains(constraint.toString()) ||
                                    post.getDescription().toLowerCase().contains(constraint.toString())) {
                                results.add(post);
                            }
                        }
                    }
                    mReturn.values = results;
                }
                return mReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                postArrayList = (ArrayList<Post>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return postArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return postArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PostHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.feedview_listitem, parent, false);
            holder = new PostHolder();
            holder.title = (TextView) convertView.findViewById(R.id.post_title);
            holder.description = (TextView) convertView.findViewById(R.id.post_description);
            holder.seller = (TextView) convertView.findViewById(R.id.seller_name);
            holder.photo = (ImageView) convertView.findViewById(R.id.post_photo);

            convertView.setTag(holder);
        } else {
            holder = (PostHolder) convertView.getTag();
        }
        holder.title.setText(postArrayList.get(position).getTitle());
        holder.description.setText(postArrayList.get(position).getDescription());
        holder.seller.setText((CharSequence) postArrayList.get(position).getSeller().getFullName());

        holder.photo.setImageResource(R.drawable.baseline_person_black_24dp);

        return convertView;
    }

}