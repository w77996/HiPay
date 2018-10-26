package com.w77996.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * json解析工具
 * @author w77996
 */
public class JsonUtil {

    static {
        System.setProperty("fastjson.compatibleWithJavaBean", "true");
    }

    /**
     * 对象转换成json
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        if (object == null) {
            return null;
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * json转换成对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    /**
     * json 转 list
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getObjectListFromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return JSON.parseArray(json, clazz);
    }

    /**
     * String 转json
     * @param json
     * @return
     */
    public static JSONObject getJSONObjectFromJson(String json) {
        if (json == null) {
            return null;
        }
        return JSONObject.parseObject(json);
    }

    /**
     * object 转 json
     * @param object
     * @return
     */
    public static JSONObject getJSONObjectFromObj(Object object) {
        if (object == null) {
            return null;
        }
        return (JSONObject) JSONObject.toJSON(object);
    }

}
