package com.example.android.steamnews.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlayedGameDataDao {
    @Query("DELETE FROM playedGameData WHERE recentlyPlayed = :recentlyPlayed")
    void deleteAll(boolean recentlyPlayed);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PlayedGameData> playedGameData);

    @Query("SELECT * FROM playedGameData WHERE recentlyPlayed = 1 ORDER BY playtimeForever DESC")
    Single<List<PlayedGameData>> getRecentlyPlayedGames();

    @Query("SELECT * FROM gameAppIdItems WHERE appId IN (SELECT appId FROM playedGameData WHERE recentlyPlayed = 1 ORDER BY playtimeForever DESC)")
    Single<List<GameAppIdItem>> getRecentlyPlayedGameAppIds();

    @Query("SELECT * FROM playedGameData WHERE recentlyPlayed = 0 ORDER BY playtimeForever DESC")
    Single<List<PlayedGameData>> getOwnedGames();

    @Query("SELECT * FROM gameAppIdItems WHERE appId IN (SELECT appId FROM playedGameData WHERE recentlyPlayed = 0 ORDER BY playtimeForever DESC)")
    Single<List<GameAppIdItem>> getOwnedGameAppIds();
}
