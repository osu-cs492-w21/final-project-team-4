package com.example.android.steamnews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.GameAppIdRepository;
import com.example.android.steamnews.data.OnDatabaseActionCompleteCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class GameAppIdViewModel extends AndroidViewModel {
    private GameAppIdRepository repository;

    public GameAppIdViewModel(@NonNull Application application) {
        super(application);
        this.repository = new GameAppIdRepository(application);
    }

    public void insertGameAppIdItem(GameAppIdItem gameAppIdItem) {
        this.repository.insertGameAppIdItem(gameAppIdItem);
    }

    public Single<Integer> countGameAppIdItems() {
        return this.repository.countGameAppIdItems();
    }

    public LiveData<List<GameAppIdItem>> getAppList() {
        return this.repository.getAppList();
    }

    public Single<List<GameAppIdItem>> searchAppList(String query) {
        return this.repository.searchAppList(query);
    }
    public LiveData<List<GameAppIdItem>> getBookmarkedGames() {
        return this.repository.getBookmarkedGames();
    }

    public Single<List<GameAppIdItem>> getBookmarkedGamesOneShot() {
        return this.repository.getBookmarkedGamesOneShot();
    }

    public void fetchAppList(@Nullable OnDatabaseActionCompleteCallback onDatabaseActionCompleteCallback) {
        this.repository.fetchAppList(onDatabaseActionCompleteCallback);
    }


}
