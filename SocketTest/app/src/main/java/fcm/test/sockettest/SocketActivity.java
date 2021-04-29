package fcm.test.sockettest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import fcm.test.sockettest.adapter.ChatRoomAdapter;
import fcm.test.sockettest.model.data.Message;
import fcm.test.sockettest.model.data.SendMessageRequest;
import fcm.test.sockettest.model.data.ViewType;
import fcm.test.sockettest.socke.GetSocket;

public class SocketActivity extends AppCompatActivity {

    private Socket socket;

    Button send, exit;
    EditText edit_message;
    RecyclerView chat_room;

    SharedPreferences username;

    String name;
    String chatId;

    ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        send = findViewById(R.id.send_btn);
        exit = findViewById(R.id.exit_btn);
        edit_message = findViewById(R.id.edit_message);
        chat_room = findViewById(R.id.chat_room);

        chat_room.setAdapter(chatRoomAdapter);

        username = getSharedPreferences("user_name", MODE_PRIVATE);
        name = username.getString("name", "null");
        if(name.equals("null")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");

        startSocket();

        socket.connect();

        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.emit("joinRoom", chatId);
        socket.on("info", onInfo);
        socket.on("message", onMessage);

        send.setOnClickListener(v -> {
            String message = edit_message.getText().toString();
            if(message.length() < 1) {
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("chatId", chatId);
                jsonObject.put("message", message);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            socket.emit("sendMessage", jsonObject);
            socket.connect();
            edit_message.setText("");
        });

        exit.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, GetChatIdActivity.class);

            socket.emit("leaveRoom", chatId);

            socket.off(Socket.EVENT_CONNECT, onConnect);
            socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.off("info", onInfo);
            socket.off("message", onMessage);
            socket.disconnect();
            socket.close();

            startActivity(intent1);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent1 = new Intent(this, GetChatIdActivity.class);

        socket.emit("leaveRoom", chatId);

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off("info", onInfo);
        socket.off("message", onMessage);
        socket.disconnect();
        socket = socket.close();

        startActivity(intent1);
        finish();
    }

    public void startSocket() {
        GetSocket getSocket = new GetSocket("name=" + name);

        this.socket = getSocket.getSocket();
    }

    public void setMessage(Message messageInfo) {
        chatRoomAdapter.addItem(messageInfo);
    }

    private Emitter.Listener onConnect = args -> {
        this.runOnUiThread(() -> {
            Log.d("TAG", "connect");
        });
    };

    private Emitter.Listener onDisconnect = args -> {
        this.runOnUiThread(() -> {
            Log.d("TAG", "connect");
        });
    };

    private Emitter.Listener onConnectError = args -> {
        Log.d("TAG", "failed connected");
    };

    private Emitter.Listener onInfo = args -> {
        this.runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            String userName;
            String message;
            String messageAt;
            try {
                userName = data.getString("userName");
                message = data.getString("message");
                messageAt = "asdf";
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            setMessage(new Message(userName, message, messageAt, ViewType.INFO));

            chat_room.smoothScrollToPosition(chatRoomAdapter.getItemCount()-1);
        });
    };

    private Emitter.Listener onMessage = args -> {
        this.runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            String userName;
            String message;
            String messageAt;
            int viewType;
            try {
                userName = data.getString("userName");
                message = data.getString("message");
                messageAt = "asdf";

                Log.d("TAG", userName);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            if(userName.equals(this.name)) {
                viewType = ViewType.MY_MESSAGE;
            }else {
                viewType = ViewType.USER_MESSAGE;
            }

            setMessage(new Message(userName, message, messageAt, viewType));
            chat_room.smoothScrollToPosition(chatRoomAdapter.getItemCount()-1);
        });
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off("info", onInfo);
        socket.off("message", onMessage);
        socket.disconnect();

        socket = socket.close();
    }
}