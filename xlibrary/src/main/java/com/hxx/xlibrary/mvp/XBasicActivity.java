package com.hxx.xlibrary.mvp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Android on 2018/1/11.
 */
public abstract class XBasicActivity extends AppCompatActivity {

    //是否支持ToolBar
    private boolean toolBarSupport;

    public <V extends View> V F(int viewResID) {
        return (V) findViewById(viewResID);
    }

    public void startActivity(Class cls) {
        startActivity(new Intent(this, cls));
    }

    public void startActivity(Class cls, String key, String data) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, data);
        startActivity(intent);
    }

    public void startActivityFinish(Class cls) {
        startActivity(new Intent(this, cls));
        finish();
    }

    public void initToolbarBackWhite(Toolbar toolbar) {
        initToolbar(toolbar, null, true, Color.WHITE);
    }

    public void initToolbarBackWhite(Toolbar toolbar, int title) {
        initToolbar(toolbar, getString(title), true, Color.WHITE);
    }

    public void initToolbar(Toolbar toolbar, String title, boolean canBack, int color) {
        if (toolbar == null) return;
        toolBarSupport = true;
        if (!TextUtils.isEmpty(title))
            toolbar.setTitle(title);
        toolbar.setTitleTextColor(color);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setHomeButtonEnabled(canBack);//设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!toolBarSupport) return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
