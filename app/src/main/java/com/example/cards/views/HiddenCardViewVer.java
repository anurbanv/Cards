package com.example.cards.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.cards.R;

public class HiddenCardViewVer extends LinearLayout {

    public HiddenCardViewVer(Context context) {
        super(context);
        init(context);
    }

    public HiddenCardViewVer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HiddenCardViewVer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_card_hidden_ver, this, true);
    }
}
