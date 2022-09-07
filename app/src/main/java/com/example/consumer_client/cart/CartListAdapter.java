package com.example.consumer_client.cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        ImageView storeProdImgView;
        TextView user_id, storeName, mdName, mdPrice, puDate, totalPrice, paycount, prodSet, prodCount;

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

//            user_id = (TextView) itemView.findViewById(R.id.UserId);
            storeProdImgView = (ImageView) itemView.findViewById(R.id.CartProdImg);
//            storeName = (TextView) itemView.findViewById(R.id.StoreName);
            mdName = (TextView) itemView.findViewById(R.id.CartProdName);
            mdPrice = (TextView) itemView.findViewById(R.id.CartProdPrice);
            puDate = (TextView) itemView.findViewById(R.id.CartPickUpDate);
            totalPrice = (TextView) itemView.findViewById(R.id.CartTotalPrice);
            paycount = (TextView) itemView.findViewById(R.id.CartProdPayCount);
            prodSet = (TextView) itemView.findViewById(R.id.CartProdSet);
            prodCount = (TextView) itemView.findViewById(R.id.CartProdCount);
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

        View view = inflater.inflate(R.layout.cart_get_list, parent, false);
        CartListAdapter.ViewHolder vh = new CartListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartListInfo item = mList.get(position);
//        holder.user_id.setText(item.getUserId());
        holder.storeProdImgView.setImageResource(R.drawable.ic_launcher_background);
        holder.paycount.setText(item.getPayCount());
        holder.mdName.setText(item.getMdName());
        holder.mdPrice.setText(item.getMdPrice());
        holder.puDate.setText(item.getPuDate());
        holder.totalPrice.setText(item.getTotalPrice());
        holder.prodSet.setText(item.getProdSet());
        holder.prodCount.setText(item.getPayCount());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
