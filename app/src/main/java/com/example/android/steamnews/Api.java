package com.example.android.steamnews;

import com.example.android.steamnews.data.ArticleData;
import com.example.android.steamnews.data.GameAppIdList;
import com.example.android.steamnews.data.GameAppIdService;
import com.example.android.steamnews.data.PlayedGameDataList;
import com.example.android.steamnews.data.TrendingData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a singleton class for interacting with an API via a Retrofit service.
 */
public class Api {
    private static volatile Api INSTANCE;
    private static final String BASE_URL = "https://api.steampowered.com";

    private GameAppIdService gameAppIdService;

    private Api() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GameAppIdList.class, new GameAppIdList.JsonDeserializer())
                .registerTypeAdapter(ArticleData.class, new ArticleData.JsonDeserializer())
                .registerTypeAdapter(PlayedGameDataList.class, new PlayedGameDataList.JsonDeserializer())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.gameAppIdService = retrofit.create(GameAppIdService.class);
    }

    /**
     * Gets the singleton instance of the `Api` class.
     */
    public static Api getInstance() {
        if (INSTANCE == null) {
            synchronized (Api.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Api();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Gets the Retrofit service.
     */
    public GameAppIdService getSteamService() {
        return this.gameAppIdService;
    }
}
