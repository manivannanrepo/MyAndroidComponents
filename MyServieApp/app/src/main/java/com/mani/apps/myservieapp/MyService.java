package com.mani.apps.myservieapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyService extends Service {

    private String urlString;
    private IBinder iBinder = new MyBinder();
    private boolean isConnected;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service","onStart");
        Bundle bundle = intent.getExtras();
        urlString = bundle.getString("url");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    HttpURLConnection httpURLConnection = null;
                    try {
                        final URL url = new URL(urlString);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                        Log.i("data", inputStream.toString());
                        isConnected = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service","OnDestroy");
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("Service","onUnbind");
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Service","onBind");
        return iBinder;
    }

    public class MyBinder extends Binder{
        MyService getMyService(){
            return MyService.this;
        }
    }

    public boolean getConnectionStatus(){
        return isConnected;
    }

}
