package com.app.baselib.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by Wang on 2017/7/21.
 */

public class GsonUtils {

    private static Gson gson;
    private static Gson getGson(){
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }
//    public static <T> List<T> getList(String jsonData) {
//         List<T> result = getGson().fromJson(jsonData, new TypeToken<List<T>>() {}.getType());
//        return result;
//    }
    /**
     *将Json数组解析成相应的映射对象列表
     */
    public static  <T> List<T> getList(String json, Class<T> cls) {
//        return JSON.parseArray(json,cls);
        ArrayList<T> mList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            mList.add(getGson().fromJson(elem, cls));
        }
        return mList;
    }
    /**
     *从json数据得到对象
     */
    public static <T> T getObject(String json,Class<T> tClass){
        return getGson().fromJson(json,tClass);
    }
    /**
     *根据对象解析得到json数据
     */
    public static String getJsonFromObject(Object object){
       return getGson().toJson(object);
    }

    /**
     *根据对象解析得到json数据
     */
    public static <T extends Object> String getJsonFromList(List<T> objectList){
        return getGson().toJson(objectList);
    }

}
