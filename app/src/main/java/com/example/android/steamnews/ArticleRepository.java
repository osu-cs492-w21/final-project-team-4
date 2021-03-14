package com.example.android.steamnews;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.steamnews.data.ArticleData;
import com.example.android.steamnews.data.ArticleDataItem;
import com.example.android.steamnews.data.GameAppIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRepository {
    private static final String TAG = ArticleRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.steampowered.com";

    private MutableLiveData<ArrayList<ArticleDataItem>> articleDataList;

    private GameAppIdService gameAppIdService;

    public ArticleRepository (){
        this.articleDataList = new MutableLiveData<ArrayList<ArticleDataItem>>();
        this.articleDataList.setValue(null);


        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ArticleData.class, new ArticleData.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);

    }
    public MutableLiveData<ArrayList<ArticleDataItem>> getArticleData(){
        return this.articleDataList;
    }
    public LiveData<ArrayList<ArticleDataItem>> loadArticleData(int appid) {


        Log.d(TAG, "Fetching the articles for the appid: in the repository " + appid);
        this.articleDataList.setValue(null);
        Call<ArticleData> results;
        results = this.gameAppIdService.getArticleData(appid);
        //Log.d(TAG, "Here are the results:" + results);

        results.enqueue(new Callback<ArticleData>() {
            @Override
            public void onResponse(Call<ArticleData> call, Response<ArticleData> response) {
                if (response.code() == 200) {
                    articleDataList.setValue(response.body().articleData);
                    Log.d(TAG, "Here are the results:" + response.body());
                } else {
                    Log.d(TAG, "unsuccessful API request: " + call.request().url());
                    Log.d(TAG, "  -- response status code: " + response.code());
                    Log.d(TAG, "  -- response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ArticleData> call, Throwable t) {
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });
        return articleDataList;

    }






    private boolean shouldFetchArticle(int appid) {

        ArrayList<ArticleDataItem> currentArticle = this.articleDataList.getValue();
        if (currentArticle == null) {
            return true;
        }

        return false;
    }
}
