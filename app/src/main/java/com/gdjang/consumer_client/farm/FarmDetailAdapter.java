package com.gdjang.consumer_client.farm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.md.MdDetailInfo;

import java.util.ArrayList;

public class FarmDetailAdapter extends RecyclerView.Adapter<FarmDetailAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private FarmDetailAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(FarmDetailAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView prodImg;
        TextView prodName, storeName, mdPrice, distance, puTime, dDay, text_pu;

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
            prodImg = (ImageView) itemView.findViewById(R.id.homeProdImg_item); //제품 사진
            prodImg.setClipToOutline(true);
            prodName = (TextView) itemView.findViewById(R.id.homeProdEx_item); //제품명
            storeName = (TextView) itemView.findViewById(R.id.homeProdName_item); //스토어명
            distance = (TextView) itemView.findViewById(R.id.storeLocationFromMe);
            mdPrice = (TextView) itemView.findViewById(R.id.homeProdPrice); //세트별 가격
            dDay = (TextView) itemView.findViewById(R.id.dDay); //d-day
            puTime = (TextView) itemView.findViewById(R.id.StoreProdDate); //픽업 예정일
            text_pu = itemView.findViewById(R.id.text_pu); //픽업 예정일 안내 문구
        }
    }

    private ArrayList<MdDetailInfo> mList = null;

    public FarmDetailAdapter(ArrayList<MdDetailInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_product, parent, false);
        FarmDetailAdapter.ViewHolder vh = new FarmDetailAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmDetailAdapter.ViewHolder holder, int position) {
        MdDetailInfo item = mList.get(position);

        Glide.with(holder.itemView).load(item.getProdImg()).into(holder.prodImg);
        holder.prodName.setText(item.getProdName());
        holder.storeName.setText(item.getStoreName());
        holder.distance.setText(item.getDistance());
        holder.mdPrice.setText(item.getMdPrice());
        holder.dDay.setText(item.getDday());
        
        // null로 넣는 경우는 content에서 오는 경우 밖에 없어서 puTime 아예 안뜨게 지정
        if (item.getPuTime().equals("null")){
            holder.text_pu.setVisibility(View.GONE);
            holder.puTime.setVisibility(View.GONE);
        }
        else holder.puTime.setText(item.getPuTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
