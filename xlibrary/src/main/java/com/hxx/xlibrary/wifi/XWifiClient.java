package com.hxx.xlibrary.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.hxx.xlibrary.XLibrary;
import com.hxx.xlibrary.exception.XInitializeException;
import com.hxx.xlibrary.util.L;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Android on 2018/1/31.
 */

public class XWifiClient {

    private static XWifiClient instance = new XWifiClient();

    private WifiManager wifiManager;

    private XWifiClientRequestPermissionListener wifiClientRequestWifiPermissionListener;
    private OnXWifiClientActionListener onXWifiClientActionListener;

    public static XWifiClient getInstance() {
        if (instance == null)
            instance = new XWifiClient();
        return instance;
    }

    private XWifiClient() {
        if (XLibrary.context == null) throw new XInitializeException("XLibrary Not Initialize!");
        wifiManager = (WifiManager) XLibrary.context.getSystemService(Context.WIFI_SERVICE);
    }

    public void setOnXWifiClientActionListener(OnXWifiClientActionListener onXWifiClientActionListener) {
        this.onXWifiClientActionListener = onXWifiClientActionListener;
    }

    /**
     * 开启wifi
     */
    public void openWifi(Activity activity) {
        if (wifiManager != null && !wifiManager.isWifiEnabled())
            checkWifiPermission(activity, new XWifiClientRequestPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    wifiManager.setWifiEnabled(true);
                }

                @Override
                public void onPermissionDenied() {
                    L.e("XWifiClient : Wifi Permission Denied!");
                    if (onXWifiClientActionListener != null)
                        onXWifiClientActionListener.onPermissionDenied(Manifest.permission.ACCESS_WIFI_STATE);
                }
            });
    }

    /**
     * 关闭wifi
     */
    public void closeWifi() {
        if (wifiManager != null)
            if (wifiManager.isWifiEnabled())
                wifiManager.setWifiEnabled(false);
    }

    /**
     * 开始扫描wifi
     *
     * @param activity
     */
    public void startScan(Activity activity) {
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            checkLocationPermission(activity, new XWifiClientRequestPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    wifiManager.startScan();
                }

                @Override
                public void onPermissionDenied() {
                    L.e("XWifiClient : Location Permission Denied!");
                    if (onXWifiClientActionListener != null)
                        onXWifiClientActionListener.onPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
            });
        }
    }

    /**
     * wifi 是否可用
     *
     * @return
     */
    public boolean isWifiEnable() {
        if (wifiManager != null)
            return wifiManager.isWifiEnabled();
        return false;
    }

    /**
     * 获取Wifi扫描结果
     *
     * @return
     */
    public List<ScanResult> getScanResult() {
        if (wifiManager != null) {
            return wifiManager.getScanResults();
        }
        return null;
    }


    public String getConnectedSSID() {
        if (wifiManager != null)
            return wifiManager.getConnectionInfo().getSSID();
        return "";
    }

    public String getConnectIP() {
        if (wifiManager != null) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            return intToInetAddress(dhcpInfo.serverAddress).getHostAddress();
        }
        return "";
    }

    public InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    public void connectWifi(String wifiSSID, String wifiPwd) {
        if (wifiManager != null) {
            int result = wifiManager.addNetwork(addNewWifiConfiguration(wifiSSID, wifiPwd));
            L.e("添加结果：" + result);
            if (result != -1) {
                boolean isConnected = wifiManager.enableNetwork(result, true);  // 连接配置好的指定ID的网络 true连接成功
                if (isConnected) {
                    L.e("连接成功：" + wifiSSID);
                } else {
                    L.e("连接失败：" + wifiSSID);
                }
            }
        }
    }

    private WifiConfiguration addNewWifiConfiguration(String wifiSSID, String wifiPwd) {
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.allowedAuthAlgorithms.clear();
        wifiConfiguration.allowedGroupCiphers.clear();
        wifiConfiguration.allowedKeyManagement.clear();
        wifiConfiguration.allowedPairwiseCiphers.clear();
        wifiConfiguration.allowedProtocols.clear();

        //如果之前有类似的配置
        WifiConfiguration tempConfig = isExist(wifiSSID);
        if (tempConfig != null) {
            //则清除旧有配置
            wifiManager.removeNetwork(tempConfig.networkId);
        }

        wifiConfiguration.SSID = "\"" + wifiSSID + "\"";
        wifiConfiguration.preSharedKey = "\"" + wifiPwd + "\"";

        wifiConfiguration.hiddenSSID = false;
        wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;

        return wifiConfiguration;
    }

    private WifiConfiguration isExist(String wifiSSID) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + wifiSSID + "\"")) {
                return config;
            }
        }
        return null;
    }

    //请求Wifi权限
    private void checkWifiPermission(Activity activity, XWifiClientRequestPermissionListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onPermissionGranted();
            return;
        }
        if (ContextCompat.checkSelfPermission(XLibrary.context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            wifiClientRequestWifiPermissionListener = listener;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, XWifiConstant.X_WIFI_REQUEST_WIFI_PERMISSION_CODE);
        } else {
            listener.onPermissionGranted();
        }
    }

    //扫描Wifi需要位置权限
    private void checkLocationPermission(Activity activity, XWifiClientRequestPermissionListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onPermissionGranted();
            return;
        }
        if (ContextCompat.checkSelfPermission(XLibrary.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            wifiClientRequestWifiPermissionListener = listener;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, XWifiConstant.X_WIFI_REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            listener.onPermissionGranted();
        }
    }

    //权限结果处理
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == XWifiConstant.X_WIFI_REQUEST_WIFI_PERMISSION_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_WIFI_STATE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                wifiClientRequestWifiPermissionListener.onPermissionGranted();
            } else {
                wifiClientRequestWifiPermissionListener.onPermissionDenied();
            }
        }
        if (requestCode == XWifiConstant.X_WIFI_REQUEST_LOCATION_PERMISSION_CODE) {
            L.e("权限结果回调");
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                wifiClientRequestWifiPermissionListener.onPermissionGranted();
                L.e("权限允许");
            } else {
                wifiClientRequestWifiPermissionListener.onPermissionDenied();
            }
        }
    }

    /**
     * 注册监听WiFi操作的系统广播
     */
    public void registerWifiReceiver(BroadcastReceiver broadcastReceiver) {
        IntentFilter filter = new IntentFilter();
        //监听WiFi开启与关闭
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //监听扫描周围可用WiFi列表结果
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        //监听WiFi连接与断开
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //注册广播
        XLibrary.context.registerReceiver(broadcastReceiver, filter);
    }

    /**
     * 反注册WiFi相关的系统广播
     */
    public void unregisterWifiReceiver(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null) {
            XLibrary.context.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }


    private interface XWifiClientRequestPermissionListener {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    public interface OnXWifiClientActionListener {
        void onPermissionDenied(String permission);
    }

}
