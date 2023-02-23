package com.example.consumer_client.address;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.network.NetworkStatus;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface FindTownService {
    @POST("register_address")
    Call<ResponseBody> addressRegister(@Body JsonObject body);
}

public class FindTownActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.POIItemEventListener, MapView.MapViewEventListener{

    FindTownService service;
    JsonParser jsonParser;

    // 주소 요청코드 상수 requestCode
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    TextView txt_address1,txt_address2,txt_address3, txt_address0;

    //주소1,2,3 위도경도 list
    List<String> addresslist=new ArrayList<>();

    MapPoint currentMapPoint;
    MapView mapView;
    ViewGroup mapViewContainer;
    MapPOIItem marker;
    String number;
    //카카오맵 위치
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(FindTownService.class);
        jsonParser = new JsonParser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_find_town);

        Intent intent = getIntent(); //intent 값 받기
        String userid=intent.getStringExtra("user_id");
        //처음실행이면 Tutorial에서 first_time 값이 yes이다.
        //메인화면들어가서 상단바 눌렀을때면 first_time 값을 no로 주기 (Home.java에서 설정하자)
        String first_time= intent.getStringExtra("first_time");
        //Log.d("68행_userid", userid);

        Button btn_addAdress = findViewById(R.id.btn_addAdress);
        Button btn_finish_address = findViewById(R.id.btn_finish_address);
        FloatingActionButton tracking_mode= findViewById(R.id.tracking_mode);

        txt_address0 = findViewById(R.id.txt_address0); //현재위치
        txt_address1= findViewById(R.id.txt_address1);
        txt_address2= findViewById(R.id.txt_address2);
        txt_address3=findViewById(R.id.txt_address3);


        //지도
        mapView = new MapView(this);
        mapView.setZoomLevel(1, true);
        mapView.zoomIn(true);
        mapView.zoomOut(true);
        mapViewContainer = (ViewGroup) findViewById(R.id.find_map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        marker = new MapPOIItem();
        marker.setDraggable(true);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();

        }

        //현재위치 탐색 버튼
        tracking_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                marker.setDraggable(true);
                //marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
                //marker.setMapPoint(currentMapPoint);
                //onCurrentLocationUpdate(mapView,currentMapPoint,1);
            }
        });

        JsonObject body = new JsonObject();
        body.addProperty("id", userid);
        body.addProperty("first_time",first_time);

        //주소추가 버튼 누르면 주소추가할 수 있는 액티비티로 이동
//        btn_addAdress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
//                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
//
//                    Log.i("주소설정페이지", "주소입력창 클릭");
//                    Intent intent = new Intent(getApplicationContext(), PlusAddressActivity.class);
//                    // 화면전환 애니메이션 없애기
//                    overridePendingTransition(0, 0);
//                    // 주소결과
//                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        txt_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent intent = new Intent(getApplicationContext(), PlusAddressActivity.class);
                    intent.putExtra("number","1");
                    //startActivity(intent);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent intent = new Intent(getApplicationContext(), PlusAddressActivity.class);
                    intent.putExtra("number","2");
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txt_address3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent intent = new Intent(getApplicationContext(), PlusAddressActivity.class);
                    intent.putExtra("number","3");
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
                if(addresslist.size()>0){
                    registAddress(userid, addresslist, first_time); //서버에 주소 저장
                }
                //메인 페이지로 이동
                Intent intent = new Intent(FindTownActivity.this, MainActivity.class);
                intent.putExtra("user_id",userid);
                startActivity(intent);
            }
        });

    }

    //선택한 도로명주소명 받아오기
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String str = "";
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("test", "onActivityResult");
        if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String data = intent.getExtras().getString("data");
                number = intent.getStringExtra("number");
                if (data != null) {
                    Log.i("test", "data:" + data);
                    Log.i("test", "number:" + number);
                    if(Objects.equals(number, "0")){   //맨처음 현재위치 받아올때 setText
                        txt_address0.setText(data);
                    }
                    else if (Objects.equals(number, "1")) {
                        txt_address1.setText(data);
                        str = txt_address1.getText().toString();
                    } else if (Objects.equals(number, "2")) {
                        txt_address2.setText(data);
                        str = txt_address2.getText().toString();
                    } else {
                        txt_address3.setText(data);
                        str = txt_address3.getText().toString();
                    }
                    //주소 리스트에 저장
                    addresslist.add(str);
                }
            }
        }
        //사용자가 GPS 활성 시켰는지 검사
        else if( requestCode==GPS_ENABLE_REQUEST_CODE) {
            Log.i("test2", "onActivityResult-elseif ");
            Log.i("test2", "number:" + number);
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {
                    Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                    number="0";
                    checkRunTimePermission();
                }
            }
        }
    }

    private void registAddress(String userid, List data, String first_time) {

        JsonObject body = new JsonObject();
        body.addProperty("id", userid);
        body.addProperty("address",data.toString());
        body.addProperty("first_time",first_time);

        Log.d("164행",body.toString());

        Call<ResponseBody> call = service.addressRegister(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        //Toast.makeText(FindTownActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FindTownActivity.this, "주소등록 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소등록", t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapViewContainer.removeAllViews();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음

            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                    Toast.makeText(FindTownActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(FindTownActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindTownActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
            //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[0])){
                Toast.makeText(this,"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }


        @Override
        public void onPOIItemSelected (MapView mapView, MapPOIItem mapPOIItem){

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched (MapView mapView, MapPOIItem mapPOIItem){

        }

        @Override
        public void onCalloutBalloonOfPOIItemTouched (MapView mapView, MapPOIItem
        mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType){

        }

        @Override
        public void onDraggablePOIItemMoved (MapView mapView, MapPOIItem mapPOIItem, MapPoint
        mapPoint){

        }

        @Override
        public void onCurrentLocationUpdate (MapView mapView, MapPoint mapPoint,
        float accuracyInMeters){
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.d("근처동네-현재위치", String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
            currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
            //이 좌표로 지도 중심 이동

            marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
            marker.setMapPoint(currentMapPoint);
            //mapView.setMapCenterPoint(currentMapPoint, true);

            //전역변수로 현재 좌표 저장
//        mCurrentLat = mapPointGeo.latitude;
//        mCurrentLng = mapPointGeo.longitude;
//        Log.d(TAG, "현재위치 => " + mCurrentLat + "  " + mCurrentLng);
//        //트래킹 모드가 아닌 단순 현재위치 업데이트일 경우, 한번만 위치 업데이트하고 트래킹을 중단시키기 위한 로직
//        if (!isTrackingMode) {
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
//        }

        }
        @Override
        public void onCurrentLocationDeviceHeadingUpdate (MapView mapView,float v){

        }

        @Override
        public void onCurrentLocationUpdateFailed (MapView mapView){
            //Log.i(TAG, "onCurrentLocationUpdateFailed");
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }

        @Override
        public void onCurrentLocationUpdateCancelled (MapView mapView){
            //Log.i(TAG, "onCurrentLocationUpdateCancelled");
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        }


        @Override
        public void onMapViewInitialized (MapView mapView){

        }

        @Override
        public void onMapViewCenterPointMoved (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewZoomLevelChanged (MapView mapView,int i){

        }

        @Override
        public void onMapViewSingleTapped (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewDoubleTapped (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewLongPressed (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewDragStarted (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewDragEnded (MapView mapView, MapPoint mapPoint){

        }

        @Override
        public void onMapViewMoveFinished (MapView mapView, MapPoint mapPoint){

        }
    }

