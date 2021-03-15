package com.example.android.steamnews.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrendingDataList {
    private static final String TAG = GameAppIdList.class.getSimpleName();

    public ArrayList<TrendingDataItem> items;

    public TrendingDataList(){
        this.items = new ArrayList<>();
    }

    public static class JsonDeserializer<gameAppIdList> implements com.google.gson.JsonDeserializer<TrendingDataList> {
        @Override
        public TrendingDataList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            //JsonObject appListObj = resultsObj.getAsJsonObject("applist");
            //JsonArray appList = appListObj.getAsJsonArray("apps");

            TrendingDataList gameAppIdList = new TrendingDataList();

            if (resultsObj != null){
                JsonObject element = resultsObj.getAsJsonObject();
                TrendingDataItem item = new TrendingDataItem(
                        element.getAsJsonPrimitive("appid").getAsInt(),
                        element.getAsJsonPrimitive("name").getAsString());
                gameAppIdList.items.add(item);
                }
            Log.d(TAG, "here is the appid in the main activity: " + gameAppIdList);
            return gameAppIdList;
            }



    }
}
