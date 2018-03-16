package com.hxx.xlibrary.memory;

import android.text.TextUtils;


import com.hxx.xlibrary.exception.XMemoryDataNullException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Android on 2018/2/7.
 */

public class XMemoryData {

    private volatile static XMemoryData instance;

    //内存数据保存容器
    private Map<String, Object> memoryData;

    public static XMemoryData getInstance() {
        if (instance == null) {
            synchronized (XMemoryData.class) {
                if (instance == null) {
                    instance = new XMemoryData();
                }
            }
        }
        return instance;
    }

    private XMemoryData() {
        memoryData = new HashMap<>();
    }

    /**
     * 保存值
     *
     * @param key
     * @param data
     * @param <T>
     */
    public <T> void setData(String key, T data) {
        checkNull();
        if (!checkKeyExist(key)) {
            memoryData.put(key, data);
        }
    }

    /**
     * 获取值
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getData(String key) {
        checkNull();
        try {
            if (checkKeyExist(key)) {
                return (T) memoryData.get(key);
            }
        } catch (ClassCastException e) {
            throw e;
        }
        return null;
    }

    /**
     * 编辑对应的值
     *
     * @param key
     * @param action
     * @param <T>
     */
    public <T> void editData(String key, OnMemoryDataEditAction<T> action) {
        checkNull();
        if (TextUtils.isEmpty(key) || action == null) return;
        T t = action.onMemoryDataEdit(((T) getData(key)));
        memoryData.put(key, t);
    }

    /**
     * 清除指定key对应的值
     *
     * @param key
     */
    public void clear(String key) {
        checkNull();
        memoryData.remove(key);
    }

    /**
     * 全部清除
     */
    public void clearAll() {
        checkNull();
        memoryData.clear();
    }

    /**
     * 检查Key值
     *
     * @param key
     * @return
     */
    private boolean checkKeyExist(String key) {
        if (TextUtils.isEmpty(key)) throw new XMemoryDataNullException();
        return memoryData.containsKey(key);
    }


    /**
     * 检查异常
     */
    private void checkNull() {
        if (memoryData == null)
            throw new XMemoryDataNullException();
    }

    public interface OnMemoryDataEditAction<T> {
        T onMemoryDataEdit(T data);
    }


}
