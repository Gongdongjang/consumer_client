package com.example.consumer_client.order;

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
import com.example.consumer_client.review.ReviewActivity;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OrderListAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView storeProdImgView;
        TextView user_id, storeName, storeLocationFromMe, mdName, mdQty, mdPrice, puDate, OrderReview;
        TextView mdStatus; //픽업하면 mdStatus로 바뀌어야 함

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

            user_id = (TextView) itemView.findViewById(R.id.UserId);
            storeProdImgView = (ImageView) itemView.findViewById(R.id.ProdImg);
            storeName = (TextView) itemView.findViewById(R.id.StoreName);
            storeLocationFromMe = (TextView) itemView.findViewById(R.id.StoreLocationFromMe);
            mdName = (TextView) itemView.findViewById(R.id.MdName);
            mdQty = (TextView) itemView.findViewById(R.id.MdQty);
            mdPrice = (TextView) itemView.findViewById(R.id.MdPrice);
            puDate = (TextView) itemView.findViewById(R.id.Pudate);
            mdStatus = (TextView) itemView.findViewById(R.id.Pudate); //픽업하면 mdStatus로 바뀌어야 함
            OrderReview = (TextView) itemView.findViewById(R.id.OrderReview);
        }
    }

    private ArrayList<OrderListInfo> mList = null;

    public OrderListAdapter(ArrayList<OrderListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.order_list, parent, false);
        OrderListAdapter.ViewHolder vh = new OrderListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.ViewHolder holder, int position) {
        OrderListInfo item = mList.get(position);
        holder.user_id.setText(item.getUserId());
        holder.storeProdImgView.setImageResource(R.drawable.ic_launcher_background);
        holder.storeName.setText(item.getStoreName());
        holder.storeLocationFromMe.setText(item.getStoreLocationFromMe());
        holder.mdName.setText(item.getMdName());
        holder.mdQty.setText(item.getMdQty());
        holder.mdPrice.setText(item.getMdPrice());
        holder.puDate.setText(item.getPuDate());
        Context context = holder.itemView.getContext();
        holder.OrderReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra("user_id", item.getUserId());
                intent.putExtra("md_name", item.getMdName());
                intent.putExtra("md_qty", item.getMdQty());
                intent.putExtra("md_fin_price", String.valueOf(Integer.parseInt(item.getMdQty()) * Integer.parseInt(item.getMdPrice())));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}