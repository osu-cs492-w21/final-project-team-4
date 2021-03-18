package com.example.android.steamnews.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameAppIdService {
    @GET("ISteamApps/GetAppList/v2")
    Call<GameAppIdList> getAppList();

    @GET("ISteamNews/GetNewsForApp/v0002")
    Call<ArticleData> getArticles();

    @GET("ISteamNews/GetNewsForApp/v0002/")
    Call<ArticleData> getArticleData(
            @Query("appid") int appid);

    @GET("api.php?request=top100in2weeks")
    Call<TrendingData> getTrendingData();

    @GET("IPlayerService/GetRecentlyPlayedGames/v0001/?format=json")
    Call<PlayedGameDataList> getRecentlyPlayedGames(@Query("key") String apiKey, @Query("steamid") String steamId);

    @GET("/IPlayerService/GetOwnedGames/v0001/?format=json")
    Call<PlayedGameDataList> getOwnedGames(@Query("key") String apiKey, @Query("steamid") String steamId);
}
