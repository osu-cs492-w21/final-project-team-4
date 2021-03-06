package com.example.android.steamnews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.GameAppIdRepository;

import java.util.List;

public class GameAppIdViewModel extends AndroidViewModel {
    private GameAppIdRepository repository;

    public GameAppIdViewModel(@NonNull Application application) {
        super(application);
        this.repository = new GameAppIdRepository(application);
    }

    public LiveData<List<GameAppIdItem>> getAppList() {
        return this.repository.getAppList();
    }

    public LiveData<List<GameAppIdItem>> searchAppList(String query) {
        return this.repository.searchAppList(query);
    }

    public void loadAppList() {
        this.repository.loadAppList();
    }
}
