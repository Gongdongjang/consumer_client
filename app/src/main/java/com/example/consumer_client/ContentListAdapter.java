package com.example.consumer_client;

import android.content.Context;
import android.content.Intent;
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
    ArrayList<String> content_thumbnail;
    ArrayList<Integer> content_id;
    ArrayList<String> content_title;
    ArrayList<String> content_date;
    ArrayList<String> content_photo;
    ArrayList<String> contentMainPhoto;
    ArrayList<String> content_context;
    ArrayList<String> content_link;

    public ContentListAdapter(Context context, ArrayList<String> content_thumbnail,
                              ArrayList<Integer> content_id, ArrayList<String> content_title,
                              ArrayList<String> content_date, ArrayList<String> content_context, ArrayList<String> contentMainPhoto,
                              ArrayList<String> content_photo, ArrayList<String> content_link) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.content_thumbnail = content_thumbnail;
        this.content_id = content_id;
        this.content_title = content_title;
        this.content_date = content_date;
        this.content_context = content_context;
        this.contentMainPhoto = contentMainPhoto;
        this.content_photo = content_photo;
        this.content_link = content_link;
    }

    @Override
    public int getCount() {
        return content_thumbnail.size();
    }

    @Override
    public String getItem(int i) {
        return content_thumbnail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.content_list_layout, null);

        ImageView content_thumbnail = v.findViewById(R.id.content_thumbnail);
        Picasso.get().load(getItem(i)).into(content_thumbnail);

        content_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ContentClick.class);

                intent.putExtra("content_id", content_id.get(i));
                intent.putExtra("content_title", content_title.get(i));
                intent.putExtra("content_photo", content_photo.get(i));
                intent.putExtra("contentMainPhoto", contentMainPhoto.get(i));
                intent.putExtra("content_context", content_context.get(i));
                intent.putExtra("contentDate", content_date.get(i));
                intent.putExtra("content_link", content_link.get(i));

                context.startActivity(intent);
            }
        });

        return v;
    }
}
