package com.hxx.xlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Android on 2017/6/14 .
 */

public abstract class XBaseAdapter<T, VH extends XBaseViewHolder> extends RecyclerView.Adapter<VH> {

    public Context context;
    public ArrayList<T> list;
    public OnAdapterItemListener onAdapterItemListener;

    public XBaseAdapter(Context context, ArrayList<T> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnAdapterItemListener(OnAdapterItemListener onAdapterItemListener) {
        this.onAdapterItemListener = onAdapterItemListener;
    }

    public View getView(int id, ViewGroup view) {
        return LayoutInflater.from(context).inflate(id, view, false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (list == null || list.size() <= 0) return;
        setData(holder, list.get(position), position);
        holder.itemView.setOnClickListener(new AdapterListener(position, onAdapterItemListener));
    }

    public abstract void setData(VH holder, T bean, int position);

    public interface OnAdapterItemListener {

        void onAdapterItemClick(int position);
    }

    public class AdapterListener implements View.OnClickListener {

        private int position;
        private OnAdapterItemListener onAdapterItemListener;

        AdapterListener(int position, OnAdapterItemListener onAdapterItemListener) {
            this.position = position;
            this.onAdapterItemListener = onAdapterItemListener;
        }

        @Override
        public void onClick(View v) {
            if (onAdapterItemListener != null)
                onAdapterItemListener.onAdapterItemClick(position);
        }
    }
}
