package com.example.consumer_client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
    final Integer NUM_PAGES = 3;

    private Context context;
    private ArrayList<String> contentThumbnails;

    public BannerListAdapter(Context context, ArrayList<String> contentThumbnails) {
        this.context = context;
        this.contentThumbnails = contentThumbnails;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (contentThumbnails.size() == 0) holder.bind("loading");
        else holder.bind(contentThumbnails.get(position % NUM_PAGES));
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES * 1000;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            banner = itemView.findViewById(R.id.content_banner);
        }

        public void bind(String thumbnail) {
            Picasso.get().load(thumbnail).into(banner);
        }
    }
}