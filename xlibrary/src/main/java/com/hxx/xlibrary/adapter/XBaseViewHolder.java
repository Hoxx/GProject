package com.hxx.xlibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Android on 2018/1/17.
 */

public abstract class XBaseViewHolder extends RecyclerView.ViewHolder {

    private View view;

    public XBaseViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public <T extends View> T F(int id) {
        return (T) view.findViewById(id);
    }
}
