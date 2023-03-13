//package com.example.consumer_client;
//
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.security.cert.TrustAnchor;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class PopupOrderActivity extends AppCompatActivity {
//
//    Boolean selectNum = false;
//    Boolean selectDate = false;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //상태바제거 (전체화면모드)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_payment_popup);
//
//        final String[] purchaseNum = {"1개","2개","3개","4개","5개"};
//        final String[] pickUpDate = {"2021.09.10 10:00", "2021.09.10 10:30", "2021.09.10 11:00", "2021.09.10 11:30"};
//
//        Spinner purchaseNumSpinner = (Spinner) findViewById(R.id.PurchaseNumSpinner);
//        Spinner pickUpDateSpinner = (Spinner) findViewById(R.id.PickUpDateSpinner);
//
//        ArrayAdapter purchaseNumAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, purchaseNum);
//        ArrayAdapter pickUpDateAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, pickUpDate);
//        purchaseNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        pickUpDateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        purchaseNumSpinner.setAdapter(purchaseNumAdapter);
//        pickUpDateSpinner.setAdapter(pickUpDateAdapter);
//
//        purchaseNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectNum = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        pickUpDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectDate = true;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        if (selectNum && selectDate){
//            Toast.makeText(getApplicationContext(), "제품을 장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public boolean onTouchEvent(MotionEvent event){
//        //바깥레이어 클릭 시 닫히지 않게
//        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
//    }
//
//    public void onBackPressed(){
//        return;
//    }
//
//
//}
