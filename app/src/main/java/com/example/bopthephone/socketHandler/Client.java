package com.example.bopthephone.socketHandler;

import com.example.bopthephone.SocketService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

public class Client {

//    private final Executor executor;

    private SocketChannel channel;
    private ByteBuffer readBuffer;
    private ByteBuffer writeBuffer;

    private String target;
    private int port;

    private Executor executor;

    public Client(String target, int port, Executor executor) {
        this.port = port;
        this.target = target;
        readBuffer = ByteBuffer.allocate(1024);
        writeBuffer = ByteBuffer.allocate(1024);

        this.executor = executor;
    }

    public void sendMessage(String msg) {
        executor.execute(() -> {
            try {
                doSendMessage("Test444");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String doSendMessage(String msg) throws IOException {
        writeBuffer = ByteBuffer.wrap(msg.getBytes());
        System.out.println("test125");
        channel.write(writeBuffer);
        System.out.println("Sent: " + msg);
        writeBuffer.clear();
        channel.read(readBuffer);
        String response = new String(readBuffer.array()).trim();
        System.out.println(response);
        readBuffer.clear();
        return response;
    }

    public void connect() throws IOException {
        executor.execute(() -> {
            try {
                channel = SocketChannel.open(new InetSocketAddress(target, port));
                System.out.println("socket opened");
                doSendMessage("connect " + "Name Here");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() throws IOException {
        if (channel == null) return;
        channel.close();
    }

}
