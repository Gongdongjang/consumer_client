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
import android.location.LocationManager;
import android.os.Bundle;

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

import com.example.consumer_client.Adapter.hamburger.JointPurchaseActivity;
import com.example.consumer_client.MdListMainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.homeRecycler.HomeProductAdapter;
import com.example.consumer_client.homeRecycler.HomeProductItem;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends Fragment implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    JsonParser jsonParser;

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
    String[] stNameL = new String[100];
    String[] mdNameL = new String[100];


    String[] farmNameL = new String[100];
    String[] payScheduleL = new String[100];
    String[] puStartL = new String[100];
    String[] puEndL = new String[100];

    List<List<String>> mdL = new ArrayList<>();

    //제품 가져올 때 필요한 변수
    int count;

    private TextView productList; //제품리스트 클릭하는 텍스트트
    private TextView change_address, home_userid;

    //userid!!
    String userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonParser = new JsonParser();


        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        userid=intent.getStringExtra("userid");
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

        //주소변경 누르면 주소등록 페이지로 (db에 저장된 주소 있으면 이전 주소 보여주는.. )
        change_address = view.findViewById(R.id.change_address);
//        change_address.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Log.d("클릭", "확인");
//                Intent intent = new Intent(mActivity, FindTownActivity.class);
//                intent.putExtra("userid",userid);
//                startActivity(intent);
//            }
//        });

        //유저아이디 띄우기
        home_userid = view.findViewById(R.id.home_userid);
        home_userid.setText("아이디:"+ userid);

        //==============
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



//        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
//        Call<MdGet> call = service.getMdMainData();
//        call.enqueue(new Callback<MdGet>() {
//            @Override
//            public void onResponse(Call<MdGet> call, Response<MdGet> response) {
//                try{
//                    MdGet result = response.body();
//                    count = Integer.parseInt(result.getCount());
//                    for (int i = 0; i < count; i++) {
//                        stNameL[i] = result.getSt_name().get(i).toString();
//                        mdNameL[i] = result.getMd_name().get(i).toString();
//                        farmNameL[i]=result.getFarm_name().get(i).toString();
//                        payScheduleL[i]= result.getPay_schedule().get(i).toString();
//                        puStartL[i]= result.getPu_start().get(i).toString();
//                        puEndL[i]= result.getPu_end().get(i).toString();
//                    }
//
//                    Toast.makeText(mActivity, "로딩중", Toast.LENGTH_SHORT).show();
//
//                    for(int i = 0; i < count; i++){
//                        List<String> mdInfo = new ArrayList<>();
//                        mdInfo.add(stNameL[i]); //0
//                        mdInfo.add(mdNameL[i]); //1
//                        mdInfo.add(farmNameL[i]); // 2농장이름
//                        mdInfo.add(payScheduleL[i]);  //3 결제 예정일
//                        mdInfo.add(puStartL[i]);  //4 픽업 시작 시점
//                        mdInfo.add(puEndL[i]); //5 픽업 끝나는 시점
//
//                        mdL.add(mdInfo);
//                    }
//                    Log.d("85행", mdL.toString());
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                    throw e;
//                }
//            }
//            @Override
//            public void onFailure(Call<MdGet> call, Throwable t) {
//                Toast.makeText(mActivity, "메인 제품리스트 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
//            }
//        });


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

                //메인제품리스트 리사이클러뷰 누르면 나오는
                mHomeProductAdapter.setOnItemClickListener(
                        new HomeProductAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                //Log.d("120행", farmL.get(pos).toString()); //클릭한 item 정보 보이기
                                Intent intent = new Intent(mActivity, JointPurchaseActivity.class);
//                                //배열로 보내고 싶은데..putExtra로 보내기
                                intent.putExtra("storeName", mdL.get(pos).get(0));
                                intent.putExtra("mdName",mdL.get(pos).get(1));
                                intent.putExtra("farmName",mdL.get(pos).get(2));
                                intent.putExtra("paySchedule",mdL.get(pos).get(3));
                                intent.putExtra("puStart",mdL.get(pos).get(4));
                                intent.putExtra("puEnd",mdL.get(pos).get(5));
                                startActivity(intent);
                            }
                        }
                );

            } }, 2000 ); // 1000 = 1초

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
