package com.example.android.steamnews.data;

import android.app.Application;
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

    private MutableLiveData<List<TrendingDataItem>> articleDataList2;

    private GameAppIdService gameAppIdService;

    public TrendingRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        //this.dao = db.gameAppIdsDao();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TrendingDataList.class, new TrendingDataList.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);
    }



    public MutableLiveData<List<TrendingDataItem>> getArticleData(){
        return this.articleDataList2;
    }

    public MutableLiveData<List<TrendingDataItem>>  getTrendingList() {
        return this.articleDataList2;
    }

    public void fetchTrendingList() {
        Log.d(TAG, "Fetching app list");
        Call<TrendingData> results;

        results = this.gameAppIdService.getTrendingData();
        Log.d(TAG, "Called::: " + results);
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
