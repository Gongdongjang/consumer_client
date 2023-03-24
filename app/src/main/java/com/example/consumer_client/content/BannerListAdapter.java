package com.example.consumer_client.content;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.ViewHolder> {
    final Integer NUM_PAGES = 4;

    private Context context;
    private ArrayList<String> bannerThumbnails;
    private ArrayList<String> bannerIds;
    private ArrayList<String> bannerTitles;
    private ArrayList<String> bannerMainPhotos;
    private ArrayList<String> bannerPhotos;
    private ArrayList<String> bannerContexts;
    private ArrayList<String> bannerMdId1;
    private ArrayList<String> bannerMdId2;
    private String user_id, standard_address;

    public BannerListAdapter(Context context, String user_id, String standard_address,
                             ArrayList<String> bannerThumbnails, ArrayList<String> bannerIds,
                             ArrayList<String> bannerTitles, ArrayList<String> bannerMainPhotos,
                             ArrayList<String> bannerPhotos, ArrayList<String> bannerContexts, ArrayList<String> bannerMdId1, ArrayList<String> bannerMdId2) {
        this.context = context;
        this.user_id = user_id;
        this.standard_address = standard_address;
        this.bannerThumbnails = bannerThumbnails;
        this.bannerIds = bannerIds;
        this.bannerTitles = bannerTitles;
        this.bannerMainPhotos = bannerMainPhotos;
        this.bannerPhotos = bannerPhotos;
        this.bannerContexts = bannerContexts;
        this.bannerMdId1 = bannerMdId1;
        this.bannerMdId2 = bannerMdId2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bannerThumbnails.size() == 0) holder.bind("loading", "loading");
        else
            holder.bind(bannerThumbnails.get(position % NUM_PAGES), bannerTitles.get(position % NUM_PAGES));
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
                    Intent intent = new Intent(context.getApplicationContext(), ContentDetailActivity.class);

                    intent.putExtra("content_id", bannerIds.get(position));
                    intent.putExtra("content_title", bannerTitles.get(position));
                    intent.putExtra("contentMainPhoto", bannerMainPhotos.get(position));
                    intent.putExtra("content_photo", bannerPhotos.get(position));
                    intent.putExtra("content_context", bannerContexts.get(position));
                    intent.putExtra("content_md_id1", bannerMdId1.get(position));
                    intent.putExtra("content_md_id2", bannerMdId2.get(position));
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("standard_address", standard_address);

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