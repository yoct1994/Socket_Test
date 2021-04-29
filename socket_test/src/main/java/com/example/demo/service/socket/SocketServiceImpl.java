package com.example.demo.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.demo.dto.MessageResponse;
import com.example.demo.dto.SendMessageRequest;
import com.example.demo.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService{

    private final SocketIOServer server;

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        String str = "오전 ";
        int h, m;

        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);

        if(h > 12) {
            str = "오후 ";
            h -= 12;
        }

        return str + h + ":" + m;
    }

    @Override
    public void connect(SocketIOClient client) {
        String name = client.getHandshakeData().getSingleUrlParam("name");
        if(name == null) {
            System.out.println("bad request (name)");
            client.disconnect();
            return;
        }

        System.out.println("connected name : " + name);

        User user = User.builder()
                .userName(name)
                .build();

        client.set("user", user);
    }

    @Override
    public void disconnect(SocketIOClient client) {
        System.out.println("DisconnectedId : " + client.getSessionId());
    }

    @Override
    public void joinRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");
        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        client.joinRoom(chatId);
        System.out.println("join : " + user.getUserName());

        sendInfo(MessageResponse.builder()
                .userName(user.getUserName())
                .message(user.getUserName() + "가 접속했습니다.")
                .messageAt(getTime())
                .build(), chatId);
    }

    @Override
    public void leaveRoom(SocketIOClient client, String chatId) {
        User user = client.get("user");
        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        sendInfo(MessageResponse.builder()
                .userName(user.getUserName())
                .message(user.getUserName() + "가 나갔습니다.")
                .messageAt(getTime())
                .build(), chatId);

        client.leaveRoom(chatId);
        client.del("user");
        System.out.println("user leave : " + user.getUserName());
    }

    @SneakyThrows
    @Override
    public void sendMessage(SocketIOClient client, SendMessageRequest sendMessageRequest) {
        User user = client.get("user");
        if(user == null) {
            System.out.println("user not found");
            client.disconnect();
            return;
        }

        System.out.println(sendMessageRequest.getMessage());

        System.out.println(LocalDateTime.now().toString());

        send(MessageResponse.builder()
                .userName(user.getUserName())
                .message(sendMessageRequest.getMessage())
                .messageAt(getTime())
                .build(),
                sendMessageRequest.getChatId());
    }

    private void sendInfo(MessageResponse messageResponse, String chatId) {
        server.getRoomOperations(chatId).sendEvent("info",
                MessageResponse.builder()
                        .userName(messageResponse.getUserName())
                        .message(messageResponse.getMessage())
                        .messageAt(messageResponse.getMessageAt())
                        .build()
                );
    }

    private void send(MessageResponse messageResponse, String chatId) {
        server.getRoomOperations(chatId).sendEvent("message",
                MessageResponse.builder()
                        .userName(messageResponse.getUserName())
                        .message(messageResponse.getMessage())
                        .message(messageResponse.getMessage())
                        .build()
                );
    }
}
