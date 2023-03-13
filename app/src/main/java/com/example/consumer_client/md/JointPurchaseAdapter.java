package com.example.consumer_client.md;

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

import java.util.ArrayList;

public class JointPurchaseAdapter extends RecyclerView.Adapter<JointPurchaseAdapter.ViewHolder> {
    private ArrayList<String> mData = null;
    private JointPurchaseAdapter.ItemClickListener mItemClickListener;


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView jpProdImgView;
        TextView jpProdName;
        TextView jpProdDesc;
        TextView jpPriceCount;
        TextView jpPrice;
        TextView jpMaxCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jpProdImgView = (ImageView) itemView.findViewById(R.id.JP_ProdIMG);
            jpProdName = (TextView) itemView.findViewById(R.id.JP_ProdName);
            jpProdDesc = (TextView) itemView.findViewById(R.id.JP_ProdInfo);
            jpPriceCount = (TextView) itemView.findViewById(R.id.JP_Prod_Num);
            jpPrice = (TextView) itemView.findViewById(R.id.JP_Prod_Price);
            jpMaxCount = (TextView) itemView.findViewById(R.id.JP_Prod_Max_Num);
        }
    }

    private ArrayList<JointPurchaseInfo> mList = null;

    public JointPurchaseAdapter(ArrayList<JointPurchaseInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.joint_purchase_prod_list, parent, false);
        JointPurchaseAdapter.ViewHolder vh = new JointPurchaseAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull JointPurchaseAdapter.ViewHolder holder, int position) {
        JointPurchaseInfo item = mList.get(position);

        Glide.with(holder.itemView).load(item.getJpProdImgView()).into(holder.jpProdImgView);   // 사진 없어서 기본 파일로 이미지 띄움
        holder.jpProdName.setText(item.getJpProdName());
        holder.jpProdDesc.setText(item.getJpProdDesc());
        holder.jpPriceCount.setText(item.getJpPriceCount());
        holder.jpPrice.setText(item.getJpPrice());
        holder.jpMaxCount.setText(item.getJpMaxCount());
    }

    public interface ItemClickListener{
        void onItemClick(JointPurchaseInfo details,String pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
