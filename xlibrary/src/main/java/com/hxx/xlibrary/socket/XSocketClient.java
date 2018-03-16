package com.hxx.xlibrary.socket;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by Android on 2018/2/5.
 */

public class XSocketClient {

    private Socket socket;
    //发送数据的流
    public XSocketClient() {

    }


    public void createSocketAndConnect(String ip, int port) {
        try {
            socket = new Socket(ip, port);

            //文件发送
            //需要发送的数据
            InputStream inputStream = new FileInputStream("");

            //接收的数据
            OutputStream outputStream = socket.getOutputStream();

            byte buffer[] = new byte[4 * 1024];

            int temp = 0;

            while ((temp = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, temp);

            }

            outputStream.flush();

            //报文发送
            String data = "测试数据xxwifidatatest";


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createSocketClient(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void sendData(String message) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
