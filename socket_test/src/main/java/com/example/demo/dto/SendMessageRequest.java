package com.example.demo.dto;

import lombok.*;

@Getter
public class SendMessageRequest {
    private String chatId;
    private String message;

    public SendMessageRequest() {
    }
}
