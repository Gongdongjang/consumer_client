package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;

public class RegisterData {
    @SerializedName("user_id")
    String user_id;

    @SerializedName("password")
    String password;

    @SerializedName("user_name")
    String user_name;

    @SerializedName("mobile_no")
    String mobile_no;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("gender")
    String gender;

//    @SerializedName("marketing_allow")
//    int marketing_allow; -> 마케팅 정보 수신이 문자 & 이메일로 나뉘는데 어떻게 처리?

    public RegisterData(String user_id, String password, String user_name, String mobile_no, String nickname, String gender){
        this.user_id = user_id;
        this.password = password;
        this.user_name = user_name;
        this.mobile_no = mobile_no;
        this.nickname = nickname;
        this.gender = gender;
    }
}
