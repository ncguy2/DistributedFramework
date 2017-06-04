package net.shared.distributed.node;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.shared.distributed.TypeFunctionHandler;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.commands.KillCommand;

import java.io.IOException;

public class NodeCore {

    protected TypeFunctionHandler handler;
    protected Client client;

    public NodeCore(Client client) {
        this.client = client;
    }

    public void Register() throws IOException {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                handler.AcceptObject(object, connection);
            }
        });
        client.sendTCP("NODECORE_CONNECT");
    }

    public NodeCore Initialize() throws IOException {
        Logger.instance().Debug("NodeCore initializing");
        handler = new TypeFunctionHandler();
        handler.AddTypeHandler(KillCommand.class, NodeCore::Accept);
        Logger.instance().Debug("NodeCore initialized");
        return this;
    }

    private static void Accept(KillCommand __, Connection ___) {
        NodeStart.NodeInterface.Kill();
    }


}
