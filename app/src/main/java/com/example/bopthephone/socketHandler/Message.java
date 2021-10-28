package com.example.bopthephone.socketHandler;

public class Message {
    long sender;
    String type;
    String content;

    public Message(long sender, String type, String content) {
        this.sender = sender;
        this.type = type;
        this.content = content;
    }
}
