package com.example.sovnem.stickysnot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author zjh
 * @description
 * @date 16/9/20.
 */
public class Client {
    public static void main(String[] args) {

        try {
            Socket socket = new Socket("14.215.177.37", 80);
            System.out.println("有链接:" + socket.isConnected());
            OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
            StringBuffer s = new StringBuffer();
            s.append("GET http://www.baidu.com/ HTTP/1.1");
            s.append("Accept: html/text");
            s.append("Host: 14.215.177.37");
            s.append("Connection: Close");

            writer.write(s.toString());
            writer.flush();
            System.out.println("请求发送成功");
            InputStream inputStream = socket.getInputStream();

            byte[] buff = new byte[8 * 1024];
            int len;
            while ((len = inputStream.read(buff)) > 0) {
                String m = new String(buff);
                System.out.println("解析:" + m);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
