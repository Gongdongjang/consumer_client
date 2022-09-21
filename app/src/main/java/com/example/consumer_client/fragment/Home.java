package com.example.consumer_client.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.ReviewDialog;
import com.example.consumer_client.address.EditTownActivity;
import com.example.consumer_client.address.FindTownActivity;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.md.MdListMainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.home.HomeProductAdapter;
import com.example.consumer_client.home.HomeProductItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface HomeService {
    @POST("standard_address/getStdAddress")
    Call<ResponseBody> getStdAddress(@Body JsonObject body);  //post user_id

    @GET("mdView_main")
    Call<ResponseBody> getMdMainData();
}

public class Home extends Fragment implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    JsonParser jsonParser;
    HomeService service;

    JsonObject res;
    JsonArray jsonArray;

    ArrayList<String> md_id_list = new ArrayList<String>();

    private View view;
    private RecyclerView mRecyclerView;
    private ArrayList<HomeProductItem> mList;
    private HomeProductAdapter mHomeProductAdapter;

    //카카오맵 위치
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    Activity mActivity;
    MapView mapView;
    ViewGroup mapViewContainer;
    LocationManager lm;
    MapPoint currentMapPoint;
    private double mCurrentLng; //Long = X, Lat = Yㅌ
    private double mCurrentLat;
    boolean isTrackingMode = false;

    private TextView productList; //제품리스트 클릭하는 텍스트트
    private TextView change_address, home_userid;

    String user_id;
    //String standard_address;
    Button popupBtn;
    private ReviewDialog reviewDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");

        // 맵뷰 초기화 및 추가
        //initMapView();
        //standard_address=intent.getStringExtra("standard_address");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(HomeService.class);
        jsonParser = new JsonParser();

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        mapView = new MapView(mActivity);

        //팝업테스트
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getActivity().getWindow().setAttributes(layoutParams);

        popupBtn = view.findViewById(R.id.PopupEx);
        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewDialog = new ReviewDialog(mActivity, "작성 중인 내용이 삭제됩니다.\n 리뷰 작성을 그만하시겠습니까?");
                reviewDialog.show();
            }
        });

        //제품리스트 누르면 제품리스트(메인) 화면으로
        productList = view.findViewById(R.id.productList);
        productList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, MdListMainActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //유저아이디 띄우기
//        home_userid = view.findViewById(R.id.home_userid);
//        home_userid.setText("아이디:"+ user_id);

        //product recyclerview 초기화
        firstInit();

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //=============기준위치 수정중================
//        if(standard_address=="현재위치"){
//            //현재위치 추적
            //mapView.setCurrentLocationEventListener(this);
//        }else{
//            //기준 설정한 위치로 설정
//            //Log.d("Home에서 주소",standard_address);
//            change_address.setText(standard_address);
//            final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());
//            List<Address> address= null;
//            Log.d("기준위치:",standard_address);
//            try {
//                address = geocoder.getFromLocationName(standard_address,10);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Address location = address.get(0);
//            double store_lat=location.getLatitude();
//            double store_long=location.getLongitude();
//            // 중심점 변경
//            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(store_lat, store_long), true);
//        }
//
//        if (!checkLocationServiceStatus(mActivity)){
//            showDialogForLocationServiceSetting();
//        }
//        else{
//            checkRunTimePermission();
//        }

        //===주소정보
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);

        Call<ResponseBody> call = service.getStdAddress(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    JsonArray addressArray = res.get("std_address_result").getAsJsonArray();  //json배열
                    String standard_address = addressArray.get(0).getAsJsonObject().get("standard_address").getAsString();
                    if(standard_address.equals("현재위치")){
                        //mapView.setCurrentLocationEventListener(this);
                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                        //mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOff );
                    }else{
                        mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOff );
                        final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());
                        List<Address> address = geocoder.getFromLocationName(standard_address,10);
                        Log.d("home제발..",standard_address);
                        Address location = address.get(0);
                        double my_lat=location.getLatitude();
                        double my_long=location.getLongitude();
                        // 중심점 변경
                        Log.d("home제발2..", String.valueOf(my_lat));
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(my_lat, my_long), true);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "기준 주소 정보 받기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });

        //=====상품 정보
        Call<ResponseBody> mdcall = service.getMdMainData();
        mdcall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    res =  (JsonObject) jsonParser.parse(response.body().string());
                    jsonArray = res.get("md_result").getAsJsonArray();

                    //어뎁터 적용
                    mHomeProductAdapter = new HomeProductAdapter(mList);
                    mRecyclerView.setAdapter(mHomeProductAdapter);

                    //가로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

                    //mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading );

                    for(int i=0;i<jsonArray.size();i++){
                        md_id_list.add(jsonArray.get(i).getAsJsonObject().get("md_id").getAsString());

                        addItem("product Img",
                                jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("md_name").getAsString()
                        );
                    }
                    //메인제품리스트 리사이클러뷰 누르면 나오는
                    mHomeProductAdapter.setOnItemClickListener(
                            new HomeProductAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, JointPurchaseActivity.class);
                                    intent.putExtra("md_id", md_id_list.get(pos));
                                    intent.putExtra("user_id", user_id);
                                    startActivity(intent);
                                }
                            }
                    );
                }
                catch(Exception e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "메인 제품리스트 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        lm = (LocationManager) mActivity.getApplicationContext().getSystemService( Context.LOCATION_SERVICE );

        //전체 fragment home return
        return view;
    }

    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }
    //홈화면 제품리스트
    public void firstInit(){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homeStore);
        mList = new ArrayList<>();
    }

    public void addItem(String imgName, String mainText, String subText){
        HomeProductItem item = new HomeProductItem();

        item.setHomeProdImg(imgName);
        item.setHomeProdName(mainText);
        item.setHomeProdEx(subText);

        mList.add(item);
    }

    // 현재 위치 업데이트 setCurrentLocationEventListener
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        Log.i(TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
        currentMapPoint = MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude);
        //이 좌표로 지도 중심 이동
        mapView.setMapCenterPoint(currentMapPoint, true);
        //전역변수로 현재 좌표 저장
        mCurrentLat = mapPointGeo.latitude;
        mCurrentLng = mapPointGeo.longitude;
        Log.d(TAG, "현재위치 => " + mCurrentLat + "  " + mCurrentLng);
        //트래킹 모드가 아닌 단순 현재위치 업데이트일 경우, 한번만 위치 업데이트하고 트래킹을 중단시키기 위한 로직
        if (!isTrackingMode) {
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateFailed");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateCancelled");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(mActivity,Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED){
            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,REQUIRED_PERMISSIONS[0])){
                Toast.makeText(mActivity,"이 앱을 실행하려면 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(mActivity,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(mActivity,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    // GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent,GPS_ENABLE_REQUEST_CODE);
            }
        }); //여기밑에 setNaviveButton 추가함
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private boolean checkLocationServiceStatus(Activity mActivity) {

        LocationManager locationManager = (LocationManager)mActivity.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {

    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {

    }
}
