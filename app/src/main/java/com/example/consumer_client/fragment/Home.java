package com.example.consumer_client.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.Adapter.hamburger.StoreActivity;
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.MdGet;
import com.example.consumer_client.MdListMainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.StoreGet;
import com.example.consumer_client.homeRecycler.HomeProductAdapter;
import com.example.consumer_client.homeRecycler.HomeProductItem;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;
import com.google.android.gms.common.internal.Constants;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

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
    String[] stNameL = new String[30];
    String[] mdNameL = new String[30];
    List<List<String>> mdL = new ArrayList<>();
    int count;
    private TextView productList; //제품리스트 클릭하는 텍스트트

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);

        mapView = new MapView(mActivity);

        //제품리스트 누르면 제품리스트(메인) 화면으로
        productList = view.findViewById(R.id.productList);
        productList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("클릭", "확인");
                Intent intent = new Intent(mActivity, MdListMainActivity.class);
                startActivity(intent);
            }
        });

        //product recyclerview 초기화
        firstInit();

        mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationEventListener(this);

        if (!checkLocationServiceStatus(mActivity)){
            showDialogForLocationServiceSetting();
        }
        else{
            checkRunTimePermission();
        }

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<MdGet> call = service.getMdMainData();
        call.enqueue(new Callback<MdGet>() {
            @Override
            public void onResponse(Call<MdGet> call, Response<MdGet> response) {
                try{
                    MdGet result = response.body();
                    count = Integer.parseInt(result.getCount());
                    for (int i = 0; i < count; i++) {
                        stNameL[i] = result.getSt_name().get(i).toString();
                        mdNameL[i] = result.getMd_name().get(i).toString();
                    }

                    Toast.makeText(mActivity, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> mdInfo = new ArrayList<>();
                        mdInfo.add(stNameL[i]);
                        mdInfo.add(mdNameL[i]);
                        mdL.add(mdInfo);
                    }
                    Log.d("85행", mdL.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<MdGet> call, Throwable t) {
                Toast.makeText(mActivity, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 3초 후에 현재위치를 받아오도록 설정 , 바로 시작 시 에러남

                //어뎁터 적용
                mHomeProductAdapter = new HomeProductAdapter(mList);
                mRecyclerView.setAdapter(mHomeProductAdapter);

                //가로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);

                mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading );

                //추후에 제품 이름 가져올 예정
                for(int i=0;i<count;i++){
                    addItem("product Img", stNameL[i], mdNameL[i]);
                }
            } }, 1000 ); // 1000 = 1초
        lm = (LocationManager) mActivity.getApplicationContext().getSystemService( Context.LOCATION_SERVICE );


        //전체 fragment home return
        return view;
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

