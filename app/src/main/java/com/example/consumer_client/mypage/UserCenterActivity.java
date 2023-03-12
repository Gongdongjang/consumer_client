package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;
import java.util.List;

public class UserCenterActivity extends AppCompatActivity {
    Context mContext;
    String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_customer_service);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");

        RecyclerView recyclerView = findViewById(R.id.FAQ_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableAdapter.Item> data = new ArrayList<>();

//        data.add(new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다."));
//        data.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));

        ExpandableAdapter.Item places = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places.invisibleChildren = new ArrayList<>();
        places.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places);
        ExpandableAdapter.Item places2 = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places2.invisibleChildren = new ArrayList<>();
        places2.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places2);
        ExpandableAdapter.Item places3 = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places3.invisibleChildren = new ArrayList<>();
        places3.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places3);
        ExpandableAdapter.Item places4 = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places4.invisibleChildren = new ArrayList<>();
        places4.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places4);
        ExpandableAdapter.Item places5 = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places5.invisibleChildren = new ArrayList<>();
        places5.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places5);
        ExpandableAdapter.Item places6 = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, "FAQ 제목입니다.");
        places6.invisibleChildren = new ArrayList<>();
        places6.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, "FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. FAQ 내용입니다. "));
        data.add(places6);

        recyclerView.setAdapter(new ExpandableAdapter(data));

        Button OpenKakao_Btn = (Button) findViewById(R.id.OpenKakao_Btn);
        OpenKakao_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://open.kakao.com/o/suhyQ78e"));
                startActivity(intent);
            }
        });
    }
}
