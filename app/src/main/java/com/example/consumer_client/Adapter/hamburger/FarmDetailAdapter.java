package com.example.consumer_client.Adapter.hamburger;

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

public class FarmDetailAdapter extends RecyclerView.Adapter<FarmDetailAdapter.ViewHolder> {
    private ArrayList<String> mData = null;
    private ItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView farmDetailProdImgView;
        TextView farmDetailName;
        TextView vegetableName;
        TextView farmDetailProdName;
        TextView farmDetailProdExplain;
        TextView prodNum;
        TextView prodPrice;
        TextView prodNum1;
        TextView prodPrice1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            farmDetailProdImgView = (ImageView) itemView.findViewById(R.id.FarmDetailProdImg_item);
            farmDetailName = (TextView) itemView.findViewById(R.id.FarmDetailName);
            vegetableName = (TextView) itemView.findViewById(R.id.VegetableName);
            farmDetailProdName = (TextView) itemView.findViewById(R.id.FarmDetailProdName);
            farmDetailProdExplain = (TextView) itemView.findViewById(R.id.FarmDetailProdExplain);
            prodNum = (TextView) itemView.findViewById(R.id.ProdNum);
            prodPrice = (TextView) itemView.findViewById(R.id.ProdPrice);
            prodNum1 = (TextView) itemView.findViewById(R.id.ProdNum1);
            prodPrice1 = (TextView) itemView.findViewById(R.id.ProdPrice1);
        }
    }

    private ArrayList<FarmDetailInfo> mList = null;

    public FarmDetailAdapter(ArrayList<FarmDetailInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.farm_prod_list, parent, false);
        FarmDetailAdapter.ViewHolder vh = new FarmDetailAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmDetailAdapter.ViewHolder holder, int position) {
        FarmDetailInfo item = mList.get(position);

        holder.farmDetailProdImgView.setImageResource(R.drawable.ic_launcher_background);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.farmDetailName.setText(item.getFarmDetailName());
        holder.vegetableName.setText(item.getVegetableName());
        holder.farmDetailProdName.setText(item.getFarmDetailProdName());
        holder.farmDetailProdExplain.setText(item.getFarmDetailProdExplain());
        holder.prodNum.setText(item.getProdNum());
        holder.prodPrice.setText(item.getProdPrice());
        holder.prodNum1.setText(item.getProdNum1());
        holder.prodPrice1.setText(item.getProdPrice1());
    }

    public interface ItemClickListener{
        void onItemClick(FarmTotalInfo details,String pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
