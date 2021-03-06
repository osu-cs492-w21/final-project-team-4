package com.example.android.steamnews.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "gameAppIdItems")
public class GameAppIdItem {
    @PrimaryKey
    @NonNull
    @SerializedName("appid")
    public int appId;

    @NonNull
    @SerializedName("name")
    public String name;

    @NonNull
    @ColumnInfo(defaultValue = "False")
    public boolean bookmarked;

    @Ignore
    public GameAppIdItem(int appId, String name, boolean bookmarked) {
        this.appId = appId;
        this.name = name;
        this.bookmarked = bookmarked;
    }

    public GameAppIdItem() {
        this (0, "", false);
    }
}
