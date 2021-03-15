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

public class TrendingRepository {
    private static final String TAG = TrendingRepository.class.getSimpleName();
    private static final String BASE_URL = "https://steamspy.com/";

    private MutableLiveData<List<TrendingDataItem>> articleDataList;

    private GameAppIdService gameAppIdService;

    public TrendingRepository (){
        this.articleDataList = new MutableLiveData<>();
        this.articleDataList.setValue(null);



        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TrendingData.class, new TrendingData.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);

    }
    public MutableLiveData<List<TrendingDataItem>> getArticleData(){
        return this.articleDataList;
    }
    public void loadArticleData(int appid) {


        Log.d(TAG, "Fetching the articles for the appid: in the repository " + appid);
        this.articleDataList.setValue(null);
        Call<TrendingData> results = this.gameAppIdService.getTrendingData(appid);
        Log.d(TAG, "Fetching the articles for the appid: in the repository " + appid);
        results.enqueue(new Callback<TrendingData>() {
            @Override
            public void onResponse(Call<TrendingData> call, Response<TrendingData> response) {

                // if(response.code() == 200){
                articleDataList.setValue(response.body().items);
                //}
            }

            @Override
            public void onFailure(Call<TrendingData> call, Throwable t) {
                Log.d(TAG, "unsuccessful API request: " + call.request().url());
                t.printStackTrace();
            }
        });

    }






//    private boolean shouldFetchArticle(int appid) {
//
//        List<ArticleDataItem> currentArticle = this.articleDataList.getValue();
//        if (currentArticle == null) {
//            return true;
//        }
//
//        return false;
//    }
}
