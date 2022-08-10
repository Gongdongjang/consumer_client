package com.example.consumer_client;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.consumer_client.cart.CartListActivity;

import java.util.Calendar;

public class OrderDialog extends Dialog {

    ImageView btn_shutdown, btn_date, btn_time;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    Spinner PurchaseNumSpinner;
    TextView PickUpDate, PickUpTime;
    CheckBox BringBasketCheck;
    TextView PopupProdName, PopupProdNum, PopupProdPrice;
    Button JP_OrderBtn;
    ImageView JP_CartBtn;
    Context mContext;

    //popuporderActivitiy
    Boolean selectNum = false;
//    Boolean selectDate = false;

    String user_id;

    public OrderDialog(@NonNull Context context, String mdName, String prodNum, String prodPrice,
                       String pu_start, String pu_end, String store_name, String store_id, String store_loc, String store_lat, String store_long,
                       String user_id, String md_id) {
        super(context);
        setContentView(R.layout.activity_payment_popup);

        Log.d("유저아이디", user_id);

        //상품명 + n개 000원 추가했음.
        PopupProdName=findViewById(R.id.PopupProdName);
        PopupProdName.setText(mdName);
        PopupProdNum=findViewById(R.id.PopupProdNum);
        PopupProdNum.setText(prodNum);
        PopupProdPrice=findViewById(R.id.PopupProdPrice);
        PopupProdPrice.setText(prodPrice);

        //픽업기간 세팅하기
        PickUpDate=findViewById(R.id.PickUpDate);
        PickUpTime=findViewById(R.id.PickUpTime);
        btn_date=findViewById(R.id.btn_date);
        btn_time=findViewById(R.id.btn_time);
        Log.d("주문하기_기간:",pu_start); //2022. 8. 28.

        String[] startDay = pu_start.split("\\.");  // .으로 자르고 싶을땐 \\. 이라고 해야함
        String[] endDay = pu_end.split("\\.");

        //month,day 한자리수 공백제거
        for(int i=1;i<3;i++){
            startDay[i]=startDay[i].trim();
            endDay[i]=endDay[i].trim();
        }

        //스피너 (개수)세팅
        PurchaseNumSpinner=findViewById(R.id.PurchaseNumSpinner);
        final String[] purchaseNum = {"1세트","2세트","3세트","4세트","5세트"};

        ArrayAdapter purchaseNumAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, purchaseNum);
        purchaseNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PurchaseNumSpinner.setAdapter(purchaseNumAdapter);

        //장바구니 지참체크
        BringBasketCheck=findViewById(R.id.BringBasketCheck);

        //----SetOnClick
        //픽업개수 선택
        PurchaseNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectNum = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //픽업날짜선택
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar =Calendar.getInstance(); //오늘날짜 변수 담기
                Calendar minDate=Calendar.getInstance();   //픽업 시작날짜
                Calendar maxDate=Calendar.getInstance();   //픽업 마감날짜

                calendar.set(Integer.parseInt(startDay[0]),Integer.parseInt(startDay[1])-1,Integer.parseInt(startDay[2]));

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

                //픽업 시작날짜부터 선택가능
                minDate.set(Integer.parseInt(startDay[0]),Integer.parseInt(startDay[1])-1,Integer.parseInt(startDay[2]));
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
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
                // Get Current Time
                Calendar c = Calendar.getInstance();

                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(v.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                PickUpTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }

        });

        //닫기버튼
        btn_shutdown=findViewById(R.id.btn_shutdown);
        btn_shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //장바구니 버튼
        JP_CartBtn=findViewById(R.id.JP_CartBtn);
        JP_CartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CartListActivity.class);
                if(BringBasketCheck.isChecked()){   //장바구니 지참사항 확인해야 넘어감
                    //스토어정보+ dialog 값 전달
                    i.putExtra("mdName",mdName);
                    i.putExtra("purchaseNum",PurchaseNumSpinner.getSelectedItem().toString());
                    i.putExtra("prodPrice",prodPrice);
                    i.putExtra("store_name",store_name);
                    i.putExtra("store_loc",store_loc);
                    i.putExtra("store_lat",store_lat);
                    i.putExtra("store_long",store_long);
                    i.putExtra("pickupDate",PickUpDate.getText());
                    i.putExtra("pickupTime",PickUpTime.getText());
                    v.getContext().startActivity(i);
                    if (selectNum){
                        Toast.makeText(getContext(), "제품을 장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "장바구니 지참사항 확인하셨나요?", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //주문하기 버튼
        JP_OrderBtn=findViewById(R.id.JP_OrderBtn);
        JP_OrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PayActivity.class);
                if(BringBasketCheck.isChecked()){   //장바구니 지참사항 확인해야 넘어감
                    //스토어정보+ dialog 값 전달
                    i.putExtra("user_id",user_id);
                    i.putExtra("md_id",md_id);
                    i.putExtra("mdName",mdName);
                    i.putExtra("purchaseNum",PurchaseNumSpinner.getSelectedItem().toString());
                    i.putExtra("prodPrice",prodPrice);
                    i.putExtra("store_name",store_name);
                    i.putExtra("store_id",store_id);
                    i.putExtra("store_loc",store_loc);
                    i.putExtra("store_lat",store_lat);
                    i.putExtra("store_long",store_long);
                    i.putExtra("pickupDate",PickUpDate.getText());
                    i.putExtra("pickupTime",PickUpTime.getText());
                    v.getContext().startActivity(i);
                }
                else{
                    Toast.makeText(getContext(), "장바구니 지참사항 확인하셨나요?", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}