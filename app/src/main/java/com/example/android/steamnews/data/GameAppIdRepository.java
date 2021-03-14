package com.example.android.steamnews.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameAppIdRepository {
    private static final String TAG = GameAppIdRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.steampowered.com";

    private GameAppIdsDao dao;
    private GameAppIdService gameAppIdService;

    public GameAppIdRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        this.dao = db.gameAppIdsDao();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GameAppIdList.class, new GameAppIdList.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);
    }

    public void insertGameAppIdItem(GameAppIdItem gameAppIdItem) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(gameAppIdItem);
            }
        });
    }

    private void rewriteAll(List<GameAppIdItem> gameAppIdItems) {
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
                dao.insertAll(gameAppIdItems);
            }
        });
    }

    public LiveData<List<GameAppIdItem>> getAppList() {
        return this.dao.getAll();
    }

    public LiveData<List<GameAppIdItem>> getBookmarkedGames() {
        return this.dao.getBookmarkedGames();
    }

    public LiveData<List<GameAppIdItem>> searchAppList(String query) {
        return this.dao.search("%" + query + "%");
    }

    public LiveData<Integer> countGameAppIdItems() {
        return this.dao.getRowCount();
    }

    public void fetchAppList() {
        Log.d(TAG, "Fetching app list");
        Call<GameAppIdList> results;

        results = this.gameAppIdService.getAppList();

        results.enqueue(new Callback<GameAppIdList>() {
            @Override
            public void onResponse(Call<GameAppIdList> call, Response<GameAppIdList> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "Fetched app list, now updating database");
                    rewriteAll(response.body().items);
                } else {
                    Log.e(TAG, "Failed to fetch app list, response " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GameAppIdList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
