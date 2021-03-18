package com.example.android.steamnews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.steamnews.data.PlayedGameData;
import com.example.android.steamnews.data.PlayedGameRepository;

import java.util.List;

public class PlayedGameViewModel extends ViewModel {
    private PlayedGameRepository playedGameRepository;
    private LiveData<List<PlayedGameData>> recentlyPlayedGames;
    private LiveData<List<PlayedGameData>> ownedGames;

    public PlayedGameViewModel() {
        this.playedGameRepository = new PlayedGameRepository();
        this.recentlyPlayedGames = this.playedGameRepository.getRecentlyPlayedGames();
        this.ownedGames = this.playedGameRepository.getOwnedGames();
    }

    public LiveData<List<PlayedGameData>> getRecentlyPlayedGames() {
        return this.recentlyPlayedGames;
    }

    public LiveData<List<PlayedGameData>> getOwnedGames() {
        return this.ownedGames;
    }

    public void loadData(String apiKey, String steamId) {
        this.playedGameRepository.loadData(apiKey, steamId);
    }
}
