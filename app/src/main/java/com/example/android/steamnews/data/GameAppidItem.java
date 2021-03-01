package com.example.android.steamnews.data;

import com.google.gson.annotations.SerializedName;

public class GameAppidItem {
    @SerializedName("name")
    public String name;

    @SerializedName("appid")
    public int appId;

    public boolean bookmarked;

    public GameAppidItem(String name, int appId, boolean bookmarked) {
        this.name = name;
        this.appId = appId;
        this.bookmarked = bookmarked;
    }

    public GameAppidItem() {
        this ("", 0, false);
    }
}
