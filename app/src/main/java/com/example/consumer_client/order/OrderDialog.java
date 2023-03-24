package com.example.consumer_client.order;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface CartPostService{
    @POST("cartPost")
    Call<ResponseBody> cartPost(@Body JsonObject body);
}

public class OrderDialog extends Dialog {
    String TAG = OrderDialog.class.getSimpleName();

    ImageView btn_shutdown, btn_date, btn_time, BringBasketCheck;
    boolean basketChek = false;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    //Spinner PurchaseNumSpinner;
    ImageView mdPlusBtn, mdMinusBtn;
    EditText PurchaseNum;
    int count;

    TextView PickUpDate, PickUpTime, JP_Remain_Count;
    TextView JP_ToTalPrice, JP_SelectCount;
    Button JP_OrderBtn;
    ImageView JP_CartBtn;
    Context mContext;

    CartDialog cartDialog;
    OrderDialog orderDialog;
    //popuporderActivitiy

    JsonObject body;
    JsonParser jsonParser;
    CartPostService service;

    BottomSheetDialog bottomSheetDialog;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
//    CustomTimePickerDialog customTimePickerDialog;
//    private final static int TIME_PICKER_INTERVAL = 10;
//    private TimePicker mTimePicker;

    public OrderDialog(@NonNull Context context, String mdName, String prodPrice,
                       String StkRemain, String pu_start, String pu_end, String pickup_start, String pickup_end, String store_name,
                       String store_id, String store_loc, String user_id, String md_id, String mdimg_thumbnail) {
        super(context);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.activity_payment_popup2);
        bottomSheetDialog.show();

//        setContentView(R.layout.activity_payment_popup2);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonParser = new JsonParser();
        service = retrofit.create(CartPostService.class);
        body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);

//        orderDialog = this;
        Log.d("유저아이디", user_id);

//        //상품명 + n개 000원 추가했음.
//        PopupProdName=findViewById(R.id.PopupProdName);
//        PopupProdName.setText(mdName);
//        PopupProdNum=findViewById(R.id.PopupProdNum);
//        PopupProdNum.setText(prodNum);
//        PopupProdPrice=findViewById(R.id.PopupProdPrice);
//        PopupProdPrice.setText(prodPrice);

        //픽업기간 세팅하기
        PickUpDate=bottomSheetDialog.findViewById(R.id.PickUpDate);
        PickUpTime=bottomSheetDialog.findViewById(R.id.PickUpTime);
        btn_date=bottomSheetDialog.findViewById(R.id.btn_date);
        btn_time=bottomSheetDialog.findViewById(R.id.btn_time);
        Log.d("주문하기_기간:",pu_start); //2022. 8. 28.

        String[] startDay = pu_start.split("\\.");  // .으로 자르고 싶을땐 \\. 이라고 해야함
        String[] endDay = pu_end.split("\\.");

        //month,day 한자리수 공백제거
        for(int i=1;i<3;i++){
            startDay[i]=startDay[i].trim();
            endDay[i]=endDay[i].trim();
        }

        mdPlusBtn=bottomSheetDialog.findViewById(R.id.mdPlusBtn);
        PurchaseNum=bottomSheetDialog.findViewById(R.id.PurchaseNum);
        mdMinusBtn=bottomSheetDialog.findViewById(R.id.mdMinusBtn);

        //상품개수
        count=Integer.parseInt(String.valueOf(PurchaseNum.getText()));
        //상품남은 재고
        JP_Remain_Count=bottomSheetDialog.findViewById(R.id.JP_Remain_Count);
        JP_Remain_Count.setText(StkRemain+"개");

        //장바구니 지참체크
        BringBasketCheck=bottomSheetDialog.findViewById(R.id.BringBasketCheck);
        BringBasketCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (basketChek){
                    basketChek = false;
                    BringBasketCheck.setImageResource(R.drawable.ic_check_off);
                }else {
                    basketChek = true;
                    BringBasketCheck.setImageResource(R.drawable.ic_check_on);
                }
            }
        });

        //총 수량, 총 가격
        JP_SelectCount=bottomSheetDialog.findViewById(R.id.JP_SelectCount);
        JP_ToTalPrice=bottomSheetDialog.findViewById(R.id.JP_ToTalPrice);
        //디폴트
        JP_SelectCount.setText(PurchaseNum.getText().toString()+"개");
        JP_ToTalPrice.setText(Integer.parseInt(PurchaseNum.getText().toString()) * Integer.parseInt(prodPrice) + "원");


        //+버튼
        mdPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //재고 있을 시 count++
                Log.d("count 개수: ", String.valueOf(count));
                if(Integer.parseInt(StkRemain) < (count+1) ){ //n세트 * m개
                    Toast.makeText(getContext(), "재고 확인 후 다시 주문해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    count++;
                    PurchaseNum.setText(count+"");
                    JP_SelectCount.setText(PurchaseNum.getText().toString()+"개");
                    JP_ToTalPrice.setText(Integer.parseInt(PurchaseNum.getText().toString()) * Integer.parseInt(prodPrice) + "원");
                }
            }
        });

        //-버튼튼
        mdMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 1){
                    Toast.makeText(getContext(), "1 세트 이상의 수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    count--;
                    PurchaseNum.setText(count+"");
                    JP_SelectCount.setText(PurchaseNum.getText().toString()+"개");
                    JP_ToTalPrice.setText(Integer.parseInt(PurchaseNum.getText().toString()) * Integer.parseInt(prodPrice) + "원");
                }
            }
        });


       //픽업날짜선택
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar =Calendar.getInstance(); //오늘날짜 변수 담기
                Calendar minDate=Calendar.getInstance();   //픽업 시작날짜
                Calendar maxDate=Calendar.getInstance();   //픽업 마감날짜

                int pYear=calendar.get(Calendar.YEAR);
                int pMonth=calendar.get(Calendar.MONTH);
                int pDay=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                //1월은 0부터 시작하기 때문에 +1을 해준다.
                                month = month+1;
                                String date=year+"-"+month+"-"+day;
                                PickUpDate.setText(date);
                            }
                        },pYear,pMonth,pDay);

                //픽업 시작날짜와 현재시간 비교한 후 오늘 이전의 날짜는 선택 불가능 하도록 하기
                minDate.set(Integer.parseInt(startDay[0]),Integer.parseInt(startDay[1])-1,Integer.parseInt(startDay[2]));
                calendar.set(pYear,pMonth,pDay);

                if (calendar.getTimeInMillis()< minDate.getTimeInMillis()) {
                    datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                }else{
                    //픽업시작일이 오늘날짜 이전일 경우 처음 픽업시작일을 오늘 날짜부터 세팅하기
                    datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
                }

                //픽업 마감날짜까지 선택가능
                maxDate.set(Integer.parseInt(endDay[0]),Integer.parseInt(endDay[1])-1,Integer.parseInt(endDay[2]));
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });

        //픽업시간 선택
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set the minimum and maximum times
                String pu_Start=pickup_start;
                String pu_End=pickup_end;

                LocalTime minTime = LocalTime.of(Integer.parseInt(pu_Start.substring(0,2)), Integer.parseInt(pu_Start.substring(3,5))); // Set the desired minimum time
                LocalTime maxTime = LocalTime.of(Integer.parseInt(pu_End.substring(0,2)), Integer.parseInt(pu_End.substring(3,5))); // Set the desired maximum time

                // Create a TimePickerDialog with custom onTimeChanged listener
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // Handle the selected time
                                PickUpTime.setText( selectedHour + ":" + selectedMinute);
                            }
                        },
                        minTime.getHour(),
                        minTime.getMinute(),
                        false) {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        // Check if the selected time is within the specified range
                        LocalTime selectedTime = LocalTime.of(hourOfDay, minute);
                        if (selectedTime.isBefore(minTime)) {
                            view.setCurrentHour(minTime.getHour());
                            view.setCurrentMinute(minTime.getMinute());
                        } else if (selectedTime.isAfter(maxTime)) {
                            view.setCurrentHour(maxTime.getHour());
                            view.setCurrentMinute(maxTime.getMinute());
                        }
                    }
                };

                timePickerDialog.setTitle("픽업 가능 시간 선택");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }

        });

        //주문하기 버튼
        JP_OrderBtn=bottomSheetDialog.findViewById(R.id.JP_OrderBtn);
        JP_OrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ToPayActivity.class);
                //수량+-버튼이 아닌 직접 입력했을 수도 있으니 다시 한번 재고 수량확인
                if(Integer.parseInt(StkRemain) < Integer.parseInt(PurchaseNum.getText().toString()) ){ //n세트 * m개
                    Toast.makeText(getContext(), "재고가 부족합니다.", Toast.LENGTH_SHORT).show();
                }else if (PickUpDate.getText().toString().equals("") || PickUpTime.getText().toString().equals("")){
                    Toast.makeText(getContext(), "픽업 날짜와 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(basketChek){   //장바구니 지참사항 확인해야
                        // 넘어감
                        //스토어정보+ dialog 값 전달
                        i.putExtra("user_id",user_id);
                        i.putExtra("md_id",md_id);
                        i.putExtra("mdName",mdName);
                        //i.putExtra("purchaseNum",PurchaseNumSpinner.getSelectedItem().toString());
                        i.putExtra("purchaseNum",PurchaseNum.getText().toString());
                        i.putExtra("JP_ToTalPrice",JP_ToTalPrice.getText());
                        i.putExtra("store_name",store_name);
                        i.putExtra("store_id",store_id);
                        i.putExtra("store_loc",store_loc);
                        i.putExtra("pickupDate",PickUpDate.getText());
                        i.putExtra("pickupTime",PickUpTime.getText());
                        i.putExtra("mdimg_thumbnail", mdimg_thumbnail);
                        v.getContext().startActivity(i);
                    }
                    else{
                        Toast.makeText(getContext(), "장바구니 지참사항 확인하셨나요?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 장바구니 버튼
        JP_CartBtn = bottomSheetDialog.findViewById(R.id.JP_CartBtn);
        JP_CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                body.addProperty("pu_date", PickUpDate.getText().toString());
                body.addProperty("pu_time", PickUpTime.getText().toString());
                body.addProperty("purchase_num", PurchaseNum.getText().toString());

                if(Integer.parseInt(StkRemain) < Integer.parseInt(PurchaseNum.getText().toString()) ){ //n세트 * m개
                    Toast.makeText(getContext(), "재고가 부족합니다.", Toast.LENGTH_SHORT).show();
                }else if (PickUpDate.getText().toString().equals("") || PickUpTime.getText().toString().equals("")){
                    Toast.makeText(getContext(), "픽업 날짜와 시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (basketChek) {   //장바구니 지참사항 확인해야
                        // cart 테이블에 데이터 값 삽입하기 Post 요청
                        Log.d("*********", body.toString());
                        Call<ResponseBody> call = service.cartPost(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("~~~~~~~~", response.toString());
                                if (response.isSuccessful()){
                                    try {
                                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    try {
                                        Log.d(TAG, "Fail " + response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });

//                        orderDialog.dismiss();
                        bottomSheetDialog.dismiss();
                        cartDialog = new CartDialog(context, user_id);
                        cartDialog.show();
                    } else {
                        Toast.makeText(getContext(), "장바구니 지참사항 확인하셨나요?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}