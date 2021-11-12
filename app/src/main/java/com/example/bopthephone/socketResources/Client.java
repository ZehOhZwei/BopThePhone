package com.example.bopthephone.socketResources;

import android.os.Handler;

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

    private final Executor executor;
    private final Handler handler;

    private final Gson gson = new Gson();

    public Client(String target, int port, Executor executor, Handler handler) {
        this.port = port;
        this.target = target;

        readBuffer = ByteBuffer.allocate(1024);
        writeBuffer = ByteBuffer.allocate(1024);

        this.executor = executor;
        this.handler = handler;
    }

    public void sendMessage(Message message, SocketCallback<Message> callback) {
        executor.execute(() -> {
            CallbackResponse<Message> response = null;
            try {
                String stringMessage = gson.toJson(message);

                writeBuffer = ByteBuffer.wrap(stringMessage.getBytes());
                channel.write(writeBuffer);

                writeBuffer.flip();
                writeBuffer.compact();

                channel.read(readBuffer);
                String answer = new String(readBuffer.array()).trim();
                System.out.println(answer);
                response = new CallbackResponse<>(gson.fromJson(answer, Message.class));

                readBuffer.clear();
            } catch (IOException | NullPointerException e) {
                System.out.println("Error sending or receiving message");
            } finally {
                notifyMessage(response, callback);
            }
        });
    }

    public void connect(SocketCallback<Boolean> callback) {
        executor.execute(() -> {
            CallbackResponse<Boolean> response = null;
            try {
                channel = SocketChannel.open(new InetSocketAddress(target, port));
                response = new CallbackResponse<>(true);
            } catch (IOException e) {
                response = new CallbackResponse<>(false);
                e.printStackTrace();
            } finally {
                callback.onComplete(response);
            }
        });
    }

    public void close(SocketCallback<Boolean> callback) {
        if (channel == null) return;
        executor.execute(() -> {
            CallbackResponse<Boolean> response = null;
            try {
                channel.close();
                response = new CallbackResponse<>(true);
            } catch (IOException e) {
                response = new CallbackResponse<>(false);
                e.printStackTrace();
            } finally {
                callback.onComplete(response);
            }
        });
    }

    private void notifyMessage(CallbackResponse<Message> response, SocketCallback<Message> callback) {
        handler.post(() -> callback.onComplete(response));
    }

    public void receive(SocketCallback<Message> callback) {
        executor.execute(() -> {
            CallbackResponse<Message> response = null;
            try {
                while (true) {
                    int length;
                    length = channel.read(readBuffer);
                    if (length > 0) {
                        String answer = new String(readBuffer.array()).trim();
                        response = new CallbackResponse<>(gson.fromJson(answer, Message.class));
                        readBuffer.clear();
                        break;
                    }
                }
            } catch (IOException e) {

            } finally {
                notifyMessage(response, callback);
            }
        });
    }
}
