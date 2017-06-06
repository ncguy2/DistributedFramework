package net.shared.distributed.node;

import com.esotericsoftware.kryonet.Client;
import net.shared.distributed.event.EventBus;
import net.shared.distributed.event.node.NodeShutdownEvent;
import net.shared.distributed.logging.Logger;

import java.io.IOException;

public class NodeCore implements NodeShutdownEvent.NodeShutdownListener {

    protected Client client;

    public NodeCore(Client client) {
        this.client = client;
        EventBus.instance().register(this);
    }

    public void Register() throws IOException {
        client.sendTCP("NODECORE_READY");
    }

    public NodeCore Initialize() throws IOException {
        Logger.instance().Debug("NodeCore initializing");
        Logger.instance().Debug("NodeCore initialized");
        return this;
    }

    @Override
    public void onNodeShutdown(NodeShutdownEvent event) {
        NodeStart.NodeInterface.Kill();
        client.close();
        client.stop();
    }
}
