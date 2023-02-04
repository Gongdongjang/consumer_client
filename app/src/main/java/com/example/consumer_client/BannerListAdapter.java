package com.example.consumer_client;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
    final Integer NUM_PAGES = 3;

    private Context context;
    private ArrayList<String> bannerThumbnails;
    private ArrayList<String> bannerIds;
    private ArrayList<String> bannerTitles;
    private ArrayList<String> bannerMainPhotos;
    private ArrayList<String> bannerPhotos;
    private ArrayList<String> bannerContexts;
    private ArrayList<String> bannerLinks;
    private ArrayList<String> bannerDates;

    public BannerListAdapter(Context context,
                             ArrayList<String> bannerThumbnails, ArrayList<String> bannerIds,
                             ArrayList<String> bannerTitles, ArrayList<String> bannerMainPhotos,
                             ArrayList<String> bannerPhotos, ArrayList<String> bannerContexts,
                             ArrayList<String> bannerLinks, ArrayList<String> bannerDates) {
        this.context = context;
        this.bannerThumbnails = bannerThumbnails;
        this.bannerIds = bannerIds;
        this.bannerTitles = bannerTitles;
        this.bannerMainPhotos = bannerMainPhotos;
        this.bannerPhotos = bannerPhotos;
        this.bannerContexts = bannerContexts;
        this.bannerLinks = bannerLinks;
        this.bannerDates = bannerDates;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bannerThumbnails.size() == 0) holder.bind("loading", "loading");
        else holder.bind(bannerThumbnails.get(position % NUM_PAGES), bannerTitles.get(position % NUM_PAGES));
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES * 1000;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView banner;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            banner = itemView.findViewById(R.id.content_banner);
            title = itemView.findViewById(R.id.content_banner_title);

            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition() % NUM_PAGES;
                    Intent intent = new Intent(context.getApplicationContext(), ContentClick.class);

                    intent.putExtra("content_id", bannerIds.get(position));
                    intent.putExtra("content_title", bannerTitles.get(position));
                    intent.putExtra("contentMainPhoto", bannerMainPhotos.get(position));
                    intent.putExtra("content_photo", bannerPhotos.get(position));
                    intent.putExtra("content_context", bannerContexts.get(position));
                    intent.putExtra("content_link", bannerLinks.get(position));
                    intent.putExtra("contentDate", bannerDates.get(position));

                    context.startActivity(intent);
                }
            });
        }

        public void bind(String thumbnail, String title) {
            this.title.setText(title);
            Picasso.get().load(thumbnail).into(banner);
        }
    }
}