package com.example.cards.dagger;

import com.example.cards.activities.GameActivity;
import com.example.cards.activities.JoinRoomActivity;
import com.example.cards.activities.LobbyActivity;
import com.example.cards.activities.MainActivity;
import com.example.cards.activities.SelectCardBackActivity;
import com.example.cards.service.RoomService;
import com.example.cards.viewmodel.RoomViewModel;
import com.example.cards.views.DoneButton;
import com.example.cards.views.GameView;
import com.example.cards.views.HandView;
import com.example.cards.views.HomeButton;
import com.example.cards.views.RoomView;
import com.example.cards.views.StartButton;

import dagger.Component;

@Component(modules = {ContextModule.class, PreferencesMod.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(LobbyActivity lobbyActivity);

    void inject(GameView gameView);

    void inject(HandView handView);

    void inject(HomeButton homeButton);

    void inject(StartButton startButton);

    void inject(DoneButton doneButton);

    void inject(RoomView roomView);

    void inject(RoomService roomService);

    void inject(RoomViewModel roomViewModel);

    void inject(JoinRoomActivity joinRoomActivity);

    void inject(GameActivity gameActivity);

    void inject(SelectCardBackActivity selectCardBackActivity);
}