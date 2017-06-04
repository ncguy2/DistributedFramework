package net.shared.distributed.network;

import net.shared.distributed.logging.Logger;

import java.io.*;
import java.util.function.BiConsumer;

/**
 * Created by Guy on 16/05/2017.
 */
public class AsyncNetworkSocket extends NetworkSocket {

    protected Thread socketThread;
    protected String name;
    protected BiConsumer<AsyncNetworkSocket, Object> process;

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
    public AsyncNetworkSocket SetCallback(BiConsumer<AsyncNetworkSocket, Object> process) {
        this.process = process;
        return this;
    }

    /**
     * Sends the payload to the connected host
     * @param payload The payload to send
     * TODO redesign to accommodate generic binary objects
     */
    public void Send(Object payload) {

        if(!(payload instanceof Serializable)) {
            Logger.instance().Error(payload.getClass().getCanonicalName() + " is not suitable for serialization");
            return;
        }

        socketThread = new Thread(() -> {
            try {
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeObject(payload);

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while(in.available() > 0) {
                    Object obj = in.readObject();
                    if(process != null)
                        process.accept(AsyncNetworkSocket.this, obj);
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }, name);
        socketThread.start();
    }

}
