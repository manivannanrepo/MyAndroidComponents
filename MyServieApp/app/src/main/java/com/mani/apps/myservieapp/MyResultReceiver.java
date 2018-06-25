package com.mani.apps.myservieapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.ResultReceiver;

public class MyResultReceiver extends ResultReceiver implements Parcelable{
    private ResultReceiverCallback resultReceiverCallback;
    private Handler handler;

    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public MyResultReceiver(Handler handler) {
        super(handler);
        this.handler = handler;
    }


    public void setCallBack(ResultReceiverCallback resultReceiverCallback){
        this.resultReceiverCallback = resultReceiverCallback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        Message message = new Message();
        resultData.putInt("code",resultCode);
        message.setData(resultData);
        handler.sendMessageAtTime(message,1000);
//        resultReceiverCallback.onSuccess(resultData.getString("result"));
    }
}
