package net.shared.distributed.network;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Guy on 16/05/2017.
 */
public class NetworkHandler {

    private static NetworkHandler instance;
    public static NetworkHandler GetInstance() {
        if (instance == null)
            instance = new NetworkHandler();
        return instance;
    }

    private NetworkHandler() {}

    public Socket CreateSocket(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);

        return socket;
    }

}
