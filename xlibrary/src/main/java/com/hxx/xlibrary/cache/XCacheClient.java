package com.hxx.xlibrary.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hxx.xlibrary.db.XDBActionCallback;
import com.hxx.xlibrary.db.XDBAsyncHandler;
import com.hxx.xlibrary.db.XDBAsyncResultCallback;
import com.hxx.xlibrary.db.XDBHelper;
import com.hxx.xlibrary.util.L;
import com.hxx.xlibrary.util.XGson;


/**
 * Created by HXX on 2017/12/24.
 */

public class XCacheClient implements XCacheContract, XDBActionCallback, XDBAsyncResultCallback {

    private static XCacheClient instance = new XCacheClient();
    private Context context;
    private boolean enable;

    private XDBAsyncHandler xdbAsyncHandler;

    private Gson gson;
    private XCacheResultCallback<String> cacheResultCallback;

    public static XCacheClient getInstance() {
        if (instance == null)
            instance = new XCacheClient();
        return instance;
    }

    private XCacheClient() {
        gson = XGson.getInstance().getGson();
    }

    @Override
    public void init(Context context) {
        this.context = context;
        XDBHelper xdbHelper = new XDBHelper(context, this);
        xdbAsyncHandler = new XDBAsyncHandler(xdbHelper.getWritableDatabase());
        xdbAsyncHandler.setAsyncResultCallback(this);
        enable = true;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public <V> void save(String key, V value) {
        save(key, value, XCacheConstant.NO_VALID_TIME);
    }

    @Override
    public <V> void save(String key, V value, long validTime) {
        saveValue(key, gson.toJson(value), validTime);
    }

    @Override
    public void saveValue(String key, String value) {
        saveValue(key, value, XCacheConstant.NO_VALID_TIME);
    }

    @Override
    public void saveValue(final String key, final String value, final long validTime) {
        getValue(key, new XCacheResultCallback<String>() {
            @Override
            public void call(String data) {
                if (TextUtils.isEmpty(data)) {
                    insert(key, value, validTime);
                } else {
                    update(key, value, validTime);
                }
            }
        });
    }

    @Override
    public <V> void get(String key, final Class<V> clz, final XCacheResultCallback<V> callback) {
        if (callback != null) {
            getValue(key, new XCacheResultCallback<String>() {
                @Override
                public void call(String value) {
                    if (TextUtils.isEmpty(value)) {
                        callback.call(null);
                    } else {
                        callback.call(gson.fromJson(value, clz));
                    }
                }
            });
        }
    }


    @Override
    public void getValue(String key, XCacheResultCallback<String> callback) {
        this.cacheResultCallback = callback;
        query(key);
    }

    @Override
    public void clearCache(String key) {
        delete(key);
    }

    @Override
    public void clearAllCache() {
        deleteDB();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(XCacheConstant.CACHE_TABLE_SQL);
    }

    @Override
    public String DBName() {
        return XCacheConstant.CACHE_DB_NAME;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //缓存数据库升级
    }

    @Override
    public int DBVersion() {
        return XCacheConstant.CACHE_DB_VERSION;
    }

    private void query(String key) {
        xdbAsyncHandler.startQuery(XCacheConstant.CACHE_DB_QUERY_TOKEN, XCacheConstant.CACHE_DB_TABLE_NAME,
                null, XCacheConstant.CACHE_DB_TABLE_KEY + " = ?", new String[]{key},
                null, null, null, null);
    }

    private void insert(String key, String value, long validTime) {
        long saveTime;
        long overdueTime;
        if (validTime == XCacheConstant.NO_VALID_TIME) {
            saveTime = 0;
            overdueTime = XCacheConstant.NO_OVERDUE_TIME;
        } else {
            saveTime = System.currentTimeMillis();
            overdueTime = saveTime + validTime;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_KEY, key);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_VALUE, value);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_SAVE_TIME, saveTime);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_VALID_TIME, validTime);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_OVERDUE_TIME, overdueTime);
        xdbAsyncHandler.startInsert(XCacheConstant.CACHE_DB_INSERT_TOKEN, XCacheConstant.CACHE_DB_TABLE_NAME,
                null, contentValues);
    }

    private void update(String key, String value, long validTime) {
        long saveTime;
        long overdueTime;
        if (validTime == XCacheConstant.NO_VALID_TIME) {
            saveTime = 0;
            overdueTime = XCacheConstant.NO_OVERDUE_TIME;
        } else {
            saveTime = System.currentTimeMillis();
            overdueTime = saveTime + validTime;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_KEY, key);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_VALUE, value);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_SAVE_TIME, saveTime);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_VALID_TIME, validTime);
        contentValues.put(XCacheConstant.CACHE_DB_TABLE_OVERDUE_TIME, overdueTime);
        xdbAsyncHandler.startUpdate(XCacheConstant.CACHE_DB_UPDATE_TOKEN, XCacheConstant.CACHE_DB_TABLE_NAME,
                contentValues, XCacheConstant.CACHE_DB_TABLE_KEY + " = ?", new String[]{key});
    }

    private void delete(String key) {
        xdbAsyncHandler.startDelete(XCacheConstant.CACHE_DB_DELETE_TOKEN, XCacheConstant.CACHE_DB_TABLE_NAME,
                XCacheConstant.CACHE_DB_TABLE_KEY + " = ?", new String[]{key});
    }

    private void deleteDB() {
        if (context != null)
            context.deleteDatabase(XCacheConstant.CACHE_DB_NAME);
    }


    @Override
    public void onQueryComplete(int token, Cursor cursor) {
        String result = "";
        if (cursor == null || cursor.getCount() <= 0) result = "";
        else {
            cursor.moveToFirst();
            int keyColumnIndex = cursor.getColumnIndexOrThrow(XCacheConstant.CACHE_DB_TABLE_KEY);
            int valueColumnIndex = cursor.getColumnIndexOrThrow(XCacheConstant.CACHE_DB_TABLE_VALUE);
            int overdueTimeColumnIndex = cursor.getColumnIndexOrThrow(XCacheConstant.CACHE_DB_TABLE_OVERDUE_TIME);
            if (!cursor.isNull(valueColumnIndex) && !cursor.isNull(overdueTimeColumnIndex)) {
                long overdueTime = cursor.getLong(overdueTimeColumnIndex);
                if (overdueTime == XCacheConstant.NO_OVERDUE_TIME || overdueTime > System.currentTimeMillis()) {//是否过期
                    result = cursor.getString(valueColumnIndex);
                } else {
                    delete(cursor.getString(keyColumnIndex));
                }
            }
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();
        if (cacheResultCallback != null)
            cacheResultCallback.call(result);
    }

    @Override
    public void onInsertComplete(int token, long result) {
        L.e("XCache Save Result:" + result);
    }

    @Override
    public void onUpdateComplete(int token, int result) {
        L.e("XCache Update Result:" + result);
    }

    @Override
    public void onDeleteComplete(int token, int result) {
        L.e("XCache Delete Result:" + result);
    }
}
