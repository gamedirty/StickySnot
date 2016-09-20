package com.example.sovnem.stickysnot;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zjh
 * @description
 * @date 16/9/20.
 */
public class Server {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(1109);
            Socket rs = serverSocket.accept();
            System.out.println("server收到链接");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
