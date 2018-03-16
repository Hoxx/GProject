package com.hxx.xlibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;


import com.hxx.xlibrary.exception.XDBInitializeException;
import com.hxx.xlibrary.util.L;

import java.util.List;

/**
 * Created by HXX on 2017/12/23.
 */

public class XDBClient implements XDBAsyncResultCallback {

    private static XDBClient instance = new XDBClient();

    private XDBAsyncHandler XDBAsyncHandler;
    private XDBHelper xdbHelper;
    private SQLiteDatabase sqLiteDatabase;

    private XDBResultCallback<Cursor> queryResultCallback;
    private XDBResultCallback<Long> insertResultCallback;
    private XDBResultCallback<Integer> updateResultCallback;
    private XDBResultCallback<Integer> deleteResultCallback;

    private boolean isEnable;

    private XDBClient() {
    }

    public static XDBClient getInstance() {
        if (instance == null)
            instance = new XDBClient();
        return instance;
    }

    public void init(Context context, XDBCreateCallback callback) {
        if (callback == null)
            throw new XDBInitializeException("XDBClient not init callback");
        if (TextUtils.isEmpty(callback.DBName()))
            throw new XDBInitializeException("XDBClient db name not is null");
        xdbHelper = new XDBHelper(context, callback);
        sqLiteDatabase = xdbHelper.getWritableDatabase();
        XDBAsyncHandler = new XDBAsyncHandler(sqLiteDatabase);
        XDBAsyncHandler.setAsyncResultCallback(this);
        isEnable = true;
    }

    public boolean isEnable() {
        return isEnable;
    }

    //异步查询
    /*开始查询*/
    public void startAsyncQuery(int token, String table, String[] columns, String selection,
                                String[] selectionArgs, String groupBy, String having, String orderBy,
                                String limit, XDBResultCallback<Cursor> callback) {
        checkNotInit();
        queryResultCallback = callback;
        XDBAsyncHandler.startQuery(token, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /*插入数据*/
    public void startAsyncInsert(int token, String table, String nullColumnHack,
                                 ContentValues initialValues, XDBResultCallback<Long> callback) {
        checkNotInit();
        insertResultCallback = callback;
        XDBAsyncHandler.startInsert(token, table, nullColumnHack, initialValues);
    }

    /*更新数据*/
    public void startAsyncUpdate(int token, String table, ContentValues values, String whereClause,
                                 String[] whereArgs, XDBResultCallback<Integer> callback) {
        checkNotInit();
        updateResultCallback = callback;
        XDBAsyncHandler.startUpdate(token, table, values, whereClause, whereArgs);
    }

    /*删除数据*/
    public void startAsyncDelete(int token, String table, String whereClause, String[] whereArgs,
                                 XDBResultCallback<Integer> callback) {
        checkNotInit();
        deleteResultCallback = callback;
        XDBAsyncHandler.startDelete(token, table, whereClause, whereArgs);
    }

    /*取消操作*/
    public void cancelAsyncOperation() {
        checkNotInit();
        XDBAsyncHandler.cancelOperation();
    }

    //异步查询结果

    @Override
    public void onQueryComplete(int token, Cursor cursor) {
        if (queryResultCallback != null)
            queryResultCallback.call(token, cursor);
    }

    @Override
    public void onInsertComplete(int token, long result) {
        if (insertResultCallback != null)
            insertResultCallback.call(token, result);
    }

    @Override
    public void onUpdateComplete(int token, int result) {
        if (updateResultCallback != null)
            updateResultCallback.call(token, result);
    }

    @Override
    public void onDeleteComplete(int token, int result) {
        if (deleteResultCallback != null)
            deleteResultCallback.call(token, result);
    }

    //同步查询
    /*开始查询*/
    public Cursor startQuery(String table, String[] columns, String selection, String[] selectionArgs,
                             String groupBy, String having, String orderBy, String limit) {
        checkNotInit();
        if (sqLiteDatabase == null) return null;
        return sqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /*插入数据*/
    public long startInsert(String table, String nullColumnHack, ContentValues initialValues) {
        checkNotInit();
        if (sqLiteDatabase == null) return -1;
        return sqLiteDatabase.insert(table, nullColumnHack, initialValues);
    }

    /*更新数据*/
    public int startUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) {
        checkNotInit();
        if (sqLiteDatabase == null) return -1;
        return sqLiteDatabase.update(table, values, whereClause, whereArgs);
    }

    /*删除数据*/
    public int startDelete(String table, String whereClause, String[] whereArgs) {
        checkNotInit();
        if (sqLiteDatabase == null) return -1;
        return sqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    private void checkNotInit() {
        if (xdbHelper == null) throw new XDBInitializeException("XDBClient not init");
    }

    public boolean insertMoreData(String table, List<String> tableColumnName, List<List<Object>> values) {
        checkNotInit();
        if (tableColumnName == null || values == null || TextUtils.isEmpty(table) || tableColumnName.size() <= 0 || values.size() <= 0)
            return false;
        try {
            String sql = getInsertMoreValue(table, tableColumnName);
            L.e("添加多条数据：" + sql);
            sqLiteDatabase.beginTransaction();
            for (List<Object> value : values) {
                Object[] objects = new Object[value.size()];
                for (int i = 0; i < value.size(); i++) {
                    objects[i] = value.get(i);
                }
                sqLiteDatabase.execSQL(sql, objects);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 结束事务
            sqLiteDatabase.endTransaction();
        }
        return true;
    }

    private String getInsertMoreValue(String table, List<String> tableColumnNames) {
        int size = tableColumnNames.size();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");
        stringBuilder.append(table);
        stringBuilder.append("(");
        for (int i = 0; i < size; i++) {
            String columnName = tableColumnNames.get(i);
            stringBuilder.append(columnName);
            if (i != size - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(") VALUES (");
        for (int i = 0; i < size; i++) {

            stringBuilder.append("?");
            if (i != size - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
