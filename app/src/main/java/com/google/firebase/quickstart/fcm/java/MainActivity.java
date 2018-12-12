/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.fcm.java;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.quickstart.fcm.ApplicationClass;
import com.google.firebase.quickstart.fcm.R;
import com.google.firebase.quickstart.fcm.model.RequestAuth;
import com.google.firebase.quickstart.fcm.model.ResponseAuth;
import com.google.firebase.quickstart.fcm.model.ResponseUserInfo;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
//        if (getIntent().getExtras() != null) {
//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
//            }
//        }
        // [END handle_data_extras]

//        Button subscribeButton = findViewById(R.id.subscribeButton);
//        subscribeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "Subscribing to weather topic");
//                // [START subscribe_topics]
//                FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                String msg = getString(R.string.msg_subscribed);
//                                if (!task.isSuccessful()) {
//                                    msg = getString(R.string.msg_subscribe_failed);
//                                }
//                                Log.d(TAG, msg);
//                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                // [END subscribe_topics]
//            }
//        });

//        Button logTokenButton = findViewById(R.id.logTokenButton);
//        logTokenButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get token
//                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                                if (!task.isSuccessful()) {
//                                    Log.w(TAG, "getInstanceId failed", task.getException());
//                                    return;
//                                }
//
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//
//                                // Log and toast
//                                String msg = getString(R.string.msg_token_fmt, token);
//                                Log.d(TAG, msg);
//                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//
//            }
//        });
        final EditText et_email = findViewById(R.id.et_login);
        final EditText et_pass = findViewById(R.id.et_pass);
    Button btnGo = findViewById(R.id.btn_go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(setRequestAuth(et_email.getText().toString(), et_pass.getText().toString()));
            }
        });
    }

    public void login(RequestAuth requestAuth) {
        ApplicationClass.getApiLogin().getAuth(requestAuth).enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("Ok")) {
                            getUserInfo(" Bearer " + response.body().getToken());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {

            }
        });
    }

    RequestAuth setRequestAuth(String email, String password) {
        RequestAuth emailAndPass = new RequestAuth();
        emailAndPass.setEmail(email);
        emailAndPass.setPassword(password);
        return emailAndPass;
    }



    private void getUserInfo(String token) {
        ApplicationClass.TOKEN = token;
        ApplicationClass.getApiLogin().getUserInfo(token).enqueue(new Callback<ResponseUserInfo>() {
            @Override
            public void onResponse(Call<ResponseUserInfo> call, Response<ResponseUserInfo> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("Ok")) {
                            ApplicationClass.USERID = response.body().getData().getId();
                            OneSignal.sendTag("UserId", response.body().getData().getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUserInfo> call, Throwable t) {

            }
        });
    }
}
