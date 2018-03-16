package com.hxx.xlibrary.socket;


import com.hxx.xlibrary.util.L;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Android on 2018/2/5.
 */

public class XSocketReceive {

    private ServerSocket serverSocket;

    public XSocketReceive() {
    }

    public void createServerSocketAndConnect(int port) {
        try {
            serverSocket = new ServerSocket(port);
            L.e("等待。。。。");
            Socket socket = serverSocket.accept();
            L.e("开始---");
            InputStream inputStream = socket.getInputStream();

            byte buffer[] = new byte[1024 * 4];

            int temp = 0;

            // 从InputStream当中读取客户端所发送的数据
            while ((temp = inputStream.read(buffer)) != -1) {
                System.out.println();
                L.e("接收到的结果："+new String(buffer, 0, temp));
            }
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
