package fcm.test.sockettest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;

import java.io.Serializable;

import fcm.test.sockettest.socke.GetSocket;

public class MainActivity extends AppCompatActivity {

    Button start_btn;
    EditText edit_name;

    SharedPreferences username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_btn = (Button) findViewById(R.id.go_btn);
        edit_name = (EditText) findViewById(R.id.edit_name);

        username = getSharedPreferences("user_name", MODE_PRIVATE);
        String username1 = username.getString("name", "null");

        if(!username1.equals("null")) {
            Intent intent = new Intent(this, GetChatIdActivity.class);
            startActivity(intent);
            finish();
        }

        start_btn.setOnClickListener(v -> {
            String name = edit_name.getText().toString();
            if(name.length() < 1) {
                Toast.makeText(this, "input your name", Toast.LENGTH_SHORT).show();
                return;
            }

            username = getSharedPreferences("user_name", MODE_PRIVATE);
            SharedPreferences.Editor editor = username.edit();
            editor.putString("name", name);
            editor.apply();

            clickBtn();
        });
    }

    private void clickBtn() {
        Intent intent = new Intent(this, GetChatIdActivity.class);
        startActivity(intent);
        finish();
    }
}