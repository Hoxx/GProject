package com.hxx.xlibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.hxx.xlibrary.util.L;


/**
 * Created by HXX on 2017/12/23.
 */

public class XDBHelper extends SQLiteOpenHelper {

    private XDBCreateCallback callback;

    public XDBHelper(Context context, XDBCreateCallback callback) {
        super(context, callback != null ? !TextUtils.isEmpty(callback.DBName()) ? callback.DBName() : "x_temp.db" : "x_temp.db", null, callback != null ? callback instanceof XDBActionCallback ? ((XDBActionCallback) callback).DBVersion() >= 1 ? ((XDBActionCallback) callback).DBVersion() : 1 : 1 : 1);
        this.callback = callback;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        callback.onCreate(db);
        L.e("XDBHelper-onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (callback != null && callback instanceof XDBActionCallback) {
            ((XDBActionCallback) callback).onUpgrade(db, oldVersion, newVersion);
            L.e("XDBHelper-onUpgrade");
        }
    }
}
