package com.example.cards.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cards.App;
import com.example.cards.R;
import com.example.cards.service.CardStyle;
import com.example.cards.service.Preferences;
import com.example.cards.views.card_view.CardHiddenVerView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCardBackActivity extends AppCompatActivity {

    @BindView(R.id.gridCards) GridLayout gridCards;

    @Inject Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card_back);
        ButterKnife.bind(this);
        App.get().getAppComponent().inject(this);

        for (CardStyle value : CardStyle.values()) {
            gridCards.addView(new CardHiddenVerView(this, value));
        }

        for (int i = 0; i < gridCards.getChildCount(); i++) {
            View child = gridCards.getChildAt(i);
            final int index = i;
            child.setOnClickListener(v -> preferences.setCardBackStyle(CardStyle.getById(index)));
        }
    }
}
