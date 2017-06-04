package net.shared.distributed.node;

import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.AsyncNetworkSocket;

import java.io.IOException;
import java.net.InetAddress;

public class NodeCore {

    protected InetAddress coreHost;
    protected int socketPort;
    protected AsyncNetworkSocket socket;

    public NodeCore(InetAddress coreHost, int port) {
        this.coreHost = coreHost;
        this.socketPort = port;
    }

    public void Register() throws IOException {
        socket = new AsyncNetworkSocket(coreHost.getHostAddress(), socketPort);
        socket.SetCallback((aNetSock, response) -> {
            Logger.instance().Debug("Response from core: "+response);
        });
        socket.Send("COMMAND:NODE_REGISTER");
    }

    public NodeCore Initialize() {
        Logger.instance().Debug("NodeCore initialized");
        return this;
    }

    @Override
    protected void finalize() throws Throwable {
        socket.Send("COMMAND:NODE_CLOSING");
        super.finalize();
    }
}
