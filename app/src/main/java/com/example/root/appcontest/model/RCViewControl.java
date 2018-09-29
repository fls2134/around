package com.example.root.appcontest.model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.root.appcontest.R;

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

    /**
     * 카드 아이템 올라가는 리스트
     */
    ArrayList<CardItem> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.recyclerview, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RCViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addListItemFromDb();
    }

    private void addListItemFromDb() {
        // this is sample
        mList.add(new CardItem(R.drawable.around_logo1, "aaa", R.drawable.around_logo1, "title1"));
        mList.add(new CardItem(R.drawable.around_logo1, "bbb", R.drawable.around_logo1, "title2"));
        mList.add(new CardItem(R.drawable.around_logo1, "ccc", R.drawable.around_logo1, "title3"));

        //양식은 다음과 같이
        /*
        try{
            db에서 가져옴
        }
        catch(){
            에러발생시 sample로 리스트 생성
        }
         */
    }

    public void arrangeByDistance() {
        // 거리순으로 정렬함
        mList.add(new CardItem(R.drawable.around_logo2, "distance", R.drawable.around_logo2, "distance"));

        mAdapter.notifyDataSetChanged();
    }

    public void arrangeByNew() {
        // 최신순으로 정렬함
        mList.add(new CardItem(R.drawable.around_logo2, "new", R.drawable.around_logo2, "new"));

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
}
