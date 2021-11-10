package com.example.bopthephone.socketHandler;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class Client implements Runnable{
    private static SocketChannel client;
    private static ByteBuffer readBuffer;
    private static ByteBuffer writeBuffer;

    private static String target;
    private static int port;

    public Client(String target, int port) throws IOException {
        this.port = port;
        this.target = target;
    }

    public String sendMessage(String msg) throws IOException {
        writeBuffer = ByteBuffer.wrap(msg.getBytes());
        System.out.println("test125");
        client.write(writeBuffer);
        System.out.println("Sent: " + msg);
        writeBuffer.clear();
        client.read(readBuffer);
        String response = new String(readBuffer.array()).trim();
        readBuffer.clear();
        return response;
    }

    public void close() throws IOException {
        if (client == null) return;
        client.close();
    }

    @Override
    public void run() {
        try {
            client = SocketChannel.open(new InetSocketAddress(target, port));
            readBuffer = ByteBuffer.allocate(1024);
            writeBuffer = ByteBuffer.allocate(1024);
            while (true) {
                client.read(readBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
