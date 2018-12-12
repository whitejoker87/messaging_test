package com.google.firebase.quickstart.fcm;

import com.google.firebase.quickstart.fcm.model.RequestAuth;
import com.google.firebase.quickstart.fcm.model.ResponseAuth;
import com.google.firebase.quickstart.fcm.model.ResponseUserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ILoginAPI {
        @Headers("Content-Type: application/json")
        @POST("account/login")
        Call<ResponseAuth> getAuth(@Body RequestAuth requestAuth);

        //@Headers("Authorization: Bearer {token}")
        @GET("account/getinfo")
        Call<ResponseUserInfo> getUserInfo(@Header("Authorization") String token);
        //Call<ResponseUserInfo> getUserInfo(@Path("token") String token);

}
