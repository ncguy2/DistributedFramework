package net.shared.distributed.node.connection;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.shared.distributed.capabilities.Capabilities;

public class NodeKryoListener extends Listener {

    public NodeKryoListener() {
        super();
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);
        Capabilities.instance().Accept(connection, object);
    }

    @Override
    public void idle(Connection connection) {
        super.idle(connection);

    }
}
