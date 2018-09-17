package com.example.root.appcontest.model;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.example.root.appcontest.R;

public class TagButton extends AppCompatButton {
    public TagButton(Context context) {
        this(context,null);
        this.setMinHeight(0);
        this.setMinimumHeight(0);
        this.setMinWidth(0);
        this.setMinimumWidth(0);
        this.setTextSize(24);
        this.setBackgroundResource(R.drawable.capsule);
    }
    public TagButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
