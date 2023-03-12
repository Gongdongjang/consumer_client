package com.example.consumer_client.review;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.example.consumer_client.R;
import com.example.consumer_client.order.OrderDetailActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ReviewService {
    @POST("review")
    Call<ResponseBody> review(@Body JsonObject body);
}

public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "review activity";
    final int PICTURE_REQUEST_CODE = 100;
    String user_id, order_id, md_name, md_qty, md_fin_price, store_name, mdimg_thumbnail, store_loc;
    ImageView reviewImg1, reviewImg2, reviewImg3;
    ImageView ClientOrderProdIMG, Star_1, Star_2, Star_3, Star_4, Star_5;
    TextView JP_StoreName;
    EditText rvw_content;
    Button reviewReg;
    int star_count = 0;
    File file1 = null, file2 = null, file3 = null;
    ReviewCancelDialog reviewCancelDialog;
    ReviewRegisterDialog reviewRegisterDialog;

    ReviewService service;
    JsonParser jsonParser;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ReviewService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        order_id = intent.getStringExtra("order_id");
        md_name = intent.getStringExtra("md_name");
        md_qty = intent.getStringExtra("md_qty");
        md_fin_price = intent.getStringExtra("md_fin_price");
        store_name = intent.getStringExtra("store_name");
        mdimg_thumbnail = intent.getStringExtra("mdimg_thumbnail");
        store_loc = intent.getStringExtra("store_loc");

        reviewImg1 = (ImageView) findViewById(R.id.reviewImg1);
        reviewImg2 = (ImageView) findViewById(R.id.reviewImg2);
        reviewImg3 = (ImageView) findViewById(R.id.reviewImg3);
        ClientOrderProdIMG = findViewById(R.id.ClientOrderProdIMG);
        JP_StoreName = findViewById(R.id.JP_StoreName);

        TextView JP_ProdName = (TextView) findViewById(R.id.JP_ProdName);
        TextView ClientOrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView ClientOrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);

        JP_ProdName.setText(md_name);
        ClientOrderCount.setText(md_qty);
        ClientOrderPrice.setText(md_fin_price);
        JP_StoreName.setText(store_name);

        Glide.with(ReviewActivity.this)
                .load(mdimg_thumbnail)
                .into(ClientOrderProdIMG);

        Star_1 = findViewById(R.id.Star_1);
        Star_2 = findViewById(R.id.Star_2);
        Star_3 = findViewById(R.id.Star_3);
        Star_4 = findViewById(R.id.Star_4);
        Star_5 = findViewById(R.id.Star_5);
        Star_1.setTag("true");
        Star_2.setTag("true");
        Star_3.setTag("true");
        Star_4.setTag("true");
        Star_5.setTag("true");

        Star_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Star_1.getTag().equals("false")) {
                    Star_2.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_3.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_4.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_5.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_2.setTag("true");
                    Star_3.setTag("true");
                    Star_4.setTag("true");
                    Star_5.setTag("true");
                } else {
                    Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_1.setTag("false");
                }
                star_count = 1;
            }
        });

        Star_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Star_2.getTag().equals("false")) {
                    Star_3.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_4.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_5.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_3.setTag("true");
                    Star_4.setTag("true");
                    Star_5.setTag("true");

                } else {
                    Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_1.setTag("false");
                    Star_2.setTag("false");
                }
                star_count = 2;
            }
        });

        Star_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Star_3.getTag().equals("false")) {
                    Star_4.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_5.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_4.setTag("true");
                    Star_5.setTag("true");
                } else {
                    Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_1.setTag("false");
                    Star_2.setTag("false");
                    Star_3.setTag("false");
                }
                star_count = 3;
            }
        });

        Star_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Star_4.getTag().equals("false")) {
                    Star_5.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_5.setTag("true");

                } else {
                    Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_4.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_1.setTag("false");
                    Star_2.setTag("false");
                    Star_3.setTag("false");
                    Star_4.setTag("false");
                }
                star_count = 4;
            }
        });

        Star_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Star_5.getTag().equals("false")) {
                    Star_5.setImageResource(R.drawable.ic_product_review_list_off_14px);
                    Star_5.setTag("true");
                    if (star_count != 0) {
                        star_count--;
                    }
                } else {
                    Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_4.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_5.setImageResource(R.drawable.ic_product_review_list_on_14px);
                    Star_1.setTag("false");
                    Star_2.setTag("false");
                    Star_3.setTag("false");
                    Star_4.setTag("false");
                    Star_5.setTag("false");
                    star_count = 5;
                }
            }
        });

        ImageButton addImg = (ImageButton) findViewById(R.id.addImg);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "리뷰 사진을 선택하세요"), PICTURE_REQUEST_CODE);
            }
        });

        reviewReg = findViewById(R.id.reviewReg);
        reviewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvw_content = findViewById(R.id.rvw_content);
                String content = rvw_content.getText().toString();
                String rvw_img1, rvw_img2, rvw_img3;
                JsonObject body = new JsonObject();
                body.addProperty("user_id", user_id);
                body.addProperty("order_id", order_id);
                body.addProperty("rvw_rating", star_count);
                body.addProperty("rvw_content", content);

                if (file1 != null) {
                    rvw_img1 = "review/" + file1.getName();
                    body.addProperty("rvw_img1", rvw_img1);
                    uploadWithTransferUtilty(file1.getName(), file1);
                } else {
                    rvw_img1 = "null";
                    body.addProperty("rvw_img1", rvw_img1);
                }

                if (file2 != null) {
                    rvw_img2 = "review/" + file2.getName();
                    body.addProperty("rvw_img2", rvw_img2);
                    uploadWithTransferUtilty(file2.getName(), file2);
                } else {
                    rvw_img2 = "null";
                    body.addProperty("rvw_img2", rvw_img2);
                }

                if (file3 != null) {
                    rvw_img3 = "review/" + file3.getName();
                    body.addProperty("rvw_img3", rvw_img3);
                    uploadWithTransferUtilty(file3.getName(), file3);
                } else {
                    rvw_img3 = "null";
                    body.addProperty("rvw_img3", rvw_img3);
                }

                //취소 버튼 클릭
                Button reviewCancel = findViewById(R.id.reviewCancel);
                reviewCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                //등록 버튼 클릭 시, 작성 유의사항 팝업
                reviewRegisterDialog = new ReviewRegisterDialog(ReviewActivity.this);
                reviewRegisterDialog.show();
                reviewRegisterDialog.findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //등록하기 버튼 눌러야 저장
                        Call<ResponseBody> call = service.review(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("review 저장 및 사진 업로드", response.toString());

                                if (response.isSuccessful()) {
                                    try {
                                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                        Toast.makeText(ReviewActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), ReviewMyDetailActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("md_name", md_name);
                                        intent.putExtra("md_qty", md_qty);
                                        intent.putExtra("md_fin_price", md_fin_price);
                                        intent.putExtra("store_name", store_name);
                                        intent.putExtra("order_id", order_id);
                                        intent.putExtra("store_loc", store_loc);
                                        intent.putExtra("mdimg_thumbnail", mdimg_thumbnail);
                                        intent.putExtra("rvw_content", content);
                                        String rvw_rating = String.valueOf(star_count);
                                        intent.putExtra("rvw_rating", rvw_rating);
                                        intent.putExtra("rvw_img1", rvw_img1);
                                        intent.putExtra("rvw_img2", rvw_img2);
                                        intent.putExtra("rvw_img3", rvw_img3);
                                        startActivity(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("실패", "failure.....");
                            }
                        });
                    }
                });
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
                                    file1 = new File(getPathFromUri(getImgUri));
                                    break;
                                case 1:
                                    reviewImg2.setImageURI(getImgUri);
                                    file2 = new File(getPathFromUri(getImgUri));
                                    break;
                                case 2:
                                    reviewImg3.setImageURI(getImgUri);
                                    file3 = new File(getPathFromUri(getImgUri));
                                    break;
                            }
                        }
                    }
                } else if (uri != null) {
                    reviewImg1.setImageURI(uri);
                    file1 = new File(String.valueOf(uri));
                }
            }
        }
    }

    public void uploadWithTransferUtilty(String fileName, File file) {

        AWSCredentials awsCredentials = new BasicAWSCredentials(getString(R.string.accessKey), getString(R.string.secretKey));
        AmazonS3Client s3Client = new AmazonS3Client(awsCredentials, Region.getRegion(Regions.AP_NORTHEAST_2));

        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(ReviewActivity.this).build();
        TransferNetworkLossHandler.getInstance(ReviewActivity.this);

        TransferObserver uploadObserver = transferUtility.upload("ggdjang/review", fileName, file);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
                    // Handle a completed upload
                    return;
                }
            }

            @Override
            public void onProgressChanged(int id, long current, long total) {
                int done = (int) (((double) current / total) * 100.0);
                Log.d(TAG, "사진을 저장 중입니다 " + done);
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.d(TAG, "사진 저장 오류 발생 EX:" + ex.toString());
            }
        });
    }

    // Function to convert a URI to a file path
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    @Override
    public void onBackPressed() {
        reviewCancelDialog = new ReviewCancelDialog(this);
        reviewCancelDialog.show();
        reviewCancelDialog.findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, OrderDetailActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("md_name", md_name);
                intent.putExtra("store_loc", store_loc);
                intent.putExtra("store_name", store_name);
                intent.putExtra("mdimg_thumbnail", mdimg_thumbnail);
                startActivity(intent);
            }
        });
    }
}