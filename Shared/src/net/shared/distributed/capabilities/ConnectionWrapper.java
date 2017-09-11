package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.RoutedResponse;
import net.shared.distributed.api.internal.IConnection;

public class ConnectionWrapper implements IConnection<Connection> {

    protected Connection connection;

    public ConnectionWrapper(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Integer GetId() {
        return connection.getID();
    }

    @Override
    public Connection GetConnection() {
        return connection;
    }

    @Override
    public <U> void Send(U data) {
        connection.sendTCP(data);
    }

    @Override
    public <U> void SendRouted(U data) {
        connection.sendTCP(new RoutedResponse<>(data));
    }
}
