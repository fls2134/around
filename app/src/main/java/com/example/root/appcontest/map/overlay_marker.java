package com.example.root.appcontest.map;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.nhn.android.maps.NMapOverlay;
import com.nhn.android.maps.NMapView;

public class overlay_marker extends NMapOverlay {
    @Override
    public void draw(Canvas canvas, NMapView nMapView, boolean b) {
        super.draw(canvas, nMapView, b);
    }

    @Override
    public boolean draw(Canvas canvas, NMapView nMapView, boolean b, long l) {
        return super.draw(canvas, nMapView, b, l);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent, NMapView nMapView) {
        return super.onKeyDown(i, keyEvent, nMapView);
    }

    @Override
    public boolean onKeyUp(int i, KeyEvent keyEvent, NMapView nMapView) {
        return super.onKeyUp(i, keyEvent, nMapView);
    }

    @Override
    public boolean onTap(int i, int i1, NMapView nMapView) {
        return super.onTap(i, i1, nMapView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent, NMapView nMapView) {
        return super.onTouchEvent(motionEvent, nMapView);
    }
}
