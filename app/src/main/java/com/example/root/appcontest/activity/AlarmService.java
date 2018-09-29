package com.example.root.appcontest.activity;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.root.appcontest.model.LocalData;
import com.nhn.android.maps.overlay.NMapPOIitem;

import java.util.ArrayList;

public class AlarmService extends Service {
    ArrayList<LocalData> data_input;
    LocationManager locationManager;
    LocationListener listener;

    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

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


    public void addLocListener() {
        listener = new LocationListener(LocationManager.GPS_PROVIDER);
        listener.start();
    }

    private void removeLocListener() {
        locationManager.removeUpdates(listener);
    }


    private class LocationListener extends Thread implements android.location.LocationListener
    {
        Location mLastLocation;
        private String mProvider = null;
        private Handler mHandler = null;

        public LocationListener(String provider)
        {
            Log.e("asd", "LocationListener " + provider);
            mProvider = provider;
            mLastLocation = new Location(provider);
        }

        @Override
        public void run() {
            super.run();

            Looper.prepare();
            mHandler = new Handler();
            try {
                locationManager.requestLocationUpdates(mProvider, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            Looper.loop();
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e("asd", "onLocationChanged: " + location);
            mLastLocation.set(location);
            removeLocListener();   // for update once
            mHandler.getLooper().quit();
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e("asd", "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e("asd", "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e("asd", "onStatusChanged: " + provider);
        }
    }

    public void myServiceFunc(){
        Location curLocation;
        settingGPS();
        curLocation = getMyLocation();
        double cur_lat = curLocation.getLatitude();
        double cur_lng = curLocation.getLongitude();
        double item_lat;
        double item_lng;
        float[] results = new float[100];
        data_input = mCallback.recvData();
        //data_input의 정보들 중에서 취할 정보들을 처리, 알림할 것
        for(int i=0; i<data_input.size();i++){
            Log.d("정보",data_input.get(i).title);
        }

        /*
        for (int i = 0; i < data_input.size(); i++) {
            item_lat = data_input.get(i).latitude;
            item_lng = data_input.get(i).longtitude;
            Location.distanceBetween(cur_lat,cur_lng,item_lat,item_lng,results);
            NMapPOIitem item;
            if(results[0]<=100.0F)//지금설정으로는 현재위치에서 100m내 마커만 보일것.

        }*/
    }

    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        addLocListener();
        /*
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // TODO 위도, 경도로 하고 싶은 것
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };*/
    }
    private Location getMyLocation() {
        Location currentLocation = null;
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ActivityCompat.requestPermissions(MainActivity., new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MainActivity.MY_PERMISSIONS_REQUEST_CUR_PLACE);
        }
        else {
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        listener);
            } catch (java.lang.SecurityException ex) {
                Log.i("asd", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("asd", "network provider does not exist, " + ex.getMessage());
            }
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        listener);
            } catch (java.lang.SecurityException ex) {
                Log.i("asd", "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d("asd", "gps provider does not exist " + ex.getMessage());
            }
            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
            }
        }
        return currentLocation;
    }
}
