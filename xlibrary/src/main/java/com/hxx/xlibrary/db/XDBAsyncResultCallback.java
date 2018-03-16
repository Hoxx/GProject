package com.hxx.xlibrary.db;

import android.database.Cursor;

/**
 * Created by HXX on 2017/12/24.
 */

public interface XDBAsyncResultCallback {

    /*查询数据结果*/
    void onQueryComplete(int token, Cursor cursor);

    /*插入数据结果*/
    void onInsertComplete(int token, long result);

    /*更新数据结果*/
    void onUpdateComplete(int token, int result);

    /*删除数据结果*/
    void onDeleteComplete(int token, int result);

}
