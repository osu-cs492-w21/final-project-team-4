package com.example.android.steamnews.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.android.steamnews.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ArticleRepository {
    private static final String TAG = ArticleRepository.class.getSimpleName();

    private MutableLiveData<List<ArticleDataItem>> articleDataList;

    private GameAppIdService gameAppIdService;

    public ArticleRepository (){
        this.articleDataList = new MutableLiveData<>();
        this.articleDataList.setValue(null);

        this.gameAppIdService = Api.getInstance().getSteamService();

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
