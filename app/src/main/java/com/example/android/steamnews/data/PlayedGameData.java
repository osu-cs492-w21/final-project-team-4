package com.example.android.steamnews.data;

import com.google.gson.annotations.SerializedName;

public class PlayedGameData {
    @SerializedName("appid")
    public int appId;

    @SerializedName("name")
    public String name;

    @SerializedName("playtime_2weeks")
    public int playtime2Weeks;

    @SerializedName("playtime_forever")
    public int playtimeForever;

    public PlayedGameData(int appId, String name, int playtime2Weeks, int playtimeForever) {
        this.appId = appId;
        this.name = name;
        this.playtime2Weeks = playtime2Weeks;
        this.playtimeForever = playtimeForever;
    }
}
