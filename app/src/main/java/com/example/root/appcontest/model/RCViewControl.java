package com.example.root.appcontest.model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.appcontest.R;

import java.util.ArrayList;

/**
 * Created by sks on 2018. 9. 28..
 */

public class RCViewControl extends Fragment {
    /**
     * 리사이클러 뷰 세팅을 위한 변수
     */
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 카드 아이템 올라가는 리스트
     */
    ArrayList<CardItem> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recyclerview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // recyclerView 뷰 생성 및 설정
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        // 레이아웃 매니저 세팅
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 리스트 작성
        addListItemFromDb();

        mAdapter = new RCViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addListItemFromDb() {
        mList.add(new CardItem(R.drawable.around_logo1,"aaa",R.drawable.around_logo1,"title1"));
        mList.add(new CardItem(R.drawable.around_logo1,"bbb",R.drawable.around_logo1,"title2"));
        mList.add(new CardItem(R.drawable.around_logo1,"ccc",R.drawable.around_logo1,"title3"));

    }

    private void arange(){
        //기준에 따라서 다른 정렬 모드
        //아마도 addListItemFromDb 메서드에서 사용할듯
    }
}
