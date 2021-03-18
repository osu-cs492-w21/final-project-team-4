package com.example.android.steamnews.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.steamnews.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayedGameRepository {
    private static final String TAG = GameAppIdRepository.class.getSimpleName();
    private GameAppIdService gameAppIdService;

    private MutableLiveData<List<PlayedGameData>> cachedRecentlyPlayedGames;
    private MutableLiveData<List<PlayedGameData>> cachedOwnedGames;

    private String currentSteamId;

    public PlayedGameRepository() {
        this.gameAppIdService = Api.getInstance().getSteamService();

        this.cachedRecentlyPlayedGames = new MutableLiveData<>();
        this.cachedRecentlyPlayedGames.setValue(null);

        this.cachedOwnedGames = new MutableLiveData<>();
        this.cachedOwnedGames.setValue(null);
    }

    public LiveData<List<PlayedGameData>> getRecentlyPlayedGames() {
        return this.cachedRecentlyPlayedGames;
    }

    public LiveData<List<PlayedGameData>> getOwnedGames() {
        return this.cachedOwnedGames;
    }

    public void loadData(String apiKey, String steamId) {
        if (steamId.equals(this.currentSteamId)) {
            this.currentSteamId = steamId;
            fetchData(apiKey, steamId);
        } else {
            Log.d(TAG, "Using cached data for steam ID: " + steamId);
        }
    }

    private void fetchData(String apiKey, String steamId) {
        Log.d(TAG, "Fetching data for steam ID: " + steamId);

        Call<PlayedGameDataList> recentlyPlayedGamesResults = this.gameAppIdService.getRecentlyPlayedGames(apiKey, steamId);

        this.cachedRecentlyPlayedGames.setValue(null);

        recentlyPlayedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    cachedRecentlyPlayedGames.setValue(response.body().items);
                }
            }

            @Override
            public void onFailure(Call<PlayedGameDataList> call, Throwable t) {
                t.printStackTrace();
            }
        });
        Call<PlayedGameDataList> ownedGamesResults = this.gameAppIdService.getOwnedGames(apiKey, steamId);

        this.cachedOwnedGames.setValue(null);

        ownedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    cachedOwnedGames.setValue(response.body().items);
                }
            }

            @Override
            public void onFailure(Call<PlayedGameDataList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
