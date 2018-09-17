package com.example.root.appcontest.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.root.appcontest.R;
import com.example.root.appcontest.model.SearchEditText;


public class MapFragment extends Fragment implements View.OnClickListener{

    SearchEditText mEditText;
    ImageButton mSearchButton;

    private boolean searchMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_activity_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setToolbar(view);
    }

    private void setToolbar(View view) {
        // imageButton add
        mSearchButton = (ImageButton) view.findViewById(R.id.btn_search_map);
        mSearchButton.setOnClickListener(this);

        // editText 설정
        mEditText = (SearchEditText) view.findViewById(R.id.editText_map);
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

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_search_map:
                if(!searchMode) {
                    mSearchButton.setImageResource(R.drawable.ic_keyboard_arrow);
                    mEditText.setHint("Search...");
                    mEditText.setUseableEditText(true);
                    searchMode = true;
                }
                else {
                    mSearchButton.setImageResource(R.drawable.ic_search);
                    mEditText.setHint(" 지도");
                    mEditText.setUseableEditText(false);
                    searchMode = false;
                }
                break;
        }
    }
}
