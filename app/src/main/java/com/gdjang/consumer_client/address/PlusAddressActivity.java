package com.gdjang.consumer_client.address;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gdjang.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface DeleteTownService {
    @POST("delete_address")
    Call<ResponseBody> deleteAddress(@Body JsonObject body);
}

public class PlusAddressActivity extends AppCompatActivity {
    DeleteTownService service;
    JsonParser jsonParser;
    private WebView webView;
    String number, user_id;

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processDATA(String data) {
            Bundle extra = new Bundle();
            Intent intent = new Intent();
            extra.putString("data", data);
            intent.putExtras(extra);
            intent.putExtra("number", number);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DeleteTownService.class);
        jsonParser = new JsonParser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_plusaddress);

        Intent intent= getIntent();
        user_id = intent.getStringExtra("user_id");
        number = intent.getStringExtra("number");

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "Android");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        webView.loadUrl(getString(R.string.baseurl)+"post_search");

        Button delete_address= (Button) findViewById(R.id.delete_address);
        delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObject body = new JsonObject();
                body.addProperty("id", user_id);
                body.addProperty("number", number);  //주소 3개 중 번호

                Call<ResponseBody> call = service.deleteAddress(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                            if (Objects.equals(res.get("message").getAsString(), "기준주소지로 설정되어 주소 삭제 불가합니다.")){
                                Toast.makeText(getApplicationContext(), res.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }else if (Objects.equals(res.get("message").getAsString(), "처음 주소 등록시에는 주소 삭제 불가합니다.")){
                                Toast.makeText(getApplicationContext(), res.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "주소가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PlusAddressActivity.this, FindTownActivity.class);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });
    }

}

