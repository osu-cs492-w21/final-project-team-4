package com.example.android.steamnews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.steamnews.data.TrendingDataItem;
import com.example.android.steamnews.data.TrendingRepository;

import java.util.List;

public class TrendingViewModel extends ViewModel {
    private TrendingRepository repository;

    private LiveData<List<TrendingDataItem>> listLiveData;


    public TrendingViewModel() {

        this.repository = new TrendingRepository();
        this.listLiveData =this.repository.getArticleData();

    }

    public LiveData<List<TrendingDataItem>> getArticleData() {
        return this.listLiveData;
    }


    public void loadArticles(int appid){
        this.repository.loadArticleData(appid);


    }


}
