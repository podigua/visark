package com.podigua.visark.server.data.utils;

/**
 * @author: podigua
 * @create: 2021-09-15 18:50
 **/
public class QueryUtils {
    /**
     * 获取Lang类型
     *
     * @param query
     * @param key
     * @return
     */
    public static Long getLong(String query, String key) {
        String value = getString(query, key);
        if (value == null) {
            return null;
        }
        return Long.parseLong(value);
    }

    /**
     * 获取字符串
     *
     * @param query
     * @param key
     * @return
     */
    public static String getString(String query, String key) {
        return getQuery(query, key);
    }

    private static String getQuery(String query, String key) {
        String[] array = query.split("&");
        for (String fieldValue : array) {
            String[] keyValue = fieldValue.split("=");
            if (keyValue.length == 2) {
                if (key.equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return "";
    }
}
