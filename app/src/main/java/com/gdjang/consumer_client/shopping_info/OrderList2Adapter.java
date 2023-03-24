package com.gdjang.consumer_client.shopping_info;

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
import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.order.OrderListInfo;
import com.gdjang.consumer_client.review.ReviewActivity;

import java.util.ArrayList;
import java.util.Objects;

public class OrderList2Adapter extends RecyclerView.Adapter<OrderList2Adapter.ViewHolder>{
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OrderList2Adapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(OrderList2Adapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeid;
        ImageView storeProdImgView;
        TextView storeName;
        TextView mdName;
        TextView mdQty;
        TextView mdPrice;
        TextView mdStatus;
        TextView puDate;
        TextView OrderReview;

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
//            storeid = (TextView) itemView.findViewById(R.id.StoreID);
            storeProdImgView = (ImageView) itemView.findViewById(R.id.ProdImg);
            storeProdImgView.setClipToOutline(true);
            storeName = (TextView) itemView.findViewById(R.id.StoreName);
            mdName = (TextView) itemView.findViewById(R.id.MdName);
            mdQty = (TextView) itemView.findViewById(R.id.MdQty);
            mdPrice = (TextView) itemView.findViewById(R.id.MdPrice);
            puDate = (TextView) itemView.findViewById(R.id.Pudate);
            mdStatus = (TextView) itemView.findViewById(R.id.mdStatus);
            OrderReview = (TextView) itemView.findViewById(R.id.OrderReview);
        }
    }

    private ArrayList<OrderListInfo> mList = null;

    public OrderList2Adapter(ArrayList<OrderListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public OrderList2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.shopping_list, parent, false);
        OrderList2Adapter.ViewHolder vh = new OrderList2Adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderList2Adapter.ViewHolder holder, int position) {
        OrderListInfo item = mList.get(position);
//        holder.userid.setText(item.getUserId());
        Glide.with(holder.itemView).load(item.getStoreProdImgView()).into(holder.storeProdImgView);
        holder.storeName.setText(item.getStoreName());
        holder.mdName.setText(item.getMdName());
        holder.mdQty.setText(item.getMdQty());
        holder.mdPrice.setText(item.getMdPrice());
        holder.mdStatus.setText(item.getMdStatus());
        holder.puDate.setText(item.getPuDate());
        Context context = holder.itemView.getContext();

        if (Objects.equals(item.getMdStatus(), "1")){
            holder.puDate.setTextColor(Color.parseColor("#848484"));
            holder.OrderReview.setVisibility(View.VISIBLE);    //리뷰작성 버튼 보이기
            holder.OrderReview.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(context, ReviewActivity.class);
                    intent.putExtra("user_id", item.getUserId());
                    intent.putExtra("order_id", item.getOrderId());
                    intent.putExtra("store_name", item.getStoreName());
                    intent.putExtra("store_loc", item.getStoreLoc());
                    intent.putExtra("md_name", item.getMdName());
                    intent.putExtra("md_qty", item.getMdQty());
                    intent.putExtra("md_fin_price", item.getMdPrice());
                    intent.putExtra("mdimg_thumbnail", item.getStoreProdImgView());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
