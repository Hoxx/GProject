package com.hxx.xlibrary.wifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;


import com.hxx.xlibrary.XLibrary;
import com.hxx.xlibrary.exception.XInitializeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Android on 2018/1/31.
 */

public class XAPClient {

    private int ANDROID_VERSION_M_SYSTEM_SETTING_WRITE_PERMISSION_FOR_AP = 1;
    private OnApActionListener onApActionListener;

    private static XAPClient instance = new XAPClient();

    private WifiManager wifiManager;

    public static XAPClient getInstance() {
        if (instance == null)
            instance = new XAPClient();
        return instance;
    }

    private XAPClient() {
        if (XLibrary.context == null) throw new XInitializeException("XLibrary Not Initialize!");
        wifiManager = (WifiManager) XLibrary.context.getSystemService(Context.WIFI_SERVICE);
    }

    //开启热点
    public void openWifiAP(Activity activity, OnApActionListener actionListener) {
        this.onApActionListener = actionListener;
        checkCanOpenAp(activity);
    }

    /**
     * 开启热点
     */
    private void openWifiAP() {
        if (wifiManager.isWifiEnabled()) {
            //如果wifi处于打开状态，则关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        WifiConfiguration configuration = new WifiConfiguration();

        configuration.SSID = XWifiConstant.WIFI_AP_SSID;
        configuration.preSharedKey = XWifiConstant.WIFI_AP_PASSWORD;
        configuration.hiddenSSID = true;
        configuration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        configuration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        configuration.status = WifiConfiguration.Status.ENABLED;

        //通过反射调用设置热点
        try {
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (Boolean) method.invoke(wifiManager, configuration, true);
            if (onApActionListener != null) {
                if (enable) {
                    onApActionListener.onApOpenSuccess();
                } else {
                    onApActionListener.onApError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onApActionListener != null)
                onApActionListener.onApError();
        }
    }

    /**
     * 检查是否开启Wifi热点
     *
     * @return
     */

    private boolean isWifiApEnabled() {
        try {
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public void closeWifiAp() {
        //判断是否有WRITE_SETTINGS权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(XLibrary.context)) {
                return;
            }
        }
        if (isWifiApEnabled()) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                boolean enable = (Boolean) method2.invoke(wifiManager, config, false);
                if (onApActionListener != null) {
                    if (enable) {
                        onApActionListener.onApCloseSuccess();
                    } else {
                        onApActionListener.onApError();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (onApActionListener != null)
                    onApActionListener.onApError();
            }
        }
    }

    //Android 6.0以上需要开启修改系统权限
    private void checkCanOpenAp(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 判断是否有WRITE_SETTINGS权限
            if (!Settings.System.canWrite(XLibrary.context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + XLibrary.context.getPackageName()));
                activity.startActivityForResult(intent, ANDROID_VERSION_M_SYSTEM_SETTING_WRITE_PERMISSION_FOR_AP);
            } else {
                openWifiAP();
            }
        } else {
            openWifiAP();
        }
    }

    //Android 6.0以上开启修改系统权限回调检查
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ANDROID_VERSION_M_SYSTEM_SETTING_WRITE_PERMISSION_FOR_AP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 判断是否有WRITE_SETTINGS权限
                if (Settings.System.canWrite(XLibrary.context)) {
                    openWifiAP();
                } else {
                    if (onApActionListener != null)
                        onApActionListener.onApError();
                }
            } else {
                openWifiAP();
            }
        }
    }

    public interface OnApActionListener {
        void onApOpenSuccess();

        void onApCloseSuccess();

        void onApError();
    }

}
