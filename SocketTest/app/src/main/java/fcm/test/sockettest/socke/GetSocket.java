package fcm.test.sockettest.socke;

import android.util.Log;

import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.Arrays;

public class GetSocket {

    Socket socket;

    public GetSocket(String query) {
        try {
            IO.Options options = new IO.Options();
            options.forceNew = true;
            options.reconnection = true;
            options.transports = new String[] {WebSocket.NAME};
            options.query = query;

            this.socket = IO.socket("http://192.168.137.213:7055/", options);
            Log.d("Socket query", query);
            Log.d("Socket Connect Log", "connect succeed");
        } catch (
                URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return this.socket;
    }
}
