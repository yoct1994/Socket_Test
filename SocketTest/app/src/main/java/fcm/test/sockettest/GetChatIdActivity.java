package fcm.test.sockettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetChatIdActivity extends AppCompatActivity {


    Button chatId_btn, refresh_btn;
    EditText edit_chatId;

    SharedPreferences username;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_chat_id);

        refresh_btn = (Button) findViewById(R.id.refresh_name_btn);
        chatId_btn = (Button) findViewById(R.id.chat_id_btn);
        edit_chatId = (EditText) findViewById(R.id.chat_id_name);

        username = getSharedPreferences("user_name", MODE_PRIVATE);
        user_name = username.getString("name", "null");

        if(user_name.equals("null")) {
            Intent intent = new Intent(this, GetChatIdActivity.class);
            startActivity(intent);
            finish();
        }

        chatId_btn.setOnClickListener(v -> {
            String chatId = edit_chatId.getText().toString();
            if(chatId.isEmpty()) {
                return;
            }

            Intent intent = new Intent(this, SocketActivity.class);
            intent.putExtra("chatId", chatId);
            startActivity(intent);
            finish();
        });

        refresh_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            username = getSharedPreferences("user_name", MODE_PRIVATE);
            username.edit().putString("name", "null").apply();
            startActivity(intent);
            finish();
        });
    }
}