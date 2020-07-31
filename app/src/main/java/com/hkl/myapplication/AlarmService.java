package com.hkl.myapplication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmService extends IntentService {

    private static final String TAG = AlarmService.class.getSimpleName();

    private Context mContext;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (mContext == null) {
            mContext = getApplicationContext();
        }
        Log.e(TAG, "action:" + intent.getAction());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}

