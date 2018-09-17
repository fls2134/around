package com.example.root.appcontest.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.root.appcontest.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.themeColor));

        // 최초 화면 설정
        FragmentTransaction fragmentTransaction
                = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.action_container, new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // 하단 바 설정
        BottomBar bottomBar = (BottomBar)findViewById(R.id.bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentTransaction fragmentTransaction
                        = getSupportFragmentManager().beginTransaction();

                switch (tabId) {
                    case R.id.tab_home:
                        fragmentTransaction.replace(R.id.action_container,
                                new HomeFragment()).commit();
                        break;
                    case R.id.tab_location:
                        fragmentTransaction.replace(R.id.action_container,
                                new MapFragment()).commit();
                        break;
                    case R.id.tab_my:
                        fragmentTransaction.replace(R.id.action_container,
                                new MyPageFragment()).commit();
                        break;
                }
            }
        });
    }
}

