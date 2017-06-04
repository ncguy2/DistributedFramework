package net.shared.distributed.distributor;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.core.Core;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Guy on 16/05/2017.
 */
public class Distributor {

    public Map<Integer, Connection> nodeSockets;

    public final Core core;

    public Distributor(Core core) {
        this.core = core;
        this.core.SetDistributor(this);
        this.nodeSockets = new HashMap<>();
    }

    public static class NodeSocketKey {
        public InetAddress addr;
        public Integer port;

        public NodeSocketKey(InetAddress addr, Integer port) {
            this.addr = addr;
            this.port = port;
        }

        @Override
        public int hashCode() {
            return addr.hashCode() + port.hashCode();
        }

        @Override
        public String toString() {
            return addr.toString()+":"+port;
        }
    }

    public static class SocketWrapper {
        public Socket socket;
        public ObjectInputStream inputStream;
        public ObjectOutputStream outputStream;
    }

}
