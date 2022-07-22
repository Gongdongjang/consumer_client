package com.example.consumer_client.address;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.PlusAddressActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.user.network.NetworkStatus;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindTownActivity extends AppCompatActivity {

    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    TextView txt_address1,txt_address2,txt_address3;

    //주소1,2,3 위도경도 list
    List<Address> addressList=new ArrayList<>();
    List<Double> latlongList=new ArrayList<>();

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_find_town);

        Button btn_addAdress = findViewById(R.id.btn_addAdress);
        Button btn_finish_address = findViewById(R.id.btn_finish_address);

        txt_address1= findViewById(R.id.txt_address1);
        txt_address2= findViewById(R.id.txt_address2);
        txt_address3=findViewById(R.id.txt_address3);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //주소추가 버튼 누르면 주소추가할 수 있는 액티비티로 이동
        btn_addAdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent intent = new Intent(getApplicationContext(), PlusAddressActivity.class);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //선택한 동네로 설정 버튼 누르면 서버db에 저장하기 위해 통신.
        btn_finish_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registAddress(new AddressData(latlongList));
            }
        });

    }

    //선택한 도로명주소명 받아오기
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //지오코더 선언과 초기화
        final Geocoder geocoder = new Geocoder(getApplicationContext());
        String str;
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("test", "onActivityResult");
        if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String data = intent.getExtras().getString("data");
                if (data != null) {
                    Log.i("test", "data:" + data);
                    if (txt_address1.getText().toString().equals("주소1")) {
                        txt_address1.setText(data);
                        str = txt_address1.getText().toString();
                    } else if (txt_address2.getText().toString().equals("주소2")) {
                        txt_address2.setText(data);
                        str = txt_address2.getText().toString();
                    } else {
                        txt_address3.setText(data);
                        str = txt_address3.getText().toString();
                    }

                    try {
                        addressList = geocoder.getFromLocationName(str, 20);
                        Log.d("addressList1", String.valueOf(addressList));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    double lat = address.getLatitude();
                    double lon = address.getLongitude();
                    //위도경도 리스트에 저장
                    latlongList.add(lat);
                    latlongList.add(lon);

                    Log.d("addressList_lat", String.valueOf(lat));
                    Log.d("addressList_lon", String.valueOf(lon));
                    Log.d("addressList_lon", String.valueOf(latlongList));
                }
            }
        }
    }

    private void registAddress(AddressData data) {
        service.addressRegister(data).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                AddressResponse result = response.body();
                Toast.makeText(FindTownActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    //선택한 동네로 설정 버튼 클릭시, 메인 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                Toast.makeText(FindTownActivity.this, "주소등록 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소등록 에러 발생", t.getMessage());
            }
        });
    }

}
