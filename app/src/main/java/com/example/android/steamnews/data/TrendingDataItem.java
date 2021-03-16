package com.example.android.steamnews.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.android.steamnews.TrendingActivity;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "trendingDataItems")
public class TrendingDataItem {
    @PrimaryKey
    @NonNull
    @SerializedName("appid")
    public int appID;

    @NonNull
    @SerializedName("name")
    public String name;


    @Ignore
    public TrendingDataItem(int appId, String name) {
        this.appID = appId;
        this.name = name;
        //this.bookmarked = bookmarked;
    }

    public TrendingDataItem() {
        this (0, "");
    }
}
