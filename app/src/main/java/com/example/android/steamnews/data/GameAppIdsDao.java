package com.example.android.steamnews.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameAppIdsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameAppIdItem gameAppidItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GameAppIdItem> gameAppIdItems);

    @Delete
    void delete(GameAppIdItem gameAppidItem);

    @Query("DELETE FROM gameAppIdItems")
    void deleteAll();

    @Query("SELECT * FROM gameAppIdItems ORDER BY name ASC")
    LiveData<List<GameAppIdItem>> getAll();

    @Query("SELECT * FROM gameAppIdItems WHERE name LIKE :query ORDER BY name ASC")
    LiveData<List<GameAppIdItem>> search(String query);
}

