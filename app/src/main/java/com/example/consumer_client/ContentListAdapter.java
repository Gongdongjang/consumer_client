package com.example.consumer_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ContentListAdapter extends BaseAdapter {
    String TAG = ContentListAdapter.class.getSimpleName();

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> photo_url;

    public ContentListAdapter(Context context, ArrayList<String> photo_url) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.photo_url = photo_url;
    }

    @Override
    public int getCount() {
        return photo_url.size();
    }

    @Override
    public String getItem(int i) {
        return photo_url.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.content_list_layout, null);

        ImageView content_photo = v.findViewById(R.id.content_thumbnail);
        Picasso.get().load(getItem(i)).into(content_photo);

        return v;
    }
}
