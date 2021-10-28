package com.example.bopthephone.socketHandler;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    Socket socket;
    String target;
    int port;
    long id;

    PrintWriter out;
    BufferedReader in;

    Gson gson = new Gson();

    public Client(String url, int port){
        this.target = url;
        this.port = port;
        id = this.hashCode();
    }

    public void createSocket(){
        try {
            socket = new Socket(target, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(gson.toJson(new Message(this.hashCode(), "connect", "")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String type, String content) {
        Message msg = new Message(id, type, content);
        out.println(gson.toJson(msg));
    }
}
