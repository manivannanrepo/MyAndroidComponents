package com.mani.apps.myservieapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * Used to name the worker thread, important only for debugging.
     */
    public MyIntentService() {
        super(MyIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        ResultReceiver myResultReceiver = intent.getExtras().getParcelable("receiver");

        String action = intent != null ? intent.getAction() : "";
        if (action.equals("google")) {
            Bundle bundle = intent.getExtras();
            String urlString = bundle.getString("url");
            Log.i("intent service",urlString );
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Log.i("data", inputStream.toString());
                Bundle bundle1 = new Bundle();
                bundle1.putString("result","google success");
                myResultReceiver.send(1,bundle1);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }else if (action.equals("bing")){
            Bundle bundle = intent.getExtras();
            String urlString = bundle.getString("url");
            Log.i("intent service",urlString );
            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                Log.i("data", inputStream.toString());
                Bundle bundle1 = new Bundle();
                bundle1.putString("result","bing success");
                myResultReceiver.send(2,bundle1);
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("intent service","onstartcommand" );
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("intent service","ondestroy" );
    }
}
