package net.shared.distributed.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Guy on 16/05/2017.
 */
public class NetworkSocket {

    /** The underlying connection socket */
    protected Socket socket;

    /** The input stream attached to the socket */
    protected InputStream inputStream;

    /** The output stream attached to the socket */
    protected OutputStream outputStream;

    public NetworkSocket(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
