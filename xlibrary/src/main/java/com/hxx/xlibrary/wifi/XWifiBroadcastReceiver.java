package com.hxx.xlibrary.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.TextUtils;


import com.hxx.xlibrary.util.L;

import java.util.List;

/**
 * Created by Android on 2018/2/2.
 */

public abstract class XWifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        //wifi状态改变
        if (TextUtils.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            //监听WiFi开启/关闭事件
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                //TODO WiFi已开启
                onWifiOpen();

            } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                //TODO WiFi已关闭
                onWifiClose();
            }
        }
        //wifi扫描到可用的Wifi
        if (TextUtils.equals(action, WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            L.e("wifi扫描到可用的Wifi");
            //TODO 获取扫描到的wifi结果
            onWifiScanResult(XWifiClient.getInstance().getScanResult());
        }
        //监听网络状态
        if (TextUtils.equals(action, WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    //TODO WiFi已连接
                    String connectedSSID = XWifiClient.getInstance().getConnectedSSID();
                    onWifiConnected(connectedSSID);
                }
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    //TODO WiFi已断开连接
                    onWifiDisconnected();
                }
            }
        }
    }

    public abstract void onWifiOpen();

    public abstract void onWifiClose();

    public abstract void onWifiScanResult(List<ScanResult> list);

    public abstract void onWifiConnected(String connectedSSID);

    public abstract void onWifiDisconnected();

}
