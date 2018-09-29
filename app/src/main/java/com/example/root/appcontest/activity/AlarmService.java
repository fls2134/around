package com.example.root.appcontest.activity;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;

import com.example.root.appcontest.model.LocalData;
import com.nhn.android.maps.overlay.NMapPOIitem;

import java.util.ArrayList;

public class AlarmService extends Service {
    ArrayList<LocalData> data_input;
    public class AlarmServiceBinder extends Binder{
        AlarmService getService(){
            return AlarmService.this;
        }
    }

    private final IBinder mBinder = new AlarmServiceBinder();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public interface  ICallback{
        public ArrayList<LocalData> recvData();
    }

    private ICallback mCallback;

    public void registerCallback(ICallback cb){
        mCallback = cb;
    }

    public void myServiceFunc(){
        data_input = mCallback.recvData();
        //data_input의 정보들 중에서 취할 정보들을 처리, 알림할 것
    }
}
