package com.example.root.appcontest.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.root.appcontest.R;
import com.example.root.appcontest.map.NMapPOIflagType;
import com.example.root.appcontest.map.ResProvider;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.io.IOException;
import java.util.List;

public class GetLocationActivity extends NMapActivity {

    private NMapView mMapView;
    private final String CLIENT_ID = "A2hMBsKeTvfrDPF5q_dM";
    String addr;
    double longitude;
    double latitude;
    NMapPOIitem item;

    NMapController nMapController;
    NMapResourceProvider nMapResourceProvider;
    NMapOverlayManager nMapOverlayManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);
        mMapView = (NMapView) findViewById(R.id.mapView);

        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setAutoRotateEnabled(true, true);
        mMapView.requestFocus();

        nMapController = mMapView.getMapController();
        nMapResourceProvider = new ResProvider(this);
        nMapOverlayManager = new NMapOverlayManager(this,mMapView,nMapResourceProvider);

        ImageButton searchButton = findViewById(R.id.searchButton);
        final EditText addr_edittext = findViewById(R.id.addr_text);
        final Geocoder geocoder = new Geocoder(this);

        super.setMapDataProviderListener(new OnDataProviderListener() {
            @Override
            public void onReverseGeocoderResponse(NMapPlacemark nMapPlacemark, NMapError nMapError) {
                if (nMapError != null)
                {
                    addr="주소를 찾을수 없습니다";
                    item.setTitle(addr);
                }
                addr = nMapPlacemark.toString();
                item.setTitle(addr);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addr = addr_edittext.getText().toString();
                List<Address> list = null;

                try
                {
                    list = geocoder.getFromLocationName(addr, 1);
                }catch (IOException e)
                {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류");
                }

                if(list != null)
                {
                    if(list.size() == 0)
                        addr_edittext.setHint("해당하는 주소가 없습니다.");
                    else
                    {
                        latitude = list.get(0).getLatitude();
                        longitude = list.get(0).getLongitude();
                        nMapController.setMapCenter(longitude,latitude);
                        int marker1 = NMapPOIflagType.PIN;

                        // set POI data
                        final NMapPOIdata poiData = new NMapPOIdata(1, nMapResourceProvider);

                        poiData.beginPOIdata(1);

                        item = poiData.addPOIitem(null, addr, marker1, 0);
                        findPlacemarkAtLocation(longitude, latitude);

                        // initialize location to the center of the map view.
                        item.setPoint(nMapController.getMapCenter());

                        // set floating mode
                        item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);

                        // show right button on callout
                        item.setRightButton(true);

                        poiData.endPOIdata();

                        // create POI data overlay
                        NMapPOIdataOverlay poiDataOverlay = nMapOverlayManager.createPOIdataOverlay(poiData, null);
                        poiDataOverlay.setOnFloatingItemChangeListener(new NMapPOIdataOverlay.OnFloatingItemChangeListener() {
                            @Override
                            public void onPointChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                                NGeoPoint point = nMapPOIitem.getPoint();
                                findPlacemarkAtLocation(point.longitude, point.latitude);
                            }
                        });
                        poiDataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
                            @Override
                            public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

                            }

                            @Override
                            public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                                NGeoPoint point = nMapPOIitem.getPoint();
                                longitude = point.longitude;
                                latitude = point.latitude;
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("longitude", longitude);
                                resultIntent.putExtra("latitude", latitude);
                                resultIntent.putExtra("addr", addr);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();

                            }
                        });
                    }
                }

            }
        });
    }
}
