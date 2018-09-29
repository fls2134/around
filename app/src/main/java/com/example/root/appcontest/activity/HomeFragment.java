package com.example.root.appcontest.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.appcontest.model.RCViewControl;
import com.example.root.appcontest.model.SearchEditText;
import com.example.root.appcontest.R;

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

    /**
     * 리사이클러뷰 프래그먼트 장착 변수
     */
    FragmentTransaction fragmentTransaction;


    /**
     * 리사이클러 뷰 컨트롤
     */
    RCViewControl rcViewControl;

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
        setToolbar(view);
        setRecyclerView();
        setFloatingButton(view);
    }

    private void setToolbar(View view) {
        // imageButton add
        mSearchButton = (ImageButton) view.findViewById(R.id.btn_search_home);
        mSearchButton.setOnClickListener(this);

        // editText 설정
        mEditText = (SearchEditText) view.findViewById(R.id.editText_home);
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

        // add filter button
        mFilterButton = (ImageButton) view.findViewById(R.id.btn_filter_home);
        mFilterButton.setOnClickListener(this);
    }

    private void setTabs(View view) {
        mTabs = (TabLayout) view.findViewById(R.id.tabs_home);
        mTabs.addTab(mTabs.newTab().setText("거리순"));
        mTabs.addTab(mTabs.newTab().setText("최신순"));

        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 탭 선택시
                switch(tab.getPosition()) {
                    case 0: // 거리순
                        rcViewControl.arrangeByDistance();
                        break;
                    case 1: // 최신순
                        rcViewControl.arrangeByNew();
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
        rcViewControl = new RCViewControl();
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

        filterDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(getActivity(), "sibal", Toast.LENGTH_SHORT).show();
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
