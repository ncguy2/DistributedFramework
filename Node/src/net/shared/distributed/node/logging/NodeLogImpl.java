package net.shared.distributed.node.logging;

import com.esotericsoftware.kryonet.Client;
import net.shared.distributed.logging.ILogImpl;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;

public class NodeLogImpl implements ILogImpl {

    public Client client;

    public NodeLogImpl(Client client) {
        this.client = client;
        Log(Logger.LogLevel.DEBUG, "Logger initialized");
    }

    @Override
    public void Log(Logger.LogLevel level, String text) {
        String s = this.ConstructLine(level, text);
        System.out.println(s);
        this.client.sendTCP(new LogPayload(level, text));
    }

}
