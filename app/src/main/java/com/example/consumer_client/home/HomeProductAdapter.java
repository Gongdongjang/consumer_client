package com.example.consumer_client.home;

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

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView md_id;
        ImageView imgView_item;
        TextView txt_main;
        TextView txt_sub;
        TextView txt_distance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 아이템 클릭 이벤트 처리.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   //md_id 저장
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v,pos);
                    }

                }
            });

            md_id=(TextView) itemView.findViewById(R.id.homeMdId);
            imgView_item = (ImageView) itemView.findViewById(R.id.homeProdImg_item);
            txt_main = (TextView) itemView.findViewById(R.id.homeProdName_item);
            txt_sub = (TextView) itemView.findViewById(R.id.homeProdEx_item);
            txt_distance=(TextView) itemView.findViewById(R.id.storeLocationFromMe);
        }
    }

    private ArrayList<HomeProductItem> mList = null;

    public HomeProductAdapter(ArrayList<HomeProductItem> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_home_product, parent, false);
        HomeProductAdapter.ViewHolder vh = new HomeProductAdapter.ViewHolder(view);
        return vh;
    }


    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull HomeProductAdapter.ViewHolder holder, int position) {
        HomeProductItem item = mList.get(position);
        holder.md_id.setText(item.getHomeMdId());
        holder.imgView_item.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.txt_main.setText(item.getHomeProdName());
        holder.txt_sub.setText(item.getHomeProdEx());
        holder.txt_distance.setText(item.getHomeDistance());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
