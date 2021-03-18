package com.example.android.steamnews.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.android.steamnews.Api;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayedGameRepository {
    private static final String TAG = PlayedGameRepository.class.getSimpleName();

    private PlayedGameDataDao dao;
    private GameAppIdService gameAppIdService;

    private String currentSteamId;

    public PlayedGameRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.playedGameDataDao();

        this.gameAppIdService = Api.getInstance().getSteamService();
    }

    private void rewriteAll(List<PlayedGameData> playedGameData, boolean recentlyPlayed, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dao.deleteAll(recentlyPlayed);
                    dao.insertAll(playedGameData);

                    if (onDatabaseActionCompleteCallback != null) {
                        onDatabaseActionCompleteCallback.onSuccess();
                    }
                } catch (Throwable t) {
                    if (onDatabaseActionCompleteCallback != null) {
                        onDatabaseActionCompleteCallback.onFailure(t);
                    }
                    throw t;
                }
            }
        });
    }

    public Single<List<GameAppIdItem>> getOwnedGameAppIds() {
        return this.dao.getOwnedGameAppIds();
    }

    public Single<List<GameAppIdItem>> getRecentlyPlayedGameAppIds() {
        return this.dao.getRecentlyPlayedGameAppIds();
    }

    public void loadData(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        if (!steamId.equals(this.currentSteamId)) {
            this.currentSteamId = steamId;
            fetchData(apiKey, steamId, onDatabaseActionCompleteCallback);
        } else {
            Log.d(TAG, "Using cached data for steam ID: " + steamId);
            onDatabaseActionCompleteCallback.onSuccess();
        }
    }

    private void fetchData(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Log.d(TAG, "Fetching data for steam ID: " + steamId);

        fetchOwnedGames(apiKey, steamId, onDatabaseActionCompleteCallback);
    }

    private void fetchOwnedGames(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Call<PlayedGameDataList> ownedGamesResults = this.gameAppIdService.getOwnedGames(apiKey, steamId);

        ownedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched owned played games list, now updating database");
                    List<PlayedGameData> items = response.body().items;
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).recentlyPlayed = false;
                    }
                    rewriteAll(items, false, new OnDatabaseActionCompleteCallback() {
                        @Override
                        public void onSuccess() {
                            fetchRecentlyPlayedGames(apiKey, steamId, onDatabaseActionCompleteCallback);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            if (onDatabaseActionCompleteCallback != null){
                                onDatabaseActionCompleteCallback.onFailure(throwable);
                            }
                        }
                    });
                } else {
                    if (onDatabaseActionCompleteCallback != null) {
                        onDatabaseActionCompleteCallback.onFailure(new Throwable("API response code " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayedGameDataList> call, Throwable t) {
                t.printStackTrace();
                onDatabaseActionCompleteCallback.onFailure(t);
            }
        });
    }

    private void fetchRecentlyPlayedGames(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Call<PlayedGameDataList> recentlyPlayedGamesResults = this.gameAppIdService.getRecentlyPlayedGames(apiKey, steamId);

        recentlyPlayedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched recently played games list, now updating database");
                    List<PlayedGameData> items = response.body().items;
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).recentlyPlayed = true;
                    }
                    rewriteAll(items, true, onDatabaseActionCompleteCallback);
                } else {
                    if (onDatabaseActionCompleteCallback != null) {
                        onDatabaseActionCompleteCallback.onFailure(new Throwable("API response code " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<PlayedGameDataList> call, Throwable t) {
                t.printStackTrace();
                onDatabaseActionCompleteCallback.onFailure(t);
            }
        });
    }
}
