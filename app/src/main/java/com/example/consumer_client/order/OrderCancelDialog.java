package com.example.consumer_client.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface OrderCancelService{
    @POST("orderCancel")
    Call<ResponseBody> orderCancel(@Body JsonObject body);
}

public class OrderCancelDialog extends Dialog {

    Button Cancel_Yes, Cancel_No;
    Context mContext;
    OrderCancelDialog orderCancelDialog;
    JsonObject body;
    JsonParser jsonParser;
    OrderCancelService service;

    public OrderCancelDialog(@NonNull Context context, String user_id, String order_id) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_cancel_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        orderCancelDialog = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonParser = new JsonParser();
        service = retrofit.create(OrderCancelService.class);
        body = new JsonObject();
        body.addProperty("order_id", order_id);

        Cancel_Yes = findViewById(R.id.Cancel_Yes);
        Cancel_No = findViewById(R.id.Cancel_No);

        //주문취소O
        Cancel_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<ResponseBody> call = service.orderCancel(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());

                                Intent i = new Intent(v.getContext(), MainActivity.class);
                                i.putExtra("user_id",user_id);
                                Toast.makeText(getContext(), "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                v.getContext().startActivity(i);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                Log.d("주문취소:", "Fail " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("주문취소:", "Fail " + t);
                    }
                });
            }
        });

        //주문취소X
        Cancel_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderCancelDialog.dismiss();
            }
        });
    }
}
