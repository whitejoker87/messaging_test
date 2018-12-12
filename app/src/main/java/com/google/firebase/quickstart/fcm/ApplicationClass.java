package com.google.firebase.quickstart.fcm;

import android.app.Application;

import com.google.firebase.quickstart.fcm.model.ResponseAuth;
import com.onesignal.OneSignal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationClass extends Application {

    public static String TOKEN;
    public static String USERID;

    private static ILoginAPI apiLogin;

    private static ResponseAuth responseAuth;

    //синглтон-оъбект с инфой из JSON
    public static synchronized ResponseAuth getResponseAuth() {
        if (responseAuth == null) {
            responseAuth = new ResponseAuth();
        }
        return responseAuth;
    }

    public static synchronized void setResponseAuth(ResponseAuth responseAuth) {
        ApplicationClass.responseAuth = responseAuth;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //OneSignal.setEmail("example@domain.com");
        //OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        //USER_ID = status.getSubscriptionStatus().getUserId();

        Retrofit retrofit = new Retrofit.Builder()//запрос для загрузки JSON
                .baseUrl("https://easycar.su/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiLogin = retrofit.create(ILoginAPI.class);

    }

    //интерфейс для загрузки JSON
    public static ILoginAPI getApiLogin() {
        return apiLogin;
    }

}
