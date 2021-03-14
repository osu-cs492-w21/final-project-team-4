package com.example.android.steamnews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.ArticleRepository;

import java.util.List;

public class ArticleViewModel extends ViewModel {
private ArticleRepository repository;

private LiveData<List<ArticleDataItem>> listLiveData;


    public ArticleViewModel() {

        this.repository = new ArticleRepository();
        this.listLiveData =this.repository.getArticleData();

    }

    public LiveData<List<ArticleDataItem>> getArticleData() {
        return this.listLiveData;
    }


    public void loadArticles(int appid){
        this.repository.loadArticleData(appid);


    }


}
