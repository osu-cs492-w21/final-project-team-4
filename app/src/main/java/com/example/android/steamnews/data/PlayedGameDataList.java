package com.example.android.steamnews.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlayedGameDataList {
    private static final String TAG = GameAppIdList.class.getSimpleName();

    public ArrayList<PlayedGameData> items;

    public PlayedGameDataList(){
        this.items = new ArrayList<>();
    }

    public static class JsonDeserializer implements com.google.gson.JsonDeserializer<PlayedGameDataList> {
        @Override
        public PlayedGameDataList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject resultsObj = json.getAsJsonObject();
            JsonObject responseObj = resultsObj.getAsJsonObject("response");
            JsonArray gameList = responseObj.getAsJsonArray("games");

            PlayedGameDataList playedGameDataList = new PlayedGameDataList();

            if (gameList != null){
                for (int i = 0; i < gameList.size(); i++) {
                    JsonObject element = gameList.get(i).getAsJsonObject();
                    PlayedGameData item = new PlayedGameData(
                            element.getAsJsonPrimitive("appid").getAsInt(),
                            element.getAsJsonPrimitive("name").getAsString(),
                            element.getAsJsonPrimitive("playtime_2weeks").getAsInt(),
                            element.getAsJsonPrimitive("playtime_forever").getAsInt());
                    playedGameDataList.items.add(item);
                }
            }

            return playedGameDataList;
        }
    }
}
