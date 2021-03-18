package com.example.android.steamnews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.OnDatabaseActionCompleteCallback;
import com.example.android.steamnews.data.PlayedGameRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PlayedGameViewModel extends AndroidViewModel {
    private PlayedGameRepository playedGameRepository;

    public PlayedGameViewModel(@NonNull Application application) {
        super(application);
        this.playedGameRepository = new PlayedGameRepository(application);
    }

    public Single<List<GameAppIdItem>> getOwnedGameAppIds() {
        return this.playedGameRepository.getOwnedGameAppIds();
    }

    public Single<List<GameAppIdItem>> getRecentlyPlayedGameAppIds() {
        return this.playedGameRepository.getRecentlyPlayedGameAppIds();
    }


    public void loadData(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        this.playedGameRepository.loadData(apiKey, steamId, onDatabaseActionCompleteCallback);
    }
}
