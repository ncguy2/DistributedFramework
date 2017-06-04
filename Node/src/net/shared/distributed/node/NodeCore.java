package net.shared.distributed.node;

import net.shared.distributed.TypeFunctionHandler;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.AsyncNetworkSocket;
import net.shared.distributed.network.commands.KillCommand;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NodeCore {

    protected TypeFunctionHandler handler;
    protected InetAddress coreHost;
    protected int socketPort;
    protected AsyncNetworkSocket socket;

    public NodeCore(InetAddress coreHost, int port) {
        this.coreHost = coreHost;
        this.socketPort = port;
    }

    private static void Accept(KillCommand __, Socket ___) {
        NodeStart.NodeInterface.Kill();
    }

    public void Register() throws IOException {
        socket = new AsyncNetworkSocket(coreHost.getHostAddress(), socketPort);
        socket.SetCallback((aNetSock, response) -> {
            Logger.instance().Debug("Response from core: "+response);
        });
        socket.Send("COMMAND:NODE_REGISTER");
    }

    public NodeCore Initialize() {
        Logger.instance().Debug("NodeCore initializing");
        handler = new TypeFunctionHandler();
        handler.AddTypeHandler(KillCommand.class, NodeCore::Accept);
        Logger.instance().Debug("NodeCore initialized");
        return this;
    }

}
