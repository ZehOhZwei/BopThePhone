package com.example.bopthephone.socketResources;

public class CallbackResponse<T> {
    public T data;

    public CallbackResponse(T data) {
        this.data = data;
    }
}
