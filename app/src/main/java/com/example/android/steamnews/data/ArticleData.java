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
    public ArrayList<ArticleDataItem> items;


    public ArticleData (){
        this.items = null;
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
            JsonObject resultsObj = json.getAsJsonObject();
            JsonObject appNewsObj = resultsObj.getAsJsonObject("appnews");
            JsonArray list = appNewsObj.getAsJsonArray("newsitems");

            ArticleData articleList = new ArticleData();
            if(list != null){
                for(int i =0; i< list.size(); i++){
                    JsonObject element = list.get(i).getAsJsonObject();
                    ArticleDataItem item = new ArticleDataItem(
                            element.getAsJsonPrimitive("title").getAsString(),
                            element.getAsJsonPrimitive("url").getAsString()
                    );
                    articleList.items.add(item);
                }
            }
        //Log.d(TAG, "Here is the article data that we deserialized: " + articleList);
        return articleList;
        }



    }
}
