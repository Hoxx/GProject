package com.hxx.xlibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


/**
 * Created by HXX on 2017/12/23.
 */

public class XDBAsyncHandler extends Handler {

    private static final int EVENT_ARG_QUERY = 1;
    private static final int EVENT_ARG_INSERT = 2;
    private static final int EVENT_ARG_UPDATE = 3;
    private static final int EVENT_ARG_DELETE = 4;

    private SQLiteDatabase sqLiteDatabase;

    private static Looper sLooper = null;

    private Handler mWorkerThreadHandler;

    private XDBAsyncResultCallback asyncResultCallback;

    protected static final class WorkerArgs {
        SQLiteDatabase sqLiteDatabase;
        Handler handler;
        String table;
        //Query
        String[] columns;
        String selection;
        String[] selectionArgs;
        String groupBy;
        String having;
        String orderBy;
        String limit;
        //insert
        String nullColumnHack;
        //update
        String whereClause;
        String[] whereArgs;

        Object result;
        ContentValues values;
    }

    protected class WorkerHandler extends Handler {

        WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            WorkerArgs args = (WorkerArgs) msg.obj;
            if (args == null)
                return;
            SQLiteDatabase sqLiteDatabase = args.sqLiteDatabase;
            if (sqLiteDatabase == null)
                return;
            int token = msg.what;
            int event = msg.arg1;
            switch (event) {
                case EVENT_ARG_QUERY:
                    Cursor cursor;
                    try {
                        cursor = sqLiteDatabase.query(args.table, args.columns, args.selection,
                                args.selectionArgs, args.groupBy, args.having, args.orderBy, args.limit);
                        // Calling getCount() causes the cursor window to be filled,
                        // which will make the first access on the main thread a lot faster.
                        if (cursor != null) {
                            cursor.getCount();
                        }
                    } catch (Exception e) {
                        cursor = null;
                    }

                    args.result = cursor;
                    break;

                case EVENT_ARG_INSERT:
                    args.result = sqLiteDatabase.insert(args.table, args.nullColumnHack, args.values);
                    break;

                case EVENT_ARG_UPDATE:
                    args.result = sqLiteDatabase.update(args.table, args.values, args.whereClause, args.whereArgs);
                    break;

                case EVENT_ARG_DELETE:
                    args.result = sqLiteDatabase.delete(args.table, args.whereClause, args.whereArgs);
                    break;
            }

            // passing the original token value back to the caller
            // on top of the event values in arg1.
            Message reply = args.handler.obtainMessage(token);
            reply.obj = args;
            reply.arg1 = msg.arg1;

            reply.sendToTarget();
        }
    }

    public XDBAsyncHandler(SQLiteDatabase sqLiteDatabase) {
        super(Looper.getMainLooper());
        this.sqLiteDatabase = sqLiteDatabase;
        synchronized (XDBAsyncHandler.class) {
            if (sLooper == null) {
                HandlerThread thread = new HandlerThread("DBAsyncWorker");
                thread.start();
                sLooper = thread.getLooper();
            }
        }
        mWorkerThreadHandler = createHandler(sLooper);
    }

    public void setAsyncResultCallback(XDBAsyncResultCallback asyncResultCallback) {
        this.asyncResultCallback = asyncResultCallback;
    }

    private Handler createHandler(Looper looper) {
        return new WorkerHandler(looper);
    }

    /*开始查询*/
    public void startQuery(int token, String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy, String having,
                           String orderBy, String limit) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_QUERY;

        WorkerArgs args = new WorkerArgs();
        args.sqLiteDatabase = sqLiteDatabase;
        args.handler = this;
        args.table = table;
        args.columns = columns;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        args.groupBy = groupBy;
        args.having = having;
        args.orderBy = orderBy;
        args.limit = limit;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /*取消操作*/
    public final void cancelOperation() {
        mWorkerThreadHandler.removeCallbacksAndMessages(null);
    }

    /*插入数据*/
    public final void startInsert(int token, String table, String nullColumnHack,
                                  ContentValues initialValues) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_INSERT;

        WorkerArgs args = new WorkerArgs();
        args.sqLiteDatabase = sqLiteDatabase;
        args.handler = this;
        args.table = table;
        args.nullColumnHack = nullColumnHack;
        args.values = initialValues;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /*更新数据*/
    public final void startUpdate(int token, String table, ContentValues values, String whereClause, String[] whereArgs) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_UPDATE;

        WorkerArgs args = new WorkerArgs();
        args.sqLiteDatabase = sqLiteDatabase;
        args.handler = this;
        args.table = table;
        args.values = values;
        args.whereClause = whereClause;
        args.whereArgs = whereArgs;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /*删除数据*/
    public final void startDelete(int token, String table, String whereClause, String[] whereArgs) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_DELETE;

        WorkerArgs args = new WorkerArgs();
        args.sqLiteDatabase = sqLiteDatabase;
        args.handler = this;
        args.table = table;
        args.whereClause = whereClause;
        args.whereArgs = whereArgs;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /*查询数据结果*/
    private void onQueryComplete(int token, Cursor cursor) {
        if (asyncResultCallback != null)
            asyncResultCallback.onQueryComplete(token, cursor);
    }

    /*插入数据结果*/
    private void onInsertComplete(int token, long result) {
        if (asyncResultCallback != null)
            asyncResultCallback.onInsertComplete(token, result);
    }

    /*更新数据结果*/
    private void onUpdateComplete(int token, int result) {
        if (asyncResultCallback != null)
            asyncResultCallback.onUpdateComplete(token, result);
    }

    /*删除数据结果*/
    private void onDeleteComplete(int token, int result) {
        if (asyncResultCallback != null)
            asyncResultCallback.onDeleteComplete(token, result);
    }

    @Override
    public void handleMessage(Message msg) {
        WorkerArgs args = (WorkerArgs) msg.obj;

        int token = msg.what;
        int event = msg.arg1;

        // pass token back to caller on each callback.
        switch (event) {
            case EVENT_ARG_QUERY:
                onQueryComplete(token, (Cursor) args.result);
                break;

            case EVENT_ARG_INSERT:
                onInsertComplete(token, (long) args.result);
                break;

            case EVENT_ARG_UPDATE:
                onUpdateComplete(token, (Integer) args.result);
                break;

            case EVENT_ARG_DELETE:
                onDeleteComplete(token, (Integer) args.result);
                break;
        }
    }
}
