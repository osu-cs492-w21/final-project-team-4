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
import java.util.Map;
import java.util.Set;

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
            String newsList =  resultsObj.getAsJsonObject("570").get("name").getAsString();
            Log.d(TAG, "Here is the newsitems: " + newsList);

            Set<Map.Entry<String, JsonElement>> newList =  resultsObj.entrySet();
            Log.d(TAG, "Here is the ems: " + newList);

            int[] intArray = new int[]{570, 730, 440, 578080, 304930, 230410, 550, 271590, 105600,
                    945360,252490,359550,4000,291550,236390,340,10,1172470,238960,218620,
                    444090,272060,892970};


            TrendingData articleData = new TrendingData();
            if (resultsObj != null){
                for(int i = 0; intArray.length > i; i++) {
                    Log.d(TAG, "array num: " + String.valueOf(intArray[i]));
                    JsonObject element = resultsObj.getAsJsonObject(String.valueOf(intArray[i]));
                    TrendingDataItem item = new TrendingDataItem(
                            element.getAsJsonPrimitive("appid").getAsInt(),
                            element.getAsJsonPrimitive("name").getAsString());
                    articleData.items.add(item);
                }
            }
//            Log.d(TAG, "Here is the articledata: " + articleData.items.get(0).name);
            return articleData;
        }



    }
}
