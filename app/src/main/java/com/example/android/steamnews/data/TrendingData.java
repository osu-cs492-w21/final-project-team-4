package com.example.android.steamnews.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TrendingData {
    private static final String TAG = TrendingData.class.getSimpleName();
    public ArrayList<TrendingDataItem> items;


    public TrendingData (){
        this.items = new ArrayList<>();
    }


    public ArrayList<TrendingDataItem> getArticleData() {
        return this.items;
    }

    //class for deserializing the json data
    // appnewsObj = name of the appnews object
    //articleList = list of articles for the specified game
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<TrendingData> {
        @Override
        public TrendingData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Log.d(TAG, "Hrer is the json: " + json);
            JsonObject resultsObj = json.getAsJsonObject();
            Log.d(TAG, "Hrer is the result: " + resultsObj);
            //JsonObject appNewsListObj = resultsObj.getAsJsonObject("appnews");
            //Log.d(TAG, "Here is the appnews: " + appNewsListObj);
            Gson gson = new Gson();
            String newsList =  resultsObj.getAsJsonObject("570").get("name").getAsString();
            Log.d(TAG, "Here is the newsitems: " + newsList);

            TrendingData articleData = new TrendingData();
            if (resultsObj != null){
                JsonObject element = resultsObj.getAsJsonObject("570");
                TrendingDataItem item = new TrendingDataItem(
                        element.getAsJsonPrimitive("appid").getAsInt(),
                        element.getAsJsonPrimitive("name").getAsString());
                articleData.items.add(item);
            }
            Log.d(TAG, "Here is the articledata: " + articleData.items.get(0).name);
            return articleData;
        }



    }
}
