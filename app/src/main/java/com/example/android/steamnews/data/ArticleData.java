package com.example.android.steamnews.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArticleData {
    private static final String TAG = ArticleData.class.getSimpleName();
    public ArrayList<ArticleDataItem> items;


    public ArticleData (){
        this.items = new ArrayList<>();
    }


    public ArrayList<ArticleDataItem> getArticleData() {
        return this.items;
    }

    //class for deserializing the json data
    // appnewsObj = name of the appnews object
    //articleList = list of articles for the specified game
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<ArticleData> {
        @Override
        public ArticleData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Log.d(TAG, "Hrer is the json: " + json);
            JsonObject resultsObj = json.getAsJsonObject();
            JsonObject appNewsListObj = resultsObj.getAsJsonObject("appnews");
            Log.d(TAG, "Here is the appnews: " + appNewsListObj);
            JsonArray newsList = appNewsListObj.getAsJsonArray("newsitems");
            Log.d(TAG, "Here is the newsitems: " + newsList);

            ArticleData articleData = new ArticleData();
            if(newsList != null){
                for(int i=0; i <newsList.size(); i++){
                    JsonObject element = newsList.get(i).getAsJsonObject();
                    ArticleDataItem item = new ArticleDataItem(
                            element.getAsJsonPrimitive("title").getAsString(),
                            element.getAsJsonPrimitive("url").getAsString()
                    );
                    articleData.items.add(item);
                }
            }
         return articleData;
        }



    }
}
