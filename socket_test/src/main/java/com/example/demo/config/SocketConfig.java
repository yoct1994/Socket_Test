package com.example.demo.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@RequiredArgsConstructor
public class SocketConfig {

    private SocketIOServer server;

    @Value("${server.socket.port}")
    private int port;

    @Bean
    public SocketIOServer SocketIOServer() {
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();

        configuration.setPort(port);

        SocketIOServer server = new SocketIOServer(configuration);

        server.start();

        this.server = server;

        return server;
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }
}
