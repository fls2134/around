package com.example.root.appcontest.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.appcontest.model.RCViewControl;
import com.example.root.appcontest.model.SearchEditText;
import com.example.root.appcontest.R;

import org.w3c.dom.Text;


/**
 * made by sks 2018. 09. 17
 * Fragment for Show User's info
 */
public class MyPageFragment extends Fragment implements View.OnClickListener{
    /**
     * 뷰 관련 변수
     */
    TabLayout mTabs;
    SearchEditText mEditText;
    ImageButton mSearchButton;
    ImageButton mSettingButton;

    private boolean searchMode = false;

    /**
     * 리사이클러뷰 프래그먼트 장착 변수
     */
    FragmentTransaction fragmentTransaction;

    /**
     * 리사이클러 뷰 컨트롤
     */
    RCViewControl rcViewControl;


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
        setNickName(view);
        setRecyclerView();
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
                switch (tab.getPosition()) {
                    case 0: // 담은글
                        rcViewControl.arrangeBySelected();
                        break;
                    case 1: // 작성글
                        rcViewControl.arrangeByWrited();
                        break;
                    default:
                        break;
                }
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

    private void setNickName(View v)
    {
        TextView nickname = v.findViewById(R.id.nickname);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nickname.setText(pref.getString("nickname_text","닉네임"));
    }

    private void setRecyclerView() {
        Bundle args = new Bundle();
        args.putInt("fragmentIndex", 1);
        rcViewControl = new RCViewControl();
        rcViewControl.setArguments(args);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.reccyclerview_container2, rcViewControl).commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_search_my:
                if(!searchMode) {
                    mSearchButton.setImageResource(R.drawable.ic_keyboard_arrow);
                    mEditText.setHint(R.string.searching);
                    mEditText.setUseableEditText(true);
                    searchMode = true;
                }
                else {
                    mSearchButton.setImageResource(R.drawable.ic_search);
                    mEditText.setText(null);
                    mEditText.setHint(R.string.title_my);
                    mEditText.setUseableEditText(false);
                    searchMode = false;
                }
                break;

            case R.id.btn_setting_my:
                //테스트 한다고 잠깐 추가 다시 없애야함
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
                //end here
                break;
        }
    }
}
