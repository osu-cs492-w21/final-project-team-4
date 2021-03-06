package com.example.android.steamnews.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GameAppIdList {
    private static final String TAG = GameAppIdList.class.getSimpleName();

    public ArrayList<GameAppIdItem> items;

    public GameAppIdList(){
        this.items = new ArrayList<>();
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<GameAppIdList> {
        @Override
        public GameAppIdList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonObject appListObj = resultsObj.getAsJsonObject("applist");
            JsonArray appList = appListObj.getAsJsonArray("apps");

            GameAppIdList gameAppIdList = new GameAppIdList();

            if (appList != null){
               for (int i = 0; i < appList.size(); i++) {
                   JsonObject element = appList.get(i).getAsJsonObject();
                   GameAppIdItem item = new GameAppIdItem(
                           element.getAsJsonPrimitive("appid").getAsInt(),
                           element.getAsJsonPrimitive("name").getAsString(),
                           false);
                   gameAppIdList.items.add(item);
               }
            }

            return gameAppIdList;
        }
    }
}
