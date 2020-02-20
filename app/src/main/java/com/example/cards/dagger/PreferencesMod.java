package com.example.cards.dagger;

import android.content.Context;

import com.example.cards.service.Preferences;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
class PreferencesMod {

    @Provides
    Preferences getPreferences(Context context) {
        return new Preferences(context);
    }
}
