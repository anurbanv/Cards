package com.example.cards.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.R;
import com.example.cards.views.card_view.CardHiddenVerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCardBackActivity extends AppCompatActivity {

    @BindView(R.id.gridCards) GridLayout gridCards;
    @BindView(R.id.tvSelected) TextView tvSelected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card_back);
        ButterKnife.bind(this);

        CardHiddenVerView view = new CardHiddenVerView(this, 0);
        CardHiddenVerView view1 = new CardHiddenVerView(this, 1);
        CardHiddenVerView view2 = new CardHiddenVerView(this, 2);

        gridCards.addView(view);
        gridCards.addView(view1);
        gridCards.addView(view2);

        for (int i = 0; i < gridCards.getChildCount(); i++) {
            View child = gridCards.getChildAt(i);
            final int index = i + 1;
            child.setOnClickListener(v -> tvSelected.setText(String.valueOf(index)));
        }
    }
}
