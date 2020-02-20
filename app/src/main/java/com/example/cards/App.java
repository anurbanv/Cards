package com.example.cards;

import android.app.Application;

import com.example.cards.dagger.AppComponent;
import com.example.cards.dagger.ContextModule;
import com.example.cards.dagger.DaggerAppComponent;

public class App extends Application {

    private AppComponent appComponent;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this)).build();
    }

    public static App get() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
