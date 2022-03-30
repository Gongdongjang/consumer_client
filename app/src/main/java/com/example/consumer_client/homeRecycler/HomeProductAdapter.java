package com.example.consumer_client.homeRecycler;

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

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView_item;
        TextView txt_main;
        TextView txt_sub;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_item = (ImageView) itemView.findViewById(R.id.homeProdImg_item);
            txt_main = (TextView) itemView.findViewById(R.id.homeProdName_item);
            txt_sub = (TextView) itemView.findViewById(R.id.homeProdEx_item);
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

        holder.imgView_item.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.txt_main.setText(item.getHomeProdName());
        holder.txt_sub.setText(item.getHomeProdEx());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
