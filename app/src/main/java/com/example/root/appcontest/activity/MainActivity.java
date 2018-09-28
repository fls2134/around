package com.example.root.appcontest.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.example.root.appcontest.model.LocalData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

/**
 * made by sks 2018. 09. 17
 * Activity that Show Main Content
 */
public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_CUR_PLACE = 3;
    FragmentTransaction fragmentTransaction;
    public ArrayList<LocalData> data_array;//서버에서 불러올 데이터 모은 어레이리스트
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    LocalData LD_tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Firebase 레퍼런스
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Local_info");

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.themeColor));

        // 하단 바 설정
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_bar);
        bottomBar.setActiveTabColor(getResources().getColor(R.color.backgroundLongin));
        bottomBar.setInActiveTabColor(getResources().getColor(R.color.inactivateTabColor));
        bottomBar.selectTabAtPosition(1);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                fragmentTransaction
                        = getSupportFragmentManager().beginTransaction();

                switch (tabId) {
                    case R.id.tab_home:
                        fragmentTransaction.replace(R.id.action_container,
                                new HomeFragment()).commit();
                        break;
                    case R.id.tab_location:
                        if(requestPermission() == PackageManager.PERMISSION_GRANTED)
                            fragmentTransaction.replace(R.id.action_container,
                                    new MapFragment()).commit();
                        else
                            Toast.makeText(MainActivity.this, "위치권한이 없어 지도를 표시할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab_my:
                        fragmentTransaction.replace(R.id.action_container,
                                new MyPageFragment()).commit();
                        break;
                }
            }
        });

        data_array = new ArrayList<>();
        getServerDatas();
        // 최초 화면 설정
    }

    private int requestPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_CUR_PLACE);
            return -1;
        }
        else
        {
            if(permissionCheck == PackageManager.PERMISSION_GRANTED)
                return PackageManager.PERMISSION_GRANTED;
            else
                return PackageManager.PERMISSION_DENIED;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_CUR_PLACE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    fragmentTransaction.replace(R.id.action_container,
                            new MapFragment()).commit();
                else
                    Toast.makeText(MainActivity.this, "위치 권한이 없으면 지도를 표시할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }

    void getServerDatas()
    {
        //생성자로 서버에서 받아온값 다 넣어주면 될듯?
        //pull하고 datas.add(Localdata형식클래스) 하기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    //LD_tmp = new LocalData();
                    LD_tmp = messageData.getValue(LocalData.class);
                    data_array.add(LD_tmp);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"오류 발생",Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getServerDatas();
    }
}

