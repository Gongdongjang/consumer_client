package com.example.consumer_client;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class OrderDialog extends Dialog {

    ImageView btn_shutdown;
    Spinner PurchaseNumSpinner, PickUpDateSpinner;
    CheckBox BringBasketCheck;
    TextView PopupProdName, pu_start, pu_end;
    Button JP_OrderBtn;
    Context mContext;

    //popuporderActivitiy
    Boolean selectNum = false;
    Boolean selectDate = false;

    public OrderDialog(@NonNull Context context, String mdName, String pu_start, String pu_end) {
        super(context);
        setContentView(R.layout.activity_payment_popup);

        //상품명 + n개 000원 추가하기
        PopupProdName=findViewById(R.id.PopupProdName);
        PopupProdName.setText(mdName);

        //픽업기간 세팅하기
        //추가하기

        //스티너 세팅+ 장바구니지참 체크
        PurchaseNumSpinner=findViewById(R.id.PurchaseNumSpinner);
        PickUpDateSpinner=findViewById(R.id.PickUpDateSpinner);
        BringBasketCheck=findViewById(R.id.BringBasketCheck);

        final String[] purchaseNum = {"1개","2개","3개","4개","5개"};
        final String[] pickUpDate = {"2021.09.10 10:00", "2021.09.10 10:30", "2021.09.10 11:00", "2021.09.10 11:30"};

        ArrayAdapter purchaseNumAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, purchaseNum);
        ArrayAdapter pickUpDateAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, pickUpDate);
        purchaseNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickUpDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PurchaseNumSpinner.setAdapter(purchaseNumAdapter);
        PickUpDateSpinner.setAdapter(pickUpDateAdapter);

        PurchaseNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectNum = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        PickUpDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectDate = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (selectNum && selectDate){
            Toast.makeText(getContext(), "제품을 장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
        }

        //닫기버튼
        btn_shutdown=findViewById(R.id.btn_shutdown);
        btn_shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //주문하기 버튼
        JP_OrderBtn=findViewById(R.id.JP_OrderBtn);
        JP_OrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PayActivity.class);
                //dialog 값 전달하기
                v.getContext().startActivity(i);
            }
        });
    }
}
