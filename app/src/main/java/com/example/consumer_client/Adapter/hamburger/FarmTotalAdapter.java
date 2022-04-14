package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;

public class FarmTotalAdapter extends RecyclerView.Adapter<FarmTotalAdapter.ViewHolder> {
    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView farmProdImgView;
        TextView farmName;
        TextView farmProdName;
        TextView farmFeature;
        TextView farmSituation;

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
            farmProdImgView = (ImageView) itemView.findViewById(R.id.FarmProdImg_item);
            farmName = (TextView) itemView.findViewById(R.id.FarmName);
            farmProdName = (TextView) itemView.findViewById(R.id.FarmProdName_item);
            farmFeature = (TextView) itemView.findViewById(R.id.FarmFeature_info);
            farmSituation = (TextView) itemView.findViewById(R.id.FarmSituation);
        }
    }

    private ArrayList<FarmTotalInfo> mList = null;

    public FarmTotalAdapter(ArrayList<FarmTotalInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.farm_total_list, parent, false);
        FarmTotalAdapter.ViewHolder vh = new FarmTotalAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmTotalAdapter.ViewHolder holder, int position) {
        FarmTotalInfo item = mList.get(position);

        holder.farmProdImgView.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.farmName.setText(item.getFarmName());
        holder.farmProdName.setText(item.getFarmProdName());
        holder.farmFeature.setText(item.getFarmFeature());
        holder.farmSituation.setText(item.getFarmSituation());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
