package com.example.consumer_client.fragment;

import static com.example.consumer_client.address.LocationDistance.distance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.CustomSpinnerAdapter;
import com.example.consumer_client.FragPagerAdapter;
import com.example.consumer_client.review.ReviewCancelDialog;
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.address.EditTownActivity;
import com.example.consumer_client.address.FindTownActivity;
import com.example.consumer_client.alarm.Alarm;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.md.MdListMainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.home.HomeProductAdapter;
import com.example.consumer_client.home.HomeProductItem;
import com.example.consumer_client.my_town.StoreMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
    @POST("get_address")
    Call<ResponseBody> addressInfo(@Body JsonObject body);  //post user_id

    @POST("standard_address/register")
    Call<ResponseBody> postStdAddress(@Body JsonObject body);  //post user_id,standard_address

    @GET("mdView_main")
    Call<ResponseBody> getMdMainData();
}

public class Home extends Fragment {

    JsonParser jsonParser;
    HomeService service;

    JsonObject res;
    JsonArray jsonArray, pu_start, dDay, addressArray;

    private View view;
    private RecyclerView mRecyclerView;
    private ArrayList<HomeProductItem> mList;
    private HomeProductAdapter mHomeProductAdapter;

    Activity mActivity;
    LocationManager lm;

    double myTownLat;   //추가
    double myTownLong;  //추가

    private TextView productList; //제품리스트 클릭하는 텍스트트
    private ImageView toolbar_cart, toolbar_notification;

    String user_id;
    String address;
    private ReviewCancelDialog reviewCancelDialog;

    private List<String> list = new ArrayList<>();
    private Spinner spinner;
    private CustomSpinnerAdapter adapter;
    private String selectedItem;
    int address_count;
    //스피터 반복호출 막기
    private boolean  isFirstSelected = true; // 전역변수로 선언

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");

        if(getArguments() != null){
            user_id = getArguments().getString("user_id"); //값을 받아옴
        }
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
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //product recyclerview 초기화
        firstInit();

        //상단바 주소변경 누르면 주소변경/선택 페이지로
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);

        Call<ResponseBody> call = service.addressInfo(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    addressArray = res.get("address_result").getAsJsonArray();  //json배열
                    Log.d("근처동네", String.valueOf(addressArray));
                    address_count = res.get("address_count").getAsInt();
                    address= addressArray.get(0).getAsJsonObject().get("standard_address").getAsString();
                    final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());
                    List<Address> address = geocoder.getFromLocationName(addressArray.get(0).getAsJsonObject().get("standard_address").getAsString(), 10);
                    Address location = address.get(0);
                    myTownLat = location.getLatitude();
                    myTownLong = location.getLongitude();

                    Log.d("근처동네", String.valueOf(address_count));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                spinner = view.findViewById(R.id.change_address);
                Log.d("근처동네", String.valueOf(addressArray));

                //사용자가 등록한 주소 불러오기
                if (address_count==0){
                    list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                    list.add("내 동네 설정하기");
                }
                else if (address_count == 1) {
                    if(Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc0").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                    }else{
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                    }
                    list.add("내 동네 설정하기");
                } else if (address_count == 2) {
                    if(Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc0").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                    }else if (Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc1").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                    }else{
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                    }
                    list.add("내 동네 설정하기");
                } else if (address_count == 3) {
                    if(Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc0").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc3").getAsString());
                    }else if (Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc1").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc3").getAsString());
                    }else if (Objects.equals(address, addressArray.get(0).getAsJsonObject().get("loc2").getAsString())){
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc3").getAsString());
                    }else{
                        list.add(addressArray.get(0).getAsJsonObject().get("loc3").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc0").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc1").getAsString());
                        list.add(addressArray.get(0).getAsJsonObject().get("loc2").getAsString());
                    }
                    list.add("내 동네 설정하기");
                }

                // 스피너에 붙일 어댑터 초기화
                adapter = new CustomSpinnerAdapter(getContext(), list);
                spinner.setAdapter(adapter);

                // 스피너 클릭 리스너
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // 어댑터에서 정의한 메서드를 통해 스피너에서 선택한 아이템의 이름을 받아온다

                        if (isFirstSelected) {
                            isFirstSelected = false;
                        } else {
                            // 로직
                            selectedItem = adapter.getItem();
                            //Toast.makeText(mActivity, "선택한 주소 : " + selectedItem, Toast.LENGTH_SHORT).show();

                            if(Objects.equals(selectedItem, "내 동네 설정하기")){
                                Intent intent = new Intent(mActivity, FindTownActivity.class);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);
                            } else {
                                Log.d("근처동네 246", "여기오니//?");
                                postStdAddress2(user_id, selectedItem);
                                isFirstSelected=true;
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        //
                    }
                });

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "주소정보 받아오기 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });

        //상단바 알림
        toolbar_notification= view.findViewById(R.id.toolbar_notification);
        toolbar_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, Alarm.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //상단바 장바구니
        toolbar_cart = view.findViewById(R.id.toolbar_cart);
        toolbar_cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //제품리스트 누르면 제품리스트(메인) 화면으로
        productList = view.findViewById(R.id.productList);
        productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MdListMainActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", address);
                startActivity(intent);
            }
        });

        //우리동네 공동구매 지도로 보기 로 이동.
        ImageView gotoMap = view.findViewById(R.id.gotoMap);
        gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, StoreMap.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", address);
                startActivity(intent);
            }
        });

        //=====상품 정보
        Call<ResponseBody> mdcall = service.getMdMainData();
        mdcall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());
                    jsonArray = res.get("md_result").getAsJsonArray();
                    pu_start = res.get("pu_start").getAsJsonArray();
                    dDay = res.get("dDay").getAsJsonArray();

                    //어뎁터 적용
                    mHomeProductAdapter = new HomeProductAdapter(mList);
                    mRecyclerView.setAdapter(mHomeProductAdapter);

                    //가로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

                    final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());

                    for (int i = 0; i < jsonArray.size(); i++) {
                        List<Address> address = geocoder.getFromLocationName(jsonArray.get(i).getAsJsonObject().get("store_loc").getAsString(), 8);
                        Address location = address.get(0);
                        double store_lat = location.getLatitude();
                        double store_long = location.getLongitude();

                        //자신이 설정한 위치와 스토어 거리 distance 구하기
                        double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");

                        if (Double.compare(1, distanceKilo) > 0) { //4km 이내 제품들만 보이기
                            //(스토어 데이터가 많이 없으므로 0.4대신 1로 test 중, 기능은 완료)

                            String realIf0;
                            if (dDay.get(i).getAsString().equals("0")) realIf0 = "D - day";
                            else if(dDay.get(i).getAsInt() < 0) realIf0 = "D + "+ Math.abs(dDay.get(i).getAsInt());
                            else realIf0 = "D - " + dDay.get(i).getAsString();

                            addItem(jsonArray.get(i).getAsJsonObject().get("md_id").getAsString(),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    String.format("%.2f", distanceKilo), //+"km",
                                    jsonArray.get(i).getAsJsonObject().get("pay_price").getAsString(),
                                    realIf0,
                                    pu_start.get(i).getAsString()
                            );
                        }
                    }

                    //거리 가까운순으로 정렬
                    mList.sort(new Comparator<HomeProductItem>() {
                        @Override
                        public int compare(HomeProductItem o1, HomeProductItem o2) {
                            int ret;
                            Double distance1 = Double.valueOf(o1.getHomeDistance().substring(o1.getHomeDistance().length() - 2));
                            Double distance2 = Double.valueOf(o2.getHomeDistance().substring(o2.getHomeDistance().length() - 2));
                            //거리비교
                            ret = distance1.compareTo(distance2);
                            Log.d("ret", String.valueOf(distance1));
                            return ret;
                        }
                    });

                    //메인제품리스트 리사이클러뷰 누르면 나오는
                    mHomeProductAdapter.setOnItemClickListener(
                            new HomeProductAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, JointPurchaseActivity.class);
                                    intent.putExtra("md_id", mList.get(pos).getHomeMdId()); //md_id 넘기기
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("standard_address", address);
                                    startActivity(intent);
                                }
                            }
                    );
                } catch (Exception e) {
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

        lm = (LocationManager) mActivity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        setInit(); //뷰페이저2 실행 메서드
        //전체 fragment home return
        return view;
    }

    //기준주소 등록하기
    void postStdAddress2(String user_id, String address){
        Log.d("근처동네 452", "여기오니//?");
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);
        body.addProperty("standard_address", address);  //기준 주소

        Call<ResponseBody> call = service.postStdAddress(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //뷰페이저2 실행 메서드
    private void setInit() {
        ViewPager2 viewPageSetUp = view.findViewById(R.id.viewPager2);
        FragPagerAdapter SetPagerAdapter = new FragPagerAdapter(getActivity());
        viewPageSetUp.setAdapter(SetPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(2); //페이지 한계 지정 개수
        viewPageSetUp.setCurrentItem(1000); //무한처럼 보이도록 하려고

        //페이지끼리 간격
        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        //final float pageMaring=(float) getResources().getDimensionPixelOffset;
        //페이지 보이는 정도
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset);
        //final float pageOffset=(float) getResources().getDimensionPixelOffset(2;
        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * -(2 * pageOffset + pageMargin);
                if (-1 > position) {
                    page.setTranslationX(-offset);
                } else if (1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });
    }

    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    //홈화면 제품리스트
    public void firstInit() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.homeStore);
        mList = new ArrayList<>();
    }

    public void addItem(String md_id, String imgName, String mainText, String subText, String distanceKilo, String mdPrice, String dDay, String puTime) {
        HomeProductItem item = new HomeProductItem();

        item.setHomeMdId(md_id);
        item.setHomeProdImg(imgName);
        item.setHomeProdName(mainText);
        item.setHomeProdEx(subText);
        item.setHomeDistance(String.valueOf(distanceKilo));
        item.setHomeMdPrice(mdPrice);
        item.setHomeDday(dDay);
        item.setHomePuTime(puTime);

        mList.add(item);
    }
}
