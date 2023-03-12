package com.example.consumer_client.review;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;

import java.util.ArrayList;
import java.util.Objects;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private ReviewListAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(ReviewListAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ProdImg, Star_1, Star_2, Star_3, Star_4, Star_5;
        TextView storeName, mdName, starScore, ReviewContent;

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

            ProdImg = (ImageView) itemView.findViewById(R.id.ProdImg);
            Star_1 = (ImageView) itemView.findViewById(R.id.Star_1);
            Star_2 = (ImageView) itemView.findViewById(R.id.Star_2);
            Star_3 = (ImageView) itemView.findViewById(R.id.Star_3);
            Star_4 = (ImageView) itemView.findViewById(R.id.Star_4);
            Star_5 = (ImageView) itemView.findViewById(R.id.Star_5);
            mdName = (TextView) itemView.findViewById(R.id.MdName);
            storeName = itemView.findViewById(R.id.StoreName);
            starScore = itemView.findViewById(R.id.starScore);
            ReviewContent = itemView.findViewById(R.id.ReviewContent);
        }
    }

    private ArrayList<ReviewListInfo> mList = null;

    public ReviewListAdapter(ArrayList<ReviewListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.review_list, parent, false);
        ReviewListAdapter.ViewHolder vh = new ReviewListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
        ReviewListInfo item = mList.get(position);
        Glide.with(holder.itemView).load(item.getReviewImg1()).into(holder.ProdImg);
        holder.storeName.setText(item.getStoreName());
        holder.mdName.setText(item.getMdName());
        holder.ReviewContent.setText(item.getReviewContent());
        holder.starScore.setText(item.getRvw_rating());

        if (Objects.equals(item.getRvw_rating(), "1")){
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_1);
        }
        else if (Objects.equals(item.getRvw_rating(), "2")){
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_1);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_2);
        }
        else if (Objects.equals(item.getRvw_rating(), "3")){
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_1);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_2);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_3);
        }
        else if (Objects.equals(item.getRvw_rating(), "4")){
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_1);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_2);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_3);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_4);
        }
        else if (Objects.equals(item.getRvw_rating(), "5")){
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_1);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_2);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_3);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_4);
            Glide.with(holder.itemView).load(R.drawable.ic_product_review_list_on_14px).into(holder.Star_5);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
