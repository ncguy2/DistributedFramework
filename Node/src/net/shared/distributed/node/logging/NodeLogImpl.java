package net.shared.distributed.node.logging;

import net.shared.distributed.Registry;
import net.shared.distributed.logging.ILogImpl;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.AsyncNetworkSocket;

import java.io.IOException;

public class NodeLogImpl implements ILogImpl {

    public String host;
    public int port;
    public AsyncNetworkSocket socket;

    public NodeLogImpl(String host) {
        this(host, Registry.REMOTE_LOG_PORT);
    }

    public NodeLogImpl(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            this.socket = new AsyncNetworkSocket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
            this.socket = null;
        }
    }

    @Override
    public void Log(Logger.LogLevel level, String text) {
        String s = this.ConstructLine(level, text);
        System.out.println(s);
        if(this.socket != null)
            this.socket.Send(new LogPayload(level, text));
    }

}
