package me.zhenxin.zmusic.music.searchSource;

import com.google.gson.*;
import me.zhenxin.zmusic.config.Config;
import me.zhenxin.zmusic.utils.NetUtils;

import java.net.URLEncoder;

public class BiliBiliMusic {

    public static JsonObject getMusic(String keyword) {
        try {
            Gson gson = new GsonBuilder().create();
            String musicId;
            String key = keyword.toLowerCase();
            if (key.contains("-id:au")||key.contains("-id:av")||key.contains("-id:bv")) {
                musicId = keyword.split("-id:")[1];
            } else {
                keyword = URLEncoder.encode(keyword, "UTF-8");
                String searchUrl = "https://api.bilibili.com/audio/music-service-c/s?keyword=" + keyword + "&pagesize=1";
                String searchJsonText = NetUtils.getNetStringBiliBili(searchUrl, null);
                JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
                JsonObject searchResult = searchJson.get("data").getAsJsonObject().get("result").getAsJsonArray().get(0).getAsJsonObject();
                musicId = searchResult.get("id").getAsString();
            }
            String getInfo = Config.bilibiliApiRoot + "?id=" + musicId;
            String infoJsonText = NetUtils.getNetStringBiliBili(getInfo, null);
            infoJsonText = infoJsonText.trim();
            JsonObject infoJson = gson.fromJson(infoJsonText, JsonObject.class);
            String musicName = infoJson.get("data").getAsJsonObject().get("title").getAsString();
            String musicSinger = infoJson.get("data").getAsJsonObject().get("author").getAsString();
            int musicTime = infoJson.get("data").getAsJsonObject().get("duration").getAsInt();
            String lyric = infoJson.get("data").getAsJsonObject().get("lyric").getAsString();
            String musicUrl = infoJson.get("data").getAsJsonObject().get("musicUrl").getAsString();
            JsonObject returnJSON = new JsonObject();
            returnJSON.addProperty("id", musicId);
            returnJSON.addProperty("url", musicUrl);
            returnJSON.addProperty("time", musicTime);
            returnJSON.addProperty("name", musicName);
            returnJSON.addProperty("singer", musicSinger);
            returnJSON.addProperty("lyric", lyric);
            returnJSON.addProperty("lyricTr", "");
            returnJSON.addProperty("error", "");
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonArray getMusicList(String keyword) {
        try {
            Gson gson = new GsonBuilder().create();
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String searchUrl = "https://api.bilibili.com/audio/music-service-c/s?keyword=" + keyword + "&pagesize=10";
            String searchJsonText = NetUtils.getNetStringBiliBili(searchUrl, null);
            JsonObject searchJson = gson.fromJson(searchJsonText, JsonObject.class);
            JsonArray searchResultList = searchJson.get("data").getAsJsonObject().get("result").getAsJsonArray();
            JsonArray returnJSON = new JsonArray();
            for (JsonElement json : searchResultList) {
                String musicName = json.getAsJsonObject().get("title").getAsString();
                String musicSinger = json.getAsJsonObject().get("author").getAsString();
                String musicId = json.getAsJsonObject().get("id").getAsString();
                JsonObject returnJSONObj = new JsonObject();
                returnJSONObj.addProperty("id", "au" + musicId);
                returnJSONObj.addProperty("name", musicName);
                returnJSONObj.addProperty("singer", musicSinger);
                returnJSON.add(returnJSONObj);
            }
            return returnJSON;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
