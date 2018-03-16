package com.hxx.xlibrary.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HXX on 2017/12/24.
 */

public interface XDBActionCallback extends XDBCreateCallback {

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    int DBVersion();
}
