package net.shared.distributed.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.function.BiConsumer;

/**
 * Created by Guy on 16/05/2017.
 */
public class AsyncNetworkSocket extends NetworkSocket {

    protected Thread socketThread;
    protected String name;
    protected BiConsumer<AsyncNetworkSocket, String> process;

    public AsyncNetworkSocket(String host, int port) throws IOException {
        this(host, port, "SocketThread");
    }

    public AsyncNetworkSocket(String host, int port, String name) throws IOException {
        super(host, port);
        this.name = name;
    }

    /**
     * Sets the function to be invoked once a response has been received
     * @param process
     * @return The self instance, for method chaining
     */
    public AsyncNetworkSocket SetCallback(BiConsumer<AsyncNetworkSocket, String> process) {
        this.process = process;
        return this;
    }

    /**
     * Sends the payload to the connected host
     * @param payload The payload to send
     * TODO redesign to accommodate generic binary objects
     */
    public void Send(Object payload) {
        socketThread = new Thread(() -> {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.print(payload);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                StringBuilder lines = new StringBuilder();
                while((line = in.readLine()) != null)
                    lines.append(line);
                if(process != null)
                    process.accept(AsyncNetworkSocket.this, lines.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, name);
        socketThread.start();
    }

}
