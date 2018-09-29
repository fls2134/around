package com.example.root.appcontest.model;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.root.appcontest.R;
import com.example.root.appcontest.activity.InfoActivity;
import com.example.root.appcontest.activity.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by sks on 2018. 9. 28..
 */

public class RCViewControl extends Fragment{
    /**
     * 리사이클러 뷰 세팅을 위한 변수
     */
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;

    RCViewAdapter mAdapter;


    FirebaseDatabase database;
    DatabaseReference databaseReference;

    ArrayList<LocalData> data_array;
    /**
     * 카드 아이템 올라가는 리스트
     */
    ArrayList<CardItem> mList = new ArrayList<>();

    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recyclerview, container, false);

        progressBar = view.findViewById(R.id.recyclerView_progressbar);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Local_info");
        /*
        do {
            data_array = ((MainActivity)getActivity()).data_array;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                }
            }, 100);
        }while (data_array.isEmpty());
        */

        //addListItemFromDb();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        data_array = new ArrayList<>();
        setDataListener();
        mAdapter = new RCViewAdapter(mList,getContext());
        mRecyclerView.setAdapter(mAdapter);





        /*
        mRecyclerView.addOnItemTouchListener(new RCViewListener(getContext(), mRecyclerView, new RCViewListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                //Toast.makeText(getContext(),position+"",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                LocalData data = new LocalData();
                startActivity(intent);
            }

            @Override public void onLongItemClick(View view, int position) {
                // do whatever
            }
        }));
        */
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addListItemFromDb() {
        // this is sample
        /*
        mList.add(new CardItem(R.drawable.around_logo1, "aaa", R.drawable.around_logo1, "title1", 123));
        mList.add(new CardItem(R.drawable.around_logo1, "bbb", R.drawable.around_logo1, "title2", 456));
        mList.add(new CardItem(R.drawable.around_logo1, "ccc", R.drawable.around_logo1, "title3", 789));
        */
        //((MainActivity)getActivity()).getServerDatas();

        //로컬 데이터 담을 어레이리스트
        try
        {
            //data_array = ((MainActivity)getActivity()).data_array;

            //Toast.makeText(getContext(), "" + data_array.size(), Toast.LENGTH_SHORT).show();
        }
        catch(NullPointerException e)
        {
            Log.d("sibal", "addListItemFromDb: ");
            return;
        }
        for (int i = 0; i < data_array.size(); i++) {
            mList.add(new CardItem(R.drawable.around_logo1, data_array.get(i).nickname, data_array.get(i).img_url, data_array.get(i).title, data_array.get(i).id));
        }

        //양식은 다음과 같이
        /*
        try{
            db에서 가져옴
        }
        catch(){
            에러발생시 sample로 리스트 생성
        }
         */
        //mAdapter = new RCViewAdapter(mList,getContext());
        //mRecyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    public void arrangeByDistance() {
        // 거리순으로 정렬함
        //mList.add(new CardItem(R.drawable.around_logo2, "distance", R.drawable.around_logo2, "distance"));

        mAdapter.notifyDataSetChanged();
    }

    public void arrangeByNew() {
        // 최신순으로 정렬함
        //mList.add(new CardItem(R.drawable.around_logo2, "new", R.drawable.around_logo2, "new"));

        mAdapter.notifyDataSetChanged();
    }

    public void arrangeBySelected() {
        // 담은글 정렬

        mAdapter.notifyDataSetChanged();
    }

    public void arrangeByWrited() {
        // 작성글 정렬

        mAdapter.notifyDataSetChanged();
    }
    private void setDataListener()
    {
        //생성자로 서버에서 받아온값 다 넣어주면 될듯?
        //pull하고 datas.add(Localdata형식클래스) 하기
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data_array.clear();
                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    LocalData LD_tmp;
                    LD_tmp = new LocalData();
                    LD_tmp = messageData.getValue(LocalData.class);
                    data_array.add(LD_tmp);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
                addListItemFromDb();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"오류 발생",Toast.LENGTH_LONG);
            }
        });
        Log.d("sibal2", "getServerDatas: " + data_array.size());
    }

}
