package com.example.consumer_client.farm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;
import com.example.consumer_client.md.MdDetailInfo;

import java.util.ArrayList;

public class FarmDetailAdapter extends RecyclerView.Adapter<FarmDetailAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private FarmDetailAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(FarmDetailAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView prodImg;
//        TextView farmName;
        TextView prodName, storeName, mdPrice, paySchedule, puTime, dDay;

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
            prodImg = (ImageView) itemView.findViewById(R.id.homeProdImg_item); //제품 사진
//            farmName = (TextView) itemView.findViewById(R.id.FarmName);
            prodName = (TextView) itemView.findViewById(R.id.homeProdEx_item); //제품명
            storeName = (TextView) itemView.findViewById(R.id.homeProdName_item); //스토어명
            mdPrice = (TextView) itemView.findViewById(R.id.homeProdPrice); //세트별 가격
//            paySchedule = (TextView) itemView.findViewById(R.id.PaySchedule);
            dDay = (TextView) itemView.findViewById(R.id.dDay); //d-day
            puTime = (TextView) itemView.findViewById(R.id.homeProdDate); //픽업 예정일
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
//        holder.prodImg.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
//        holder.farmName.setText(item.getFarmName());
        holder.prodName.setText(item.getProdName());
        holder.storeName.setText(item.getStoreName());
        holder.mdPrice.setText(item.getMdPrice());
        holder.dDay.setText(item.getDday());
//        holder.paySchedule.setText(item.getPaySchedule());
        holder.puTime.setText(item.getPuTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
