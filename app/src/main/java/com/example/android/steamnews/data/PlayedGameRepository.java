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

    private void rewriteAll(List<PlayedGameData> playedGameData, boolean deleteAll, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (deleteAll) {
                        dao.deleteAll();
                    }
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

    // Loads data with caching based off the Steam ID
    public void loadData(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        if (!steamId.equals(this.currentSteamId)) {
            this.currentSteamId = steamId;
            fetchData(apiKey, steamId, onDatabaseActionCompleteCallback);
        } else {
            Log.d(TAG, "Using cached data for steam ID: " + steamId);
            onDatabaseActionCompleteCallback.onSuccess();
        }
    }

    // Fetch all data for owned and recently played games
    private void fetchData(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Log.d(TAG, "Fetching data for steam ID: " + steamId);

        // Fetch owned games then recently played games, in that specific order.
        // Because the appId is the primary key, when the recently played games are inserted,
        // they will update the recentlyPlayed flag in the row to true. If the owned games
        // were fetched second, then they'd reset all these flags to false
        fetchOwnedGames(apiKey, steamId, onDatabaseActionCompleteCallback);
    }

    // Fetch owned game data
    private void fetchOwnedGames(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Call<PlayedGameDataList> ownedGamesResults = this.gameAppIdService.getOwnedGames(apiKey, steamId);

        ownedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched owned played games list, now updating database");

                    // Ensure recentlyPlayed is false for all items
                    List<PlayedGameData> items = response.body().items;
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).recentlyPlayed = false;
                    }

                    // Write to database and then fetch recently played games
                    rewriteAll(items, true, new OnDatabaseActionCompleteCallback() {
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

    // Fetch recently played game data
    private void fetchRecentlyPlayedGames(String apiKey, String steamId, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Call<PlayedGameDataList> recentlyPlayedGamesResults = this.gameAppIdService.getRecentlyPlayedGames(apiKey, steamId);

        recentlyPlayedGamesResults.enqueue(new Callback<PlayedGameDataList>() {
            @Override
            public void onResponse(Call<PlayedGameDataList> call, Response<PlayedGameDataList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched recently played games list, now updating database");

                    // Ensure recentlyPlayed is true for all data
                    List<PlayedGameData> items = response.body().items;
                    for (int i = 0; i < items.size(); i++) {
                        items.get(i).recentlyPlayed = true;
                    }

                    // Write to database
                    rewriteAll(items, false, onDatabaseActionCompleteCallback);
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
