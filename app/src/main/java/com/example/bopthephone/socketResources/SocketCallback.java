package com.example.bopthephone.socketResources;

public interface SocketCallback<T> {
    void onComplete(CallbackResponse<T> response);
}
