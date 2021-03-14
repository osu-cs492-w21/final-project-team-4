package com.example.android.steamnews;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.steamnews.data.ArticleData;
import com.example.android.steamnews.data.ArticleDataItem;

import java.util.ArrayList;

public class ArticleViewModel extends AndroidViewModel {
private ArticleRepository repository;
private LiveData<ArticleData> articleData;


    public ArticleViewModel(@NonNull Application application) {
        super(application);
        this.repository = new ArticleRepository();
    }

    public LiveData<ArticleData> getArticleData() {
        return this.articleData;
    }
    public LiveData<ArrayList<ArticleDataItem>> loadArticles(int appid){
       return this.repository.loadArticleData(appid);

    }
}
