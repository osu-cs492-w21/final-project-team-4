package com.example.android.steamnews.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

// Data related to a game played by a Steam account associated with a given Steam ID
// This class is used for both recently played games and owned games, though the API returns slightly
// different data for each
@Entity(tableName = "playedGameData")
public class PlayedGameData {
    @PrimaryKey
    @NonNull
    @SerializedName("appid")
    public int appId;

    @ColumnInfo(defaultValue = "0")
    @SerializedName("playtime_forever")
    public int playtimeForever;

    @NonNull
    public boolean recentlyPlayed; // True if recently played, false if just owned

    @Ignore
    public PlayedGameData(@NonNull int appId, int playtimeForever, boolean recentlyPlayed) {
        this.appId = appId;
        this.playtimeForever = playtimeForever;
        this.recentlyPlayed = recentlyPlayed;
    }

    public PlayedGameData() {
        this(0, 0, false);
    }
}
