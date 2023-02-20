package com.example.consumer_client.review;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

import org.w3c.dom.Text;

public class ReviewActivity<starScore> extends AppCompatActivity {
    final int PICTURE_REQUEST_CODE = 100;
    String md_name, md_qty, md_fin_price;
    ImageView reviewImg1, reviewImg2, reviewImg3;
    RatingBar ratingBar;
    TextView starScore;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        Intent intent = getIntent();
        md_name = intent.getStringExtra("md_name");
        md_qty = intent.getStringExtra("md_qty");
        md_fin_price = intent.getStringExtra("md_fin_price");

        reviewImg1=(ImageView)findViewById(R.id.reviewImg1);
        reviewImg2=(ImageView)findViewById(R.id.reviewImg2);
        reviewImg3=(ImageView)findViewById(R.id.reviewImg3);

        TextView JP_ProdName = (TextView) findViewById(R.id.JP_ProdName);
        TextView ClientOrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView ClientOrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);

        JP_ProdName.setText(md_name);
        ClientOrderCount.setText(md_qty);
        ClientOrderPrice.setText(md_fin_price);

        ImageButton addImg = (ImageButton)findViewById(R.id.addImg);
        addImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "리뷰 사진을 선택하세요"), PICTURE_REQUEST_CODE);
            }
        });
        starScore = findViewById(R.id.scoreRate);
        starScore.setText("0점");

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starScore.setText(rating+"점");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                reviewImg1.setImageResource(0);
                reviewImg2.setImageResource(0);
                reviewImg3.setImageResource(0);
                Uri uri = data.getData();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < 3; i++) {
                        if (i < clipData.getItemCount()) {
                            Uri getImgUri = clipData.getItemAt(i).getUri();
                            switch (i) {
                                case 0:
                                    reviewImg1.setImageURI(getImgUri);
                                    break;
                                case 1:
                                    reviewImg2.setImageURI(getImgUri);
                                    break;
                                case 2:
                                    reviewImg3.setImageURI(getImgUri);
                                    break;
                            }
                        }
                    }
                }
                else if (uri != null) {
                    reviewImg1.setImageURI(uri);
                }
            }
        }
    }
}