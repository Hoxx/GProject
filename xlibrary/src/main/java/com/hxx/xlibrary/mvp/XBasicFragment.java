package com.hxx.xlibrary.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Android on 2018/1/20.
 */

public abstract class XBasicFragment extends Fragment {

    protected Activity activity;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Fragment和Activity相关联时调用。可以通过该方法获取Activity引用，还可以通过getArguments()获取参数
        this.context = context;
        this.activity = ((Activity) context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //当Fragment和Activity解除关联时调用
        if (activity != null)
            activity = null;
        if (context != null)
            context = null;
    }

    public <V extends View> V F(int viewResID) {
        return ((V) getView().findViewById(viewResID));
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, String key, String data) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(key, data);
        startActivity(intent);
    }

}
