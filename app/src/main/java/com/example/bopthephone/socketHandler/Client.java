package com.example.bopthephone.socketHandler;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

public class Client {

    private SocketChannel channel;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    private String target;
    private int port;

    private Executor executor;
    private final Gson gson = new Gson();

    public Client(String target, int port, Executor executor) {
        this.port = port;
        this.target = target;

        readBuffer = ByteBuffer.allocate(1024);
        writeBuffer = ByteBuffer.allocate(1024);

        this.executor = executor;
    }

    public void sendMessage(Message message) {
        executor.execute(() -> {
            try {
                String stringMessage = gson.toJson(message);

                writeBuffer = ByteBuffer.wrap(stringMessage.getBytes());
                channel.write(writeBuffer);

                writeBuffer.flip();
                writeBuffer.compact();

                channel.read(readBuffer);
                String response = new String(readBuffer.array()).trim();

                readBuffer.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void connect() {
        executor.execute(() -> {
            try {
                channel = SocketChannel.open(new InetSocketAddress(target, port));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if (channel == null) return;
        executor.execute(() -> {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
