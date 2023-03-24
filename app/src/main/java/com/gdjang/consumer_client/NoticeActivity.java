package com.gdjang.consumer_client;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.content.ButtonClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeActivity extends AppCompatActivity {

    private RecyclerView noticeRecyclerView;
    private RecyclerView.Adapter noticeAdapter;

    private Button btnAdd, btnMinus;
    private TextView cartCount;
    private int count = 0;

    ArrayList<Map<String, Object>> jsonList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeRecyclerView = findViewById(R.id.NoticeRecycler);
        cartCount = findViewById(R.id.CartProdCount);
        cartCount.setText(count+"");
        btnAdd = findViewById(R.id.CartBtnPlus);
        btnMinus = findViewById(R.id.CartBtnMinus);

        //Retrofit 객체생성
        //

        //밀어서 삭제하기
        SwipeHelper swipeHelper= new SwipeHelper(NoticeActivity.this,noticeRecyclerView,300) {
            @Override
            public void instantiatrMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(NoticeActivity.this,
                        "Delete",
                        30,
                        R.drawable.ic_baseline_delete_24,
                        Color.parseColor("#FF3C30"),
                        new ButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Toast.makeText(NoticeActivity.this, "Delete click", Toast.LENGTH_SHORT).show();
                                Log.d("TAG",viewHolder.getAdapterPosition()+"");
                                jsonList.remove(viewHolder.getAdapterPosition());                // 해당 항목 삭제
                                noticeAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());    // Adapter에 알려주기.
                            }
                        }));
            }
        };

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(count==5){
                    Toast.makeText(NoticeActivity.this, "대용량 구매는 불가능합니다.", Toast.LENGTH_SHORT).show();
                }
                count++;
                cartCount.setText(count+"");
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(count==1){
                    Toast.makeText(NoticeActivity.this, "1 이상의 수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                count--;
                cartCount.setText(count+"");
            }
        });
    }


    public void click_btn(View view) {
        noticeRecyclerView.setAdapter(noticeAdapter);
    }
}
