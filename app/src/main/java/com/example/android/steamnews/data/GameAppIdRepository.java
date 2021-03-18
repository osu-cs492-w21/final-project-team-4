package com.example.android.steamnews.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.android.steamnews.Api;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameAppIdRepository {
    private static final String TAG = GameAppIdRepository.class.getSimpleName();

    private GameAppIdsDao dao;
    private GameAppIdService gameAppIdService;

    public GameAppIdRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.gameAppIdsDao();

        this.gameAppIdService = Api.getInstance().getSteamService();
    }

    public void insertGameAppIdItem(GameAppIdItem gameAppIdItem) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(gameAppIdItem);
            }
        });
    }

    private void rewriteAll(List<GameAppIdItem> gameAppIdItems, @Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    dao.deleteAll();
                    dao.insertAll(gameAppIdItems);

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

    public LiveData<List<GameAppIdItem>> getAppList() {
        return this.dao.getAll();
    }

    public LiveData<List<GameAppIdItem>> getBookmarkedGames() {
        return this.dao.getBookmarkedGames();
    }

    public Single<List<GameAppIdItem>> getBookmarkedGamesOneShot() {
        return this.dao.getBookmarkedGamesOneShot();
    }

    public Single<List<GameAppIdItem>> searchAppList(String query) {
        return this.dao.search("%" + query + "%");
    }

    public Single<Integer> countGameAppIdItems() {
        return this.dao.getRowCount();
    }

    public void fetchAppList(@Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        Log.d(TAG, "Fetching app list");
        Call<GameAppIdList> results;

        results = this.gameAppIdService.getAppList();

        results.enqueue(new Callback<GameAppIdList>() {
            @Override
            public void onResponse(Call<GameAppIdList> call, Response<GameAppIdList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched app list, now populating database with " + response.body().items.size() + " items");
                    rewriteAll(response.body().items, onDatabaseActionCompleteCallback);
                } else {
                    Log.e(TAG, "Failed to fetch app list, response " + response.code());
                    if (onDatabaseActionCompleteCallback != null) {
                        onDatabaseActionCompleteCallback.onFailure(new Throwable("API response code " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<GameAppIdList> call, Throwable t) {
                t.printStackTrace();
                if (onDatabaseActionCompleteCallback != null) {
                    onDatabaseActionCompleteCallback.onFailure(t);
                }
            }
        });
    }
}
