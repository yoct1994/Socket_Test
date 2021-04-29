package com.example.demo.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.example.demo.dto.SendMessageRequest;

public interface SocketService {
    void connect(SocketIOClient client);
    void disconnect(SocketIOClient client);
    void joinRoom(SocketIOClient client, String chatId);
    void leaveRoom(SocketIOClient client, String chatId);
    void sendMessage(SocketIOClient client, SendMessageRequest messageRequest);
}
