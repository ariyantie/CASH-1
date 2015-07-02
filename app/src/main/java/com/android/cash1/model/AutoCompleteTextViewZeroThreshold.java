package com.android.cash1.model;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class AutoCompleteTextViewZeroThreshold extends AutoCompleteTextView {

    public AutoCompleteTextViewZeroThreshold(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        showDropDown();
        return super.requestFocus(direction, previouslyFocusedRect);
    }
}
