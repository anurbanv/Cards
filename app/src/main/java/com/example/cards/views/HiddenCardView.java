package com.example.cards.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cards.MainActivity;
import com.example.cards.R;
import com.example.cards.domain.Card;
import com.example.cards.domain.Player;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HiddenCardView extends LinearLayout {

    public HiddenCardView(Context context) {
        super(context);
        init(context);
    }

    public HiddenCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HiddenCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.item_card_hidden, this, true);
    }
}
