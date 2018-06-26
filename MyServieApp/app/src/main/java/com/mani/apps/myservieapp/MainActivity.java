package com.mani.apps.myservieapp;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.mani.apps.myservieapp.MyServiceContract.TodoEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements ResultReceiverCallback, View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    Intent serviceIntent;
    private Button googleBT;
    private Button bingBT;
    private MyResultReceiver myResultReceiver;
    private ServiceConnection serviceConnection;
    private MyService myService;
    private boolean bound;
    // Result Receiver will post the message as soon as the task is completed.
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int code = bundle.getInt("code");
            String data = bundle.getString("result");

            if (code == 1) {
                googleBT.setText("" + data);
                googleBT.setOnClickListener(null);
            } else if (code == 2) {
                bingBT.setText("" + data);
                bingBT.setOnClickListener(null);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra("url", "http://www.google.com");


        googleBT = findViewById(R.id.google);
        bingBT = findViewById(R.id.bing);

        googleBT.setOnClickListener(this);
        bingBT.setOnClickListener(this);

        // Result Reciver to get the result of IntentService.
        myResultReceiver = new MyResultReceiver(handler);
        // ResultReciever can update result to the activity in two ways .
        // One is callback.
        // another method to get the result from result reciver is to post message to the handler.refer the handler ablove.

//        myResultReceiver.setCallBack(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MyServiceContract.TodoEntry.COLUMN_DATE, 21051988);
        contentValues.put(MyServiceContract.TodoEntry.COLUMN_TASK, "Nothing");
        contentValues.put(MyServiceContract.TodoEntry.COLUMN_STATUS, "Waste");

        ContentResolver contentResolver = getContentResolver();

        //Insert the row into DB - Same App
        MySqliteDBHelper mySqliteDBHelper = new MySqliteDBHelper(this);
        mySqliteDBHelper.insert(contentValues);

        //Access Using ContentResolver like another app.
        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);
        Log.i(TAG, "Inserted Row " + uri.getAuthority());

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.i(TAG, "Inserted Row  " + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
            }
            cursor.close();
        }


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;
                myService = myBinder.getMyService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
                myService.stopSelf();
            }
        };

        startService(serviceIntent);
    }

    @Override
    public void onSuccess(String data) {
        Log.i(TAG, "OnSuccess " + data);
    }

    @Override
    public void onClick(View v) {
        if (v == googleBT) {
            if (bound) {
                googleBT.setText(String.format("Google %s", myService.getConnectionStatus()));
                Log.i(TAG, "service bound " + myService.getConnectionStatus());
            }
        } else if (v == bingBT) {
            Intent intent2 = new Intent(this, MyIntentService.class);
            intent2.putExtra("receiver", myResultReceiver);
            intent2.setAction("bing");
            intent2.putExtra("url", "http://www.bing.com");
            startService(intent2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        unbindService(serviceConnection);
    }
}
