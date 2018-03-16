package com.hxx.xlibrary.cache;

/**
 * Created by HXX on 2017/12/24.
 */

public class XCacheConstant {

    static String CACHE_DB_NAME = "XCache.db";
    static int CACHE_DB_VERSION = 1;

    static String CACHE_DB_TABLE_NAME = "cache";
    static String CACHE_DB_TABLE_KEY = "cache_key";
    static String CACHE_DB_TABLE_VALUE = "cache_value";
    static String CACHE_DB_TABLE_SAVE_TIME = "cache_save_time";
    static String CACHE_DB_TABLE_VALID_TIME = "cache_life_time";
    static String CACHE_DB_TABLE_OVERDUE_TIME = "cache_overdue_time";

    static String CACHE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " +
            CACHE_DB_TABLE_NAME + " (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            CACHE_DB_TABLE_KEY + " TEXT NOT NULL," +
            CACHE_DB_TABLE_VALUE + " TEXT NOT NULL," +
            CACHE_DB_TABLE_SAVE_TIME + " INTEGER NOT NULL," +
            CACHE_DB_TABLE_VALID_TIME + " INTEGER NOT NULL," +
            CACHE_DB_TABLE_OVERDUE_TIME + " INTEGER NOT NULL" +
            ")";

    //异步查询缓存消息Token
    static int CACHE_DB_QUERY_TOKEN = 1;
    static int CACHE_DB_INSERT_TOKEN = 2;
    static int CACHE_DB_UPDATE_TOKEN = 3;
    static int CACHE_DB_DELETE_TOKEN = 4;
    //未定义缓存有效时间（不会过期）
    public static int NO_VALID_TIME = -0XA1;
    static int NO_OVERDUE_TIME = -0XA2;

}
