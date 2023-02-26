package com.example.consumer_client.cart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;
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
import retrofit2.http.GET;
import retrofit2.http.Query;

interface CartAdapterService{
    @GET("/cartDelete")
    Call<ResponseBody> cartDelete(@Query("user_id") String user_id, @Query("store_id") String store_id, @Query("md_id") String md_id);
}

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    int checked_count = 0;
    JsonParser jsonParser;
    CartAdapterService service;
    Context mContext;
    String user_id, store_id, md_id;

    private ArrayList<String> mData = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
        void onDeleteClick(View v, int pos);
    }


    private CartListAdapter.OnItemClickListener mListener = null;

    public void setOnItemClickListener(CartListAdapter.OnItemClickListener listener){
        this.mListener= listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mdImg;
        TextView eachMdTotalPrice, eachMdPrice, storeName, eachStoreTotalPrice, mdName, mdSet, qty;
        String TAG = CartListActivity.class.getSimpleName();
        Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(mContext.getString(R.string.baseurl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = (CartAdapterService) retrofit.create(CartAdapterService.class);
            jsonParser = new JsonParser();

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
//            eachMdPrice = (TextView) itemView.findViewById(R.id.CLP_EachMdPrice);
//            eachMdTotalPriceP = (TextView) itemView.findViewById(R.id.CLP_EachMdTotalPrice);
            eachMdPrice = (TextView) itemView.findViewById(R.id.CL_EachMdPrice);
            eachStoreTotalPrice = (TextView) itemView.findViewById(R.id.CL_EachStoreTotalPrice);
            mdImg = (ImageView) itemView.findViewById(R.id.CL_MD_IMG);
            storeName = (TextView) itemView.findViewById(R.id.CL_StoreName);
            mdName = (TextView) itemView.findViewById(R.id.CL_MdName);
            mdSet = (TextView) itemView.findViewById(R.id.CL_MdSet);
            qty = (TextView) itemView.findViewById(R.id.CL_PurchaseNum);
            ImageView checkBtn = (ImageView) itemView.findViewById(R.id.CL_CheckButton);
            checkBtn.setImageResource(R.drawable.ic_check_off);
            checkBtn.setTag("check");
            checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkBtn.getTag().equals("check")) {
                        if (checked_count == 0) {
                            checkBtn.setImageResource(R.drawable.ic_check_on);
                            checkBtn.setTag("checked");
                            checked_count += 1;
                        } else {
                            Toast.makeText(view.getContext(), "하나의 스토어의 제품만 선택할 수 있습니다.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        checkBtn.setImageResource(R.drawable.ic_check_off);
                        checkBtn.setTag("check");
                        checked_count -= 1;
                    }
                }
            });

            deleteBtn = (Button) itemView.findViewById(R.id.CartDelete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onDeleteClick(view, pos);
                        }
                    }

                    Call<ResponseBody> call = service.cartDelete(user_id, store_id, md_id);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                JsonObject res = (JsonObject) jsonParser.parse(response.body().string());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(TAG, "onFailure: e " + t.getMessage());
                        }
                    });
                }
            });

//            Call<ResponseBody> call = service.cartListGet();
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    try {
//                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                        stk_remain = res.get("stk_remain").getAsJsonArray();
//                        pay_price = res.get("pay_price").getAsJsonArray();
//                        checkBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if (checkBtn.getTag().equals("check")) {
//                                    if (checked_count == 0) {
//                                        checkBtn.setImageResource(R.drawable.ic_check_on);
//                                        checkBtn.setTag("checked");
//                                        checked_count += 1;
//                                    } else {
//                                        Toast.makeText(view.getContext(), "하나의 스토어의 제품만 선택할 수 있습니다.", Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    checkBtn.setImageResource(R.drawable.ic_check_off);
//                                    checkBtn.setTag("check");
//                                    checked_count -= 1;
//                                }
//                            }
//                        });
////                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.e(TAG, "onFailure: e " + t.getMessage());
//                }
//            });

//                    Call<ResponseBody> call = service.cartListGet(body);
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            try {
//                                JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                                stk_remain = res.get("stk_remain").getAsJsonArray();
//                                pay_price = res.get("pay_price").getAsJsonArray();
//                                if (checkBtn.getTag().equals("like")) {
//                                    checkBtn.setImageResource(R.drawable.ic_check_on);
//                                    checkBtn.setTag("liked");
//                                } else {
//                                    checkBtn.setImageResource(R.drawable.ic_check_off);
//                                    checkBtn.setTag("like");
//                                }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            Log.e(TAG, "onFailure: e " + t.getMessage());
//                        }
//                    });
//                }
//            });

//            ImageView mdMinusBtn = (ImageView) itemView.findViewById(R.id.CL_mdMinusBtn);
//            ImageView mdPlusBtn = (ImageView) itemView.findViewById(R.id.CL_mdPlusBtn);
//
//            //상품개수
//            count=Integer.parseInt(String.valueOf(qty.getText()));
//            Log.d("count : ", String.valueOf(count));
//
//            //+버튼
//            mdPlusBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //재고 있을 시 count++
//                    Log.d("count 개수: ", String.valueOf(count));
////                    if(Integer.parseInt(StkRemain) < (count+1) ){ //n세트 * m개
////                        Toast.makeText(v.getContext(), "재고 확인 후 다시 주문해주세요", Toast.LENGTH_SHORT).show();
////                    }else{
//                        count++;
//                        qty.setText(count+"");
//                    qty.setText(qty.getText().toString());
//                    eachStoreTotalPrice.setText(Integer.parseInt(qty.getText().toString()) * Integer.parseInt(eachStoreTotalPrice.getText().toString()));
////                    }
//                }
//            });
//
//            //-버튼튼
//            mdMinusBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (count == 1){
//                        Toast.makeText(v.getContext(), "1 세트 이상의 수를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        count--;
//                        qty.setText(count+"");
//                        qty.setText(qty.getText().toString());
//                        eachStoreTotalPrice.setText(Integer.parseInt(qty.getText().toString()) * Integer.parseInt(eachStoreTotalPrice.getText().toString()));
//                    }
//                }
//            });
        }
    }


    private ArrayList<CartListInfo> mList = null;

    public CartListAdapter(ArrayList<CartListInfo> mList) {
        this.mList = mList;
    }

    // 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.cart_list, parent, false);
        CartListAdapter.ViewHolder vh = new CartListAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        CartListInfo item = mList.get(position);
        user_id = ((CartListActivity)CartListActivity.mContext).user_id;
        store_id = ((CartListActivity)CartListActivity.mContext).store_id;
        md_id = ((CartListActivity)CartListActivity.mContext).md_id;
        Glide.with(holder.itemView).load(item.getMdImg()).into(holder.mdImg);
        holder.eachMdPrice.setText(item.getEachMdPrice());
        holder.eachStoreTotalPrice.setText(item.getEachStoreTotalPrice());
        holder.mdName.setText(item.getMdName());
        holder.mdSet.setText(item.getMdSet());
        holder.storeName.setText(item.getStoreName());
        holder.qty.setText(item.getQty());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
