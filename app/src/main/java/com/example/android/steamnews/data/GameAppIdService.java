package com.example.android.steamnews.data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GameAppIdService {
    @GET("ISteamApps/GetAppList/v2")
    Call<GameAppIdList> getAppList();
}
