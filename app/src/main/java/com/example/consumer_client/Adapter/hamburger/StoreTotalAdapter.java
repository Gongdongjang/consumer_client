package com.example.consumer_client.Adapter.hamburger;

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

public class StoreTotalAdapter extends RecyclerView.Adapter<StoreTotalAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private StoreTotalAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(StoreTotalAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storeProdImgView;
        TextView storeName;
        TextView storeLocationFromMe;
        TextView storeInfo;
        TextView storeRestDays;
        TextView storeHours;
        TextView storeSituation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
            storeProdImgView = (ImageView) itemView.findViewById(R.id.StoreProdImg);
            storeName = (TextView) itemView.findViewById(R.id.StoreName);
            storeLocationFromMe = (TextView) itemView.findViewById(R.id.StoreLocationFromMe);
            storeInfo = (TextView) itemView.findViewById(R.id.StoreInfo);
            storeRestDays = (TextView) itemView.findViewById(R.id.StoreRestDays);
            storeHours = (TextView) itemView.findViewById(R.id.StoreHours);
            storeSituation = (TextView) itemView.findViewById(R.id.StoreJointPurchaseCount);

        }
    }

    private ArrayList<StoreTotalInfo> mList = null;

    public StoreTotalAdapter(ArrayList<StoreTotalInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.store_total_list, parent, false);
        StoreTotalAdapter.ViewHolder vh = new StoreTotalAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreTotalAdapter.ViewHolder holder, int position) {
        StoreTotalInfo item = mList.get(position);

        holder.storeProdImgView.setImageResource(R.drawable.ic_launcher_background);
        holder.storeName.setText(item.getStoreName());
        holder.storeLocationFromMe.setText(item.getStoreLocationFromMe());
        holder.storeInfo.setText(item.getStoreInfo());
        holder.storeRestDays.setText(item.getStoreRestDays());
        holder.storeHours.setText(item.getStoreHours());
        //holder.storeSituation.setText(String.valueOf(item.getStoreSituation()));
        //나중에 추가
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
