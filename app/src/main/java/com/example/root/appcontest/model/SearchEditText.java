package com.example.root.appcontest.model;

import android.content.Context;
import android.util.AttributeSet;

public class SearchEditText extends android.support.v7.widget.AppCompatEditText {
    public SearchEditText(Context context) {
        super(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUseableEditText(boolean useable) {
        this.setClickable(useable);
        this.setEnabled(useable);
        this.setFocusable(useable);
        this.setFocusableInTouchMode(useable);
    }
}
