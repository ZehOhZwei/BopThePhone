package com.example.bopthephone.socketHandler;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class Client implements Runnable {
    private long id;
    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static Gson gson;
    private String target;
    private int port;

    public Client(String target, int port) throws IOException {
        this.target = target;
        this.port = port;
        Random random = new Random();
        id = random.nextLong();
    }

    public String sendMessage(String msg) throws IOException {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response;
        client.write(buffer);
        buffer.clear();
        client.read(buffer);
        System.out.println("test125");
        response = new String(buffer.array()).trim();
        buffer.clear();
        return response;
    }

    public void close() throws IOException {
        sendMessage("disconnect " + id);
        if (client == null) return;
        client.close();
    }

    @Override
    public void run() {
        try {
            client = SocketChannel.open(new InetSocketAddress(target, port));
            buffer = ByteBuffer.allocate(1024);
            while (true) {
                client.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
