package com.example.root.appcontest.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.root.appcontest.R;


public class Intro extends Activity {
    private Handler handler;

    Runnable mRun = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(Intro.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        handler = new Handler();
        handler.postDelayed(mRun, 1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(mRun);
    }
}

