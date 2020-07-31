package com.hkl.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {
    int mRequestCode = 100;
    final int REQ_CODE_PIC = 1000;
    final int REQ_CODE_VID = 2000;
    final String TAG = getClass().getSimpleName();
    int current_hide_mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "Activity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTakePicture = (Button) findViewById(R.id.btn_take_picture);
        Button btnRecordVideo = (Button) findViewById(R.id.btn_record_video);
        Button btnHideNav = (Button) findViewById(R.id.btn_hide_nav);
        Button btnHideSticky = (Button) findViewById(R.id.btn_immersive_sticky);
        Button btnShowNav = (Button) findViewById(R.id.btn_show_nav);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*recordVideo();*/
                finish();
            }
        });
        btnHideNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideNav(false);
            }
        });
        btnHideSticky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideNav(true);
            }
        });
        btnShowNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSttTtsDemo();
//                notificationDialog();
//                finish();
                /*showNav();*/
            }
        });
//        setAlarm(getApplicationContext());
        getLifecycle().addObserver(new LifeCycleComponentExt());
        getLifecycle().addObserver(this);

    }
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager)       getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setAutoCancel(false).setOngoing(true)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setWhen(0)
                .setLocalOnly(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setTicker("Tutorialspoint")
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("sample notification")
                .setContentText("This is sample notification")
                .setContentInfo("Information");
//        notificationManager.notify(1, notificationBuilder.build());
        nm.notify(12348765, notificationBuilder.build());
    }
    int INTERVAL = 3000;
    public void setAlarm(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 100, intent, 0);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                INTERVAL, alarmIntent);
    }
    @Override
    public void onResume() {
        Log.e(TAG, "Activity onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e(TAG, "Activity onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Activity onDestroy");
        super.onDestroy();
    }
        /**
         * Calls {@link WebView#destroy()} on the player. And unregisters the broadcast receiver (for network events), if registered.
         * Call this method before destroying the host Fragment/Activity, or register this View as an observer of its host lifcecycle
         */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDes() {
        Log.e(TAG, "onDes OnLifecycleEvent ON_DESTROY");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onSto() {
        Log.e(TAG, "onSto OnLifecycleEvent ON_STOP");
    }

    String filePath;
    private void takePicture() {
        String storagePath = Environment.getExternalStorageDirectory().getPath();
        filePath = storagePath + "/test_take_picture2.jpg";
        Uri photoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(filePath));
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(i, REQ_CODE_PIC);
    }
    private void recordVideo() {
        String storagePath = Environment.getExternalStorageDirectory().getPath();
        filePath = storagePath + "/test_record_video2.mp4";
        Uri videoURI = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(filePath));
        Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, videoURI);
//        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(i, REQ_CODE_VID);
    }
    int currentApiVersion = android.os.Build.VERSION.SDK_INT;

    private void hideNav(final boolean sticky) {

        final int flags =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                ;
        final int flags_sticky =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                ;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            if(sticky) {
                current_hide_mode = 2;
                getWindow().getDecorView().setSystemUiVisibility(flags_sticky);
            } else {
                current_hide_mode = 1;
                getWindow().getDecorView().setSystemUiVisibility(flags);
            }

            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        if(sticky) {
                            current_hide_mode = 2;
                            decorView.setSystemUiVisibility(flags_sticky);
                        } else {
                            current_hide_mode = 1;
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                }
            });
        }

    }
    private void showNav() {

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                ;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);
            current_hide_mode = 0;
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            if(current_hide_mode == 1) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            } else if(current_hide_mode == 2) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode,resultCode,intent);
        Log.e(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode + " filePath:" + filePath);
    }

    private void startSttTtsDemo() {
        Intent intent = new Intent(this, sg_tester.MainActivity.class);
        startActivity(intent);
    }
}

