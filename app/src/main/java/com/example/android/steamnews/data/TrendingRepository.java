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

    private MutableLiveData<List<TrendingDataItem>> trendingDataList;

    private GameAppIdService gameAppIdService;

    public TrendingRepository(Application application) {
        this.trendingDataList = new MutableLiveData<>();
        this.trendingDataList.setValue(null);
        Log.d(TAG, "trendrepo" + application);
        //AppDatabase db = AppDatabase.getDatabase(application);
        //this.dao = db.gameAppIdsDao();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TrendingData.class, new TrendingData.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);
        Log.d(TAG, "trendrepo" + this.gameAppIdService);
    }


    public MutableLiveData<List<TrendingDataItem>>  getArticleData() {
        return this.trendingDataList;
    }

    public void fetchTrendingList() {
        Log.d(TAG, "Fetching app");
        Call<TrendingData> results;

        results = this.gameAppIdService.getTrendingData();
        Log.d(TAG, "Called::: " + results);

    }


    public void fetchAppList() {
        Log.d(TAG, "Fetching app list");
        Call<TrendingData> results;

        results = this.gameAppIdService.getTrendingData();

        results.enqueue(new Callback<TrendingData>() {
            @Override
            public void onResponse(Call<TrendingData> call, Response<TrendingData> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "here is the url: "+call.request().url());
                    Log.d(TAG, "Fetched app list, now updating database" + response.body().items);
                    trendingDataList.setValue(response.body().items);
                } else {
                    Log.e(TAG, "Failed to fetch app list, response " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TrendingData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
