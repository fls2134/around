package com.example.root.appcontest.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.appcontest.model.SearchEditText;
import com.example.root.appcontest.R;

public class MyPageFragment extends Fragment implements View.OnClickListener{

    TabLayout mTabs;
    SearchEditText mEditText;
    ImageButton mSearchButton;
    ImageButton mSettingButton;

    private boolean searchMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_activity_mypage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        setToolbar(view);
        setTabs(view);

    }

    private void setToolbar(View view) {
        // imageButton add
        mSearchButton = (ImageButton) view.findViewById(R.id.btn_search_my);
        mSearchButton.setOnClickListener(this);

        // editText 설정
        mEditText = (SearchEditText) view.findViewById(R.id.editText_my);
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

        // settingButton 설정
        mSettingButton = (ImageButton) view.findViewById(R.id.btn_setting_my);
        mSettingButton.setOnClickListener(this);
    }

    private void setTabs(View view) {
        mTabs = (TabLayout) view.findViewById(R.id.tabs_my);
        mTabs.addTab(mTabs.newTab().setText("담은글"));
        mTabs.addTab(mTabs.newTab().setText("작성글"));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 탭 선택시
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 탭 선택이 풀릴 시
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 탭이 다시 선택 되었을 시
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_search_my:
                if(!searchMode) {
                    mSearchButton.setImageResource(R.drawable.ic_keyboard_arrow);
                    mEditText.setHint("Search...");
                    mEditText.setUseableEditText(true);
                    searchMode = true;
                }
                else {
                    mSearchButton.setImageResource(R.drawable.ic_search);
                    mEditText.setHint(" 마이 페이지");
                    mEditText.setUseableEditText(false);
                    searchMode = false;
                }
                break;

            case R.id.btn_setting_my:
                Toast.makeText(getActivity().getApplicationContext(), "설정버튼", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
