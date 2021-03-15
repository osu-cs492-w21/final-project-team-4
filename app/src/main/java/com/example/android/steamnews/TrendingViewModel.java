package com.example.android.steamnews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.steamnews.data.GameAppIdItem;
import com.example.android.steamnews.data.GameAppIdRepository;
import com.example.android.steamnews.data.TrendingDataItem;
import com.example.android.steamnews.data.TrendingRepository;

import java.util.List;

public class TrendingViewModel extends AndroidViewModel {
    private TrendingRepository repository;

    private LiveData<List<TrendingDataItem>> listLiveData2;

    public TrendingViewModel(@NonNull Application application) {
        super(application);
        this.repository = new TrendingRepository(application);
    }

//    public void insertGameAppIdItem(GameAppIdItem gameAppIdItem) {
//        this.repository.insertGameAppIdItem(gameAppIdItem);
//    }
//
//    public LiveData<Integer> countGameAppIdItems() {
//        return this.repository.countGameAppIdItems();
//    }
//
//    public LiveData<List<GameAppIdItem>> getAppList() {
//        return this.repository.getAppList();
//    }
//
//    public LiveData<List<GameAppIdItem>> searchAppList(String query) {
//        return this.repository.searchAppList(query);
//    }
    public LiveData<List<TrendingDataItem>> getBookmarkedGames() {
        return this.repository.getTrendingList();
    }

    public void fetchTrendingList() {
        this.repository.fetchTrendingList();
    }



}
