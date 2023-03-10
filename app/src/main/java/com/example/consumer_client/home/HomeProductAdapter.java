package com.example.consumer_client.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.fragment.Keep;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface JointPurchaseService2 {
    @POST("isKeep")
    Call<ResponseBody> postisKeep(@Body JsonObject body);

    @POST("keep")
    Call<ResponseBody> postKeep(@Body JsonObject body);
}

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    public static Context mContext;
    JsonParser jsonParser;
    JointPurchaseService2 service;
    JsonObject body;
    JsonArray keep_data;
    String message;
    public String user_id, md_id2;
    public ArrayList<String> md_list;

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
        TextView txt_distance, mdPrice, dDay, puTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mContext.getString(R.string.baseurl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = (JointPurchaseService2) retrofit.create(JointPurchaseService2.class);
            jsonParser = new JsonParser();

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
            mdPrice = (TextView) itemView.findViewById(R.id.homeProdPrice); //세트별 가격
            dDay = (TextView) itemView.findViewById(R.id.dDay); //d-day
            puTime = (TextView)itemView.findViewById(R.id.StoreProdDate);

            ImageView Keep = itemView.findViewById(R.id.homeProdKeep);

            Home home = new Home();
            user_id = home.user_id;
            Keep keep = new Keep();
            md_list = keep.keep_list;
            Log.d("상품 list", md_list.toString());
            for(int i = 0; i<md_list.size(); i++){
                md_id2 = md_list.get(i);
                body = new JsonObject();
                body.addProperty("user_id", user_id);
                body.addProperty("md_id", md_id2);
                Log.d("상품 id", md_id2);
            }

            //----고정하단바-----
            //찜 한 정보 불러오기 (해당 사용자가 해당 상품에 찜했으면, 하트)
            Call<ResponseBody> call = service.postisKeep(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        Log.d("116행", res.toString());
                        message = res.get("message").getAsString();
                        if (message.equals("exist")) {
                            Keep.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Keep.setTag("liked");
                        } else if (message.equals("notexist")) {
                            Keep.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            Keep.setTag("like");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Home Keep", "onFailure: e " + t.getMessage());
                }
            });

            //찜 클릭시 취소 or 등록 제어
            Keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<ResponseBody> call = service.postKeep(body);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                keep_data = res.get("keep_data").getAsJsonArray();
                                message = res.get("message").getAsString();
                                if (Keep.getTag().equals("like")) {
                                    Toast.makeText(view.getContext(), "찜한 상품에 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    Keep.setImageResource(R.drawable.ic_baseline_favorite_24);
                                    Keep.setTag("liked");
                                } else {
                                    Toast.makeText(view.getContext(), "찜한 상품에서 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    Keep.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                    Keep.setTag("like");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("Home Keep Click", "onFailure: e " + t.getMessage());
                        }
                    });
                }
            });
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
        Glide.with(holder.itemView).load(item.getHomeProdImg()).into(holder.imgView_item);
        holder.txt_main.setText(item.getHomeProdName());
        holder.txt_sub.setText(item.getHomeProdEx());
        holder.txt_distance.setText(item.getHomeDistance());
        holder.mdPrice.setText(item.getHomeMdPrice());
        holder.dDay.setText(item.getHomeDday());
        holder.puTime.setText(item.getHomePuTime());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
