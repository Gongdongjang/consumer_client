package com.example.consumer_client.cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private CartListAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(CartListAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mdImg;
        TextView eachMdTotalPrice, storeName, eachStoreTotalPrice, mdName, mdSet, qty;

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

//            eachMdPrice = (TextView) itemView.findViewById(R.id.CLP_EachMdPrice);
//            eachMdTotalPriceP = (TextView) itemView.findViewById(R.id.CLP_EachMdTotalPrice);
            eachMdTotalPrice = (TextView) itemView.findViewById(R.id.CL_EachMdTotalPrice);
            eachStoreTotalPrice = (TextView) itemView.findViewById(R.id.CL_EachStoreTotalPrice);
            mdImg = (ImageView) itemView.findViewById(R.id.CL_MD_IMG);
            storeName = (TextView) itemView.findViewById(R.id.CL_StoreName);
            mdName = (TextView) itemView.findViewById(R.id.CL_MdName);
            mdSet = (TextView) itemView.findViewById(R.id.CL_MdSet);
            qty = (TextView) itemView.findViewById(R.id.CL_PurchaseNum);
        }
    }

    private ArrayList<CartListInfo> mList = null;

    public CartListAdapter(ArrayList<CartListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.cart_list, parent, false);
        CartListAdapter.ViewHolder vh = new CartListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartListInfo item = mList.get(position);
        Glide.with(holder.itemView).load(item.getMdImg()).into(holder.mdImg);
        holder.eachMdTotalPrice.setText(item.getEachMdTotalPrice());
//        holder.eachMdTotalPriceP.setText(item.getEachMdTotalPriceP());
//        holder.eachMdPrice.setText(item.getEachMdPrice());
        holder.eachStoreTotalPrice.setText(item.getEachStoreTotalPrice());
        holder.mdName.setText(item.getMdName());
        holder.mdSet.setText(item.getMdSet());
        holder.storeName.setText(item.getStoreName());
        holder.qty.setText(item.getQty());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
