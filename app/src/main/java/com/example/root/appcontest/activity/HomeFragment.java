package com.example.root.appcontest.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.appcontest.model.RCViewControl;
import com.example.root.appcontest.model.SearchEditText;
import com.example.root.appcontest.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

/**
 * made by sks 2018. 09. 17
 * Fragment for Main page
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    /**
     * 뷰 관련 변수
     */
    TabLayout mTabs;
    SearchEditText mEditText;
    ImageButton mSearchButton;
    ImageButton mFilterButton;
    FloatingActionButton writeBtn;
    Dialog filterDialog;

    private boolean searchMode = false;

    static final int WRITE_REQUEST_CODE = 1883;

    Set<String> filterSet;
    /**
     * 리사이클러뷰 프래그먼트 장착 변수
     */
    FragmentTransaction fragmentTransaction;


    /**
     * 리사이클러 뷰 컨트롤
     */
    RCViewControl rcViewControl;

    int position = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_activity_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        setTabs(view);
        setRecyclerView();
        setToolbar(view);
        setFloatingButton(view);
    }

    private void setToolbar(View view) {
        // imageButton add
        mSearchButton = (ImageButton) view.findViewById(R.id.btn_search_home);
        mSearchButton.setOnClickListener(this);

        // editText 설정
        mEditText = (SearchEditText) view.findViewById(R.id.editText_home);
        mEditText.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                // 재정렬

                String str = editable.toString();
                if(position == 0)
                {
                    rcViewControl.arrangeByNew(str);
                    Log.d("ssibal", "onDismiss: ");
                }
                else
                {
                    rcViewControl.arrangeByDistance(str);
                    Log.d("ssibal", "onDismiss: ");
                }

                // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                //Toast.makeText(getActivity().getApplicationContext(), "텍스트 입력됨", Toast.LENGTH_SHORT).show();
            }
        });

        mEditText.setUseableEditText(false);


        // add filter button
        mFilterButton = (ImageButton) view.findViewById(R.id.btn_filter_home);
        mFilterButton.setOnClickListener(this);
    }

    private void setTabs(View view) {
        mTabs = (TabLayout) view.findViewById(R.id.tabs_home);
        mTabs.addTab(mTabs.newTab().setText("최신순"));
        mTabs.addTab(mTabs.newTab().setText("거리순"));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 탭 선택시
                position = tab.getPosition();
                mEditText.setText("");
                mEditText.clearFocus();
                switch(tab.getPosition()) {
                    case 0: // 최신순
                        rcViewControl.arrangeByNew("");
                        break;
                    case 1: // 거리순
                        rcViewControl.arrangeByDistance("");
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

    private void setRecyclerView() {
        Bundle args = new Bundle();
        args.putInt("fragmentIndex", 0);
        rcViewControl = new RCViewControl();
        rcViewControl.setArguments(args);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.recyclerView_container, rcViewControl).commit();
    }

    private void setFloatingButton(View view) {
        writeBtn = view.findViewById(R.id.floatingActionButton_home);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeIntent = new Intent(getActivity(), WriteActivity.class);
                startActivityForResult(writeIntent, WRITE_REQUEST_CODE);
            }
        });
    }

    private void createFilterDialog()
    {
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        Button b = innerView.findViewById(R.id.button_comp_dialog);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterDialog.dismiss();
            }
        });
        filterDialog = new Dialog(getActivity());
        filterDialog.setContentView(innerView);

        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(true);


        final SharedPreferences pref = getContext().getSharedPreferences("filters", MODE_PRIVATE);
        filterSet = new HashSet<String>(pref.getStringSet("filter", new HashSet<String>()));


        final CheckBox[] filterCategories = new CheckBox[8];

        filterCategories[0] = innerView.findViewById(R.id.checkbox_filter_1);
        filterCategories[1] = innerView.findViewById(R.id.checkbox_filter_2);
        filterCategories[2] = innerView.findViewById(R.id.checkbox_filter_3);
        filterCategories[3] = innerView.findViewById(R.id.checkbox_filter_4);
        filterCategories[4] = innerView.findViewById(R.id.checkbox_filter_5);
        filterCategories[5] = innerView.findViewById(R.id.checkbox_filter_6);
        filterCategories[6] = innerView.findViewById(R.id.checkbox_filter_7);
        filterCategories[7] = innerView.findViewById(R.id.checkbox_filter_8);

        for (int i = 0; i < 8; i++) {
            if(filterSet.contains(filterCategories[i].getText().toString()))
                filterCategories[i].setChecked(true);
            else
                filterCategories[i].setChecked(false);
        }

        filterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                for (int i = 0; i < 8; i++) {
                    String str = filterCategories[i].getText().toString();
                    if(filterCategories[i].isChecked())
                    {
                        if(filterSet.contains(str))
                            continue;
                        else
                            filterSet.add(str);
                    }
                    else
                    {
                        if(filterSet.contains(str))
                            filterSet.remove(str);
                    }
                }


                for (int i = 0; i < 8; i++) {
                    if(filterSet.contains(filterCategories[i].getText()))
                    {
                        filterCategories[i].setChecked(true);
                        Log.d("sibal", filterCategories[i].getText().toString());
                    }
                    else
                        filterCategories[i].setChecked(false);
                }

                SharedPreferences.Editor editor = pref.edit();
                editor.putStringSet("filter", filterSet);
                editor.apply();

                //Toast.makeText(getContext(), "sibal" + position, Toast.LENGTH_SHORT).show();

                if(position == 0)
                {
                    rcViewControl.arrangeByNew("");
                    Log.d("ssibal", "onDismiss: ");
                }
                else
                {
                    rcViewControl.arrangeByDistance("");
                    Log.d("ssibal", "onDismiss: ");
                }
            }
        });
        filterDialog.show();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_search_home:
                if(!searchMode) {
                    mSearchButton.setImageResource(R.drawable.ic_keyboard_arrow);
                    mEditText.setHint(R.string.searching);
                    mEditText.setUseableEditText(true);
                    searchMode = true;
                }
                else {
                    mSearchButton.setImageResource(R.drawable.ic_search);
                    mEditText.setText(null);
                    mEditText.setHint(R.string.title_main);
                    mEditText.setUseableEditText(false);
                    searchMode = false;
                }
                break;

            case R.id.btn_filter_home:
                createFilterDialog();
                break;
        }
    }
}
