package com.example.root.appcontest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.example.root.appcontest.map.NMapFragment;
import com.example.root.appcontest.map.NMapPOIflagType;
import com.example.root.appcontest.map.ResProvider;
import com.example.root.appcontest.model.LocalData;
import com.example.root.appcontest.model.SearchEditText;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapCircleData;
import com.nhn.android.maps.overlay.NMapCircleStyle;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.root.appcontest.BuildConfig.DEBUG;

/**
 * made by sks 2018. 09. 17
 * Fragment for Show map
 */
public class MapFragment extends NMapFragment implements View.OnClickListener{

    SearchEditText mEditText;
    ImageButton mSearchButton;

    private OnFragmentInteractionListener mListener;
    private NMapView mMapView;
    private final String CLIENT_ID = "A2hMBsKeTvfrDPF5q_dM";
    final int MY_PERMISSON_REQUEST_FINE_LOCATION = 3;
    NMapController nMapController;
    NMapResourceProvider nMapResourceProvider;
    NMapOverlayManager nMapOverlayManager;
    NMapCompassManager compassManager;
    NMapMyLocationOverlay myLocationOverlay;
    NMapCircleData circleData;
    NMapLocationManager locationManager;
    MapContainerView mMapContainerView;

    double cur_lat;
    double cur_lng;

    float cur_meters;

    private boolean searchMode = false;

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.setAutoRotateEnabled(true, true);
        mMapView.requestFocus();

        compassManager = new NMapCompassManager(getActivity());

        locationManager = new NMapLocationManager(getContext());
        locationManager.enableMyLocation(true);
        locationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        nMapController = mMapView.getMapController();
        nMapResourceProvider = new ResProvider(getContext());

        nMapOverlayManager = new NMapOverlayManager(getContext(),mMapView,nMapResourceProvider);
        myLocationOverlay = nMapOverlayManager.createMyLocationOverlay(locationManager, compassManager);

        //mMapContainerView = new MapContainerView(getContext());
        //mMapContainerView.addView(mMapView);

        //testPOIdataOverlay();
        //startMyLocation(); 현재위치 방향까지 같이 표시

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tab_activity_map, container, false);
        mMapView = v.findViewById(R.id.nmap_view);
        mMapView.setClientId(CLIENT_ID);
        mMapView.setClickable(true);

        //mMapView = new NMapView(getContext());
        //LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = vi.inflate(R.layout.test,null);
        //mMapView.addView(vi);
        /*
        ImageButton compass = v.findViewById(R.id.compass);
        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMyLocation();
            }
        });
        */

        //setContentView(mMapContainerView);


        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void testPOIdataOverlay() {

        //final int ITEM_SIZE = 12;
        // Markers for POI item
        int markerId = NMapPOIflagType.PIN;
        float results[]= new float[1];
        ArrayList<LocalData> data_array;

        //로컬 데이터 담을 어레이리스트
        try
        {
            data_array = ((MainActivity)getActivity()).data_array;
        }
        catch(NullPointerException e)
        {
            return;
        }


        //localDatas.add(로컬 데이터)로 서버에있는거 다 들고오기
        // set POI data
        NMapPOIdata poiData = new NMapPOIdata(2, nMapResourceProvider);
        poiData.beginPOIdata(2);
        for (int i = 0; i < data_array.size(); i++) {
            double item_lat = data_array.get(i).latitude;
            double item_lng = data_array.get(i).longtitude;
            Location.distanceBetween(cur_lat,cur_lng,item_lat,item_lng,results);
            NMapPOIitem item;
            if(results[0]<=cur_meters)//지금설정으로는 현재위치에서 100m내 마커만 보일것.
                item = poiData.addPOIitem(item_lng,item_lat, data_array.get(i).title, markerId, 0);
            //NMapPOIitem item = poiData.addPOIitem(item_lat,item_lng, "바겐 세일~~~ "+i, markerId, 0);
        }
        //Location.distanceBetween(cur_lat,cur_lng,,,results);
        // poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);



        poiData.endPOIdata();

        // create POI data overlay
        NMapPOIdataOverlay poiDataOverlay = nMapOverlayManager.createPOIdataOverlay(poiData, null);
        // set event listener to the overlay
        poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

        // select an item
        poiDataOverlay.selectPOIitem(0, true);

        // show all POI data
        poiDataOverlay.showAllPOIdata(0);
    }

    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                Log.i("asd", "onCalloutClick: title=" + item.getTitle());
            }

            // [[TEMP]] handle a click event of the callout
            // 인텐트해서 인포 액티비티 만들자.
            Toast.makeText(getContext(), "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
            if (DEBUG) {
                if (item != null) {
                    Log.i("asd", "onFocusChanged: " + item.toString());
                } else {
                    Log.i("asd", "onFocusChanged: ");
                }
            }
        }
    };


    private void startMyLocation() {

        if (myLocationOverlay != null) {
            if (!nMapOverlayManager.hasOverlay(myLocationOverlay)) {
                nMapOverlayManager.addOverlay(myLocationOverlay);
            }

            if (locationManager.isMyLocationEnabled()) {

                if (!mMapView.isAutoRotateEnabled()) {
                    myLocationOverlay.setCompassHeadingVisible(true);

                    compassManager.enableCompass();

                    mMapView.setAutoRotateEnabled(true, false);

                    mMapContainerView.requestLayout();
                } else {
                    stopMyLocation();
                }

                mMapView.postInvalidate();
            } else {
                boolean isMyLocationEnabled = locationManager.enableMyLocation(true);
                if (!isMyLocationEnabled) {
                    Toast.makeText(getContext(), "Please enable a My Location source in system settings",
                            Toast.LENGTH_LONG).show();

                    Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(goToSettings);

                    return;
                }
            }
        }
    }

    private void stopMyLocation() {
        if (myLocationOverlay != null) {
            locationManager.disableMyLocation();

            if (mMapView.isAutoRotateEnabled()) {
                myLocationOverlay.setCompassHeadingVisible(false);

                compassManager.disableCompass();

                mMapView.setAutoRotateEnabled(false, false);

                mMapView.requestLayout();
            }
        }
    }

    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
            if(nMapController != null)
            {
                cur_lat = nGeoPoint.latitude;
                cur_lng = nGeoPoint.longitude;
                SharedPreferences pref;
                try{
                    pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                }
                catch (Exception e)
                {
                    return false;
                }
                String str = pref.getString("radius_list", "500");
                cur_meters = Float.parseFloat(str);

                testPOIdataOverlay();
                int zoom_level = ((int)cur_meters) / 500;
                Log.d("sibal", ""+zoom_level);
 //               nMapController.setZoomEnabled(true);
//                nMapController.setMapCenter(nGeoPoint, 10);
                nMapController.animateTo(nGeoPoint);
//                nMapController.setZoomLevel(1);
                NMapPathDataOverlay pathDataOverlay = nMapOverlayManager.createPathDataOverlay();
                if(circleData == null)
                    circleData = new NMapCircleData(1);
                else
                {
                    nMapOverlayManager.removeOverlay(pathDataOverlay);
                    circleData = new NMapCircleData(1);
                }
                circleData.initCircleData();

                circleData.addCirclePoint(nGeoPoint.getLongitude(),nGeoPoint.getLatitude(),cur_meters);//50.0F가 사이즈
                circleData.endCircleData();
                pathDataOverlay.addCircleData(circleData);
                NMapCircleStyle circleStyle = new NMapCircleStyle(mMapView.getContext());

                circleStyle.setLineType(NMapPathLineStyle.TYPE_DASH);
                circleStyle.setFillColor(0x0000FF,0x20);
                circleData.setCircleStyle(circleStyle);
                pathDataOverlay.showAllPathData(0);

            }
            return true;
        }

;

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {

        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

        }
    };
    private class MapContainerView extends ViewGroup {

        public MapContainerView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            final int width = getWidth();
            final int height = getHeight();
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);
                final int childWidth = view.getMeasuredWidth();
                final int childHeight = view.getMeasuredHeight();
                final int childLeft = (width - childWidth) / 2;
                final int childTop = (height - childHeight) / 2;
                view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            }

            if (changed) {
                nMapOverlayManager.onSizeChanged(width, height);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int sizeSpecWidth = widthMeasureSpec;
            int sizeSpecHeight = heightMeasureSpec;

            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View view = getChildAt(i);

                if (view instanceof NMapView) {
                    if (mMapView.isAutoRotateEnabled()) {
                        int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
                        sizeSpecWidth = View.MeasureSpec.makeMeasureSpec(diag, View.MeasureSpec.EXACTLY);
                        sizeSpecHeight = sizeSpecWidth;
                    }
                }

                view.measure(sizeSpecWidth, sizeSpecHeight);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
    }

    private void setToolbar(View view) {
        // imageButton add
        mSearchButton = (ImageButton) view.findViewById(R.id.btn_search_map);
        mSearchButton.setOnClickListener(this);

        // editText 설정
        mEditText = (SearchEditText) view.findViewById(R.id.editText_map);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 재정렬
                Toast.makeText(getActivity().getApplicationContext(), "텍스트 입력됨", Toast.LENGTH_SHORT).show();
            }
        });

        mEditText.setUseableEditText(false);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_search_map:
                if(!searchMode) {
                    mSearchButton.setImageResource(R.drawable.ic_keyboard_arrow);
                    mEditText.setHint(R.string.searching);
                    mEditText.setUseableEditText(true);
                    searchMode = true;
                }
                else {
                    mSearchButton.setImageResource(R.drawable.ic_search);
                    mEditText.setText(null);
                    mEditText.setHint(R.string.title_map);
                    mEditText.setUseableEditText(false);
                    searchMode = false;
                }
                break;
        }
    }
}
