package com.example.consumer_client.user;

import android.app.Application;

import com.example.consumer_client.R;
import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    private static KakaoApplication instance;

    @Override public void onCreate() {
        super.onCreate();
        instance=this;

        // @brief : kakao 네이티브 앱키로 초기화
        String kakao_app_key = getResources().getString(R.string.kakao_app_key);
        KakaoSdk.init(this,kakao_app_key );
    }
}


