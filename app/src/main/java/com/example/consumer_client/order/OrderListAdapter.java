package com.example.consumer_client.order;

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
//        TextView storeid;
        ImageView storeProdImgView;
        TextView storeName;
        TextView storeLocationFromMe;
        TextView mdName;
        TextView mdComp;
        TextView mdPrice;
        TextView mdStatus;
        TextView puDate; //픽업하면 mdStatus로 바뀌어야 함

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
//            storeid=(TextView) itemView.findViewById(R.id.StoreID);
            storeProdImgView = (ImageView) itemView.findViewById(R.id.ProdImg);
            storeName = (TextView) itemView.findViewById(R.id.StoreName);
            storeLocationFromMe = (TextView) itemView.findViewById(R.id.StoreLocationFromMe);
            mdName = (TextView) itemView.findViewById(R.id.MdName);
            mdComp = (TextView) itemView.findViewById(R.id.MdComp);
            mdPrice = (TextView) itemView.findViewById(R.id.MdPrice);
            puDate = (TextView) itemView.findViewById(R.id.Pudate);
            mdStatus = (TextView) itemView.findViewById(R.id.Pudate); //픽업하면 mdStatus로 바뀌어야 함
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

        holder.storeProdImgView.setImageResource(R.drawable.ic_launcher_background);
        holder.storeName.setText(item.getStoreName());
        holder.storeLocationFromMe.setText(item.getStoreLocationFromMe());
        holder.mdName.setText(item.getMdName());
        holder.mdComp.setText(item.getMdComp());
        holder.mdPrice.setText(item.getMdPrice());
        holder.puDate.setText(item.getPuDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
