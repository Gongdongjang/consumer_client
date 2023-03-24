package com.gdjang.consumer_client.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.R;

import java.util.ArrayList;

public class CartListAdapter2 extends RecyclerView.Adapter<CartListAdapter2.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private CartListAdapter2.OnItemClickListener mListener = null;

    public void setOnItemClickListener(CartListAdapter2.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eachMdPrice, eachMdTotalPriceP, totalPrice, qty;

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

            eachMdPrice = (TextView) itemView.findViewById(R.id.CLP_EachMdPrice);
            eachMdTotalPriceP = (TextView) itemView.findViewById(R.id.CLP_EachMdTotalPrice);
            totalPrice = (TextView) itemView.findViewById(R.id.CLP_EachMdTotalPrice);
            qty = (TextView) itemView.findViewById(R.id.CLP_EachMdCount);
        }
    }

    private ArrayList<CartListInfo> mList = null;

    public CartListAdapter2(ArrayList<CartListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public CartListAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.cart_paylist, parent, false);
        CartListAdapter2.ViewHolder vh = new CartListAdapter2.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter2.ViewHolder holder, int position) {
        CartListInfo item = mList.get(position);
        holder.eachMdTotalPriceP.setText(item.getEachMdTotalPriceP());
        holder.eachMdPrice.setText(item.getEachMdPrice());
        holder.totalPrice.setText(item.getTotalPrice());
        holder.qty.setText(item.getQty());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
