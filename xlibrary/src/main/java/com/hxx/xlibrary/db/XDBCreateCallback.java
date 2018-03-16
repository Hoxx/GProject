package com.hxx.xlibrary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HXX on 2017/12/24.
 */

public interface XDBCreateCallback {

    void onCreate(SQLiteDatabase db);

    String DBName();

}
