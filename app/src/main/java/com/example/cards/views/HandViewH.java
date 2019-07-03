package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.cards.R;

import androidx.annotation.Nullable;

public class HandViewH extends HandView {

    public HandViewH(Context context) {
        super(context);
    }

    public HandViewH(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HandViewH(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutId() {
        return R.layout.hand_view;
    }
}
