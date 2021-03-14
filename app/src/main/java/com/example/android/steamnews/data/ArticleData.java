package com.example.android.steamnews.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArticleData {
    public static final String TAG = ArticleData.class.getSimpleName();
    public ArrayList<ArticleDataItem> articleData;


    public ArticleData (){
        this.articleData = new ArrayList<>();
    }


    public ArrayList<ArticleDataItem> getArticleData() {
        return this.articleData;
    }

    //class for deserializing the json data
    // appnewsObj = name of the appnews object
    //articleList = list of articles for the specified game
    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<ArticleData> {
        @Override
        public ArticleData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonObject appNewsObj = resultsObj.getAsJsonObject("appnews");
            JsonArray list = resultsObj.getAsJsonArray("newsitems");

            ArticleData articleList = new ArticleData();
            if(list != null){
                for(int i =0; i< list.size(); i++){
                    JsonObject element = list.get(i).getAsJsonObject();
                    ArticleDataItem item = new ArticleDataItem(
                            element.getAsJsonPrimitive("title").getAsString(),
                            element.getAsJsonPrimitive("url").getAsString()
                    );
                    articleList.articleData.add(item);
                }
            }

        return articleList;
        }



    }
}
