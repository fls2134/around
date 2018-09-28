package com.example.root.appcontest.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class AlarmService extends Thread {
    Handler handler;
    boolean isRun = true;

    public AlarmService(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this){
            this.isRun = false;
        }
    }

    public void run(){

        while(isRun){

        }
    }

}
