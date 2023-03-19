package com.example.consumer_client;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.review.ReviewListAdapter;
import com.example.consumer_client.review.ReviewListInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

public class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private ContentListAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ContentListAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView content_thumbnail;
        TextView content_list_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            content_thumbnail = (ImageView) itemView.findViewById(R.id.content_thumbnail);
            content_list_title = (TextView) itemView.findViewById(R.id.content_list_title);
        }
    }

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

    private ArrayList<ContentItem> mList = null;

    public ContentListAdapter(ArrayList<ContentItem> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.content_list_layout, parent, false);
        ContentListAdapter.ViewHolder vh = new ContentListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContentItem item = mList.get(position);

        Picasso.get().load(item.getContent_thumbnail()).into(holder.content_thumbnail);
        holder.content_list_title.setText(item.getContent_title());

        holder.content_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), ContentDetailActivity.class);
                intent.putExtra("content_id", item.getContent_id());
                intent.putExtra("content_title", item.getContent_title());
                intent.putExtra("content_photo", item.getContent_photo());
                intent.putExtra("contentMainPhoto", item.getContentMainPhotos());
                intent.putExtra("content_context", item.getContent_context());
                intent.putExtra("contentDate", item.getContent_date());
                intent.putExtra("content_link", item.getContent_link());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
