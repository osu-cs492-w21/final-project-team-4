package com.example.android.steamnews.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRepository {
    private static final String TAG = ArticleRepository.class.getSimpleName();
    private static final String BASE_URL = "https://api.steampowered.com";

    private MutableLiveData<List<ArticleDataItem>> articleDataList;

    private GameAppIdService gameAppIdService;

    public ArticleRepository (){
        this.articleDataList = new MutableLiveData<>();
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
    public MutableLiveData<List<ArticleDataItem>> getArticleData(){
        return this.articleDataList;
    }
    public void loadArticleData(int appid) {


        Log.d(TAG, "Fetching the articles for the appid: in the repository " + appid);
        this.articleDataList.setValue(null);
        Call<ArticleData> results = this.gameAppIdService.getArticleData(appid);

        results.enqueue(new Callback<ArticleData>() {
            @Override
            public void onResponse(Call<ArticleData> call, Response<ArticleData> response) {

               // if(response.code() == 200){
                    articleDataList.setValue(response.body().items);
               //}
            }

            @Override
            public void onFailure(Call<ArticleData> call, Throwable t) {
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });

    }






    private boolean shouldFetchArticle(int appid) {

        List<ArticleDataItem> currentArticle = this.articleDataList.getValue();
        if (currentArticle == null) {
            return true;
        }

        return false;
    }
}
