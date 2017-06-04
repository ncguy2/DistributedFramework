package net.shared.distributed.network;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Guy on 16/05/2017.
 */
public class NetworkSocket {

    /** The underlying connection socket */
    protected Socket socket;


    public NetworkSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public Socket getSocket() {
        return socket;
    }

}
