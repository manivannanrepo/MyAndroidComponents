package com.mani.apps.myservieapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static com.mani.apps.myservieapp.MyServiceContract.TodoEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements ResultReceiverCallback, View.OnClickListener {
    private Button googleBT;
    private Button bingBT;
    private MyResultReceiver myResultReceiver;
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
        setContentView(R.layout.activity_main);

        Intent intent1 = new Intent(this, MyService.class);
        intent1.putExtra("url", "http://www.google.com");
        startService(intent1);


        googleBT = findViewById(R.id.google);
        bingBT = findViewById(R.id.bing);

        googleBT.setOnClickListener(this);
        bingBT.setOnClickListener(this);

        myResultReceiver = new MyResultReceiver(handler);
//        myResultReceiver.setCallBack(this);

        ContentValues contentValues = new ContentValues();

        contentValues.put(MyServiceContract.TodoEntry.COLUMN_DATE, 21051988);
        contentValues.put(MyServiceContract.TodoEntry.COLUMN_TASK, "Nothing");
        contentValues.put(MyServiceContract.TodoEntry.COLUMN_STATUS, "Waste");

        ContentResolver contentResolver = getContentResolver();
        MySqliteDBHelper mySqliteDBHelper = new MySqliteDBHelper(this);
        mySqliteDBHelper.insert(contentValues);
//        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);
//        Log.i("Inserted Row ", "" + uri.getAuthority());

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.i("Inserted Row ", " " + cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getString(3));
            }
            cursor.close();
        }
    }

    @Override
    public void onSuccess(String data) {
        Log.i("OnSuccess", data);
    }

    @Override
    public void onClick(View v) {
        if (v == googleBT) {
            Intent intent1 = new Intent(this, MyIntentService.class);
            intent1.putExtra("receiver", myResultReceiver);
            intent1.setAction("google");
            intent1.putExtra("url", "http://www.google.com");
            startService(intent1);
        } else if (v == bingBT) {
            Intent intent2 = new Intent(this, MyIntentService.class);
            intent2.putExtra("receiver", myResultReceiver);
            intent2.setAction("bing");
            intent2.putExtra("url", "http://www.bing.com");
            startService(intent2);
        }
    }
}
