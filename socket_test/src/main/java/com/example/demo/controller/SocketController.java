package com.example.demo.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.demo.dto.SendMessageRequest;
import com.example.demo.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer server;

    private final SocketService service;

    @PostConstruct
    public void socket() {
        server.addConnectListener(service::connect);
        server.addDisconnectListener(service::disconnect);
        server.addEventListener("joinRoom", String.class,
                ((client, data, ackSender) -> { service.joinRoom(client, data); }));
        server.addEventListener("leaveRoom", String.class,
                ((client, data, ackSender) -> { service.leaveRoom(client, data); }));
        server.addEventListener("sendMessage", SendMessageRequest.class,
                ((client, data, ackSender) -> { service.sendMessage(client, data); }));
    }
}
