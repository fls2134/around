package com.example.root.appcontest.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.root.appcontest.R;
import com.example.root.appcontest.model.LocalData;
import com.nhn.android.maps.overlay.NMapPOIitem;

import java.util.ArrayList;
import java.util.Timer;

public class AlarmService extends Service {

    PendingIntent pendingIntent;
    ArrayList<LocalData> data_input;
    LocationManager locationManager;
    LocationListener listener;
    Location curLocation;
    private final String CHANNEL_ID = "default";

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
        settingGPS();
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


    public class LocationListener extends Thread implements android.location.LocationListener
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
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void myServiceFunc(){
        curLocation = getMyLocation();
        double cur_lat = curLocation.getLatitude();
        double cur_lng = curLocation.getLongitude();
        double item_lat;
        double item_lng;
        float[] output;
        float[] results = new float[100];
        data_input = mCallback.recvData();
        //data_input의 정보들 중에서 취할 정보들을 처리, 알림할 것

        createNotificationChannel();



       /*
        for(int i=0; i<3; i++) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.around_logo2)
                    .setContentTitle("Around Seoul")
                    .setContentText("근처에 관심이 있을만한 장소가 있네요!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("근처에 관심이 있을만한 장소가 있네요!"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            //  .setContentIntent(pendingIntent)
            //  .addAction(R.drawable.ic_launcher_foreground, getString(R.string.snooze),
            //          pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(i, mBuilder.build());
        }
*/
       for(int i=0; i<data_input.size(); i++)
           Log.d("제목",data_input.get(i).title);

        //PendingIntent pendingIntents[] = new PendingIntent[data_input.size()];
        while(true) {



            Log.d("데이터 개수", Integer.toString(data_input.size()));
            output = new float[data_input.size()];
            for (int i = 0; i < data_input.size(); i++) {
                item_lat = data_input.get(i).latitude;
                item_lng = data_input.get(i).longtitude;
                Location.distanceBetween(cur_lat, cur_lng, item_lat, item_lng, results);
                output[i] = results[0];
                //pendingIntent = PendingIntent.getActivity(getApplicationContext(), i, nextIntent, PendingIntent.FLAG_ONE_SHOT);

            }

            for (int i = 0; i < data_input.size(); i++) {
                if (output[i] <= 500.0F)//지금설정으로는 현재위치에서 500m내 마커만 보일것.
                {

                    Log.d("거리", Float.toString(output[i]));
                    if (data_input.get(i).alarmed == false) {

                        Intent nextIntent = new Intent(getApplicationContext(), InfoActivity.class);
                        //nextIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        //        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        //        | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data",data_input.get(i));
                        nextIntent.putExtras(bundle);

                        /*
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                        stackBuilder.addNextIntentWithParentStack(nextIntent);

                        PendingIntent pendingIntent =
                                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        */

                        PendingIntent pintent = PendingIntent.getActivity(getApplicationContext(),data_input.get(i).id,nextIntent,0);




                       /*
                       TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                       PendingIntent pendingIntent =
                               stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                               */

                        Log.d("ssibal", "myServiceFunc: " +i);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.around_logo2)
                                .setContentTitle("Around Seoul")
                                .setContentText("근처에 관심을 가질 만한 장소가 있네요! " + data_input.get(i).title)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText("근처에 관심을 가질 만한 장소가 있네요! " + data_input.get(i).title))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pintent)
                                .addAction(R.drawable.around_logo2, getString(R.string.see_It),
                                        pendingIntent);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(i, mBuilder.build());
                        data_input.get(i).alarmed = true;
                    }
                }
            }
            SystemClock.sleep(60000);
        }
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

    public Location getCurLocation() {
        return curLocation;
    }
}
