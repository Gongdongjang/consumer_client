package com.example.consumer_client.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;

public class StoreReviewAdapter extends RecyclerView.Adapter<StoreReviewAdapter.ViewHolder> {
    private ArrayList<String> mData = null;
    private ItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileImg;
        TextView userID;
        TextView prodName;
        TextView starCount;
        TextView review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImg = (ImageView) itemView.findViewById(R.id.UserProfileImg);
            userID = (TextView) itemView.findViewById(R.id.UserId);
            prodName = (TextView) itemView.findViewById(R.id.ReviewProdName);
            starCount = (TextView) itemView.findViewById(R.id.ReviewStarCount);
            review = (TextView) itemView.findViewById(R.id.ReviewMessage);
        }
    }

    private ArrayList<StoreReviewInfo> mList = null;

    public StoreReviewAdapter(ArrayList<StoreReviewInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.store_available_purchase_list, parent, false);
        StoreReviewAdapter.ViewHolder vh = new StoreReviewAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreReviewAdapter.ViewHolder holder, int position) {
        StoreReviewInfo item = mList.get(position);

        holder.userProfileImg.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.userID.setText(item.getUserID());
        holder.prodName.setText(item.getProdName());
        holder.starCount.setText(item.getStarCount());
        holder.review.setText(item.getReview());
    }

    public interface ItemClickListener{
        void onItemClick(StoreReviewInfo details,String pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
