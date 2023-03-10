package com.example.consumer_client.farm;

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
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.fragment.Keep;
import com.example.consumer_client.md.MdDetailInfo;
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

interface JointPurchaseService3 {
    @POST("isKeep")
    Call<ResponseBody> postisKeep(@Body JsonObject body);

    @POST("keep")
    Call<ResponseBody> postKeep(@Body JsonObject body);
}

public class FarmDetailAdapter extends RecyclerView.Adapter<FarmDetailAdapter.ViewHolder> {
    public static Context mContext;
    JsonParser jsonParser;
    JointPurchaseService3 service;
    JsonObject body;
    JsonArray keep_data;
    String message;
    public String user_id, md_id;
    public ArrayList<String> md_list;

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
        TextView prodName, storeName, mdPrice, distance, paySchedule, puTime, dDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mContext.getString(R.string.baseurl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = (JointPurchaseService3) retrofit.create(JointPurchaseService3.class);
            jsonParser = new JsonParser();

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
            distance = (TextView) itemView.findViewById(R.id.storeLocationFromMe);
            mdPrice = (TextView) itemView.findViewById(R.id.homeProdPrice); //세트별 가격
//            paySchedule = (TextView) itemView.findViewById(R.id.PaySchedule);
            dDay = (TextView) itemView.findViewById(R.id.dDay); //d-day
            puTime = (TextView) itemView.findViewById(R.id.StoreProdDate); //픽업 예정일

            ImageView Keep = itemView.findViewById(R.id.homeProdKeep);

            Home home = new Home();
//            user_id = home.user_id;
            user_id = ((MainActivity)MainActivity.mContext).user_id;
//            com.example.consumer_client.fragment.Keep keep = new Keep();
//            md_list = keep.keep_list;
//            Log.d("상품 list", md_list.toString());
            for(int i = 0; i<2; i++){
//                md_id = md_list.get(i);
                body = new JsonObject();
                body.addProperty("user_id", user_id);
                body.addProperty("md_id", 11+i);
                Log.d("유저 id", user_id);

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
                            Log.d("116행", "에러");
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
        holder.distance.setText(item.getDistance());
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
