package com.kpro.common.dto.base;

import java.util.List;

public class BaseApiResponse<T> {
    private List<ApiMessage> messages;
    private T data;

    public BaseApiResponse() {
    }

    public BaseApiResponse(List<ApiMessage> messages, T data) {
        this.messages = messages;
        this.data = data;
    }

    public List<ApiMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ApiMessage> messages) {
        this.messages = messages;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseApiResponse{" +
                "messages=" + messages +
                ", data=" + data +
                '}';
    }
}
