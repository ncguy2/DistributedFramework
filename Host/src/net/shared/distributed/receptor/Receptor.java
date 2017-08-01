package net.shared.distributed.receptor;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import net.shared.distributed.CoreStart;
import net.shared.distributed.capabilities.CapabilityPacket;
import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.event.NodeConnectedEvent;
import net.shared.distributed.event.NodeDisconnectedEvent;
import net.shared.distributed.logging.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.shared.distributed.Registry.RegisterKryoClasses;

/**
 * Created by Guy on 16/05/2017.
 */
public class Receptor {

    public final Core core;
    public final Distributor distributor;
    protected Map<Integer, Server> portSockets;
    protected Map<Server, Thread> serverThreads;

    public Receptor(Core core, Distributor distributor) {
        this.core = core;
        this.distributor = distributor;
        portSockets = new HashMap<>();
        serverThreads = new HashMap<>();
    }

    public void StartListening(int tcp, int udp, BiConsumer<Connection, Object> received) throws IOException {
        StartListening(tcp, udp, new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                received.accept(connection, object);
            }
        });
    }

    public void StartListening(int tcp, int udp, Listener listener) throws IOException {
        Server server = new Server();
        RegisterKryoClasses(server.getKryo());
        server.start();
        server.bind(tcp);
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Logger.instance().Info(connection.getID() + " connected");
                CoreStart.distributor.nodeSockets.put(connection.getID(), connection);
                CoreStart.distributor.nodeCapabilities.put(connection.getID(), new LinkedHashSet<>());
                listener.connected(connection);

                new NodeConnectedEvent(connection.getID()).Fire();

                CapabilityPacket.Request req = new CapabilityPacket.Request();
                connection.sendTCP(req);
            }

            @Override
            public void disconnected(Connection connection) {
                Logger.instance().Warn(connection.getID() + " disconnected");
                CoreStart.distributor.nodeSockets.remove(connection.getID());
                new NodeDisconnectedEvent(connection.getID()).Fire();
                listener.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object object) {
                listener.received(connection, object);
            }

            @Override
            public void idle(Connection connection) {
                listener.idle(connection);
            }
        });

        portSockets.put(tcp, server);
        serverThreads.put(server, server.getUpdateThread());
    }

    public void StopListening(int port) {
        if(!portSockets.containsKey(port)) return;
        Server s = portSockets.get(port);
        s.stop();
        portSockets.remove(port);
        serverThreads.remove(s);
    }

    public static class ServerSocketWrapper {
        public ServerSocket socket;
        public BiConsumer<Socket, Object> func;
        public boolean isAlive;
        public String name;
        public Thread thread;

        public ServerSocketWrapper(ServerSocket socket, BiConsumer<Socket, Object> func) {
            this(socket, func, "");
        }

        public ServerSocketWrapper(ServerSocket socket, BiConsumer<Socket, Object> func, String name) {
            this.socket = socket;
            this.func = func;
            this.name = name;
            this.thread = new Thread(this::Process, this.name);
            this.isAlive = true;
        }

        public ServerSocketWrapper Start() {
            this.thread.start();
            return this;
        }

        @Deprecated
        public void Process() {
//            ObjectOutputStream oos = null;
//            ObjectInputStream ois = null;
//            Socket skt = null;
//            while(this.isAlive) {
//                try {
//                    skt = socket.accept();
//                    if(skt == null) continue;
//                    Logger.instance().Debug("Socket connected: "+skt.getInetAddress());
//                    Distributor.NodeSocketKey key = new Distributor.NodeSocketKey(skt.getInetAddress(), skt.getPort());
//                    CoreStart.distributor.nodeSockets.put(key, skt);
//
//                    oos = new ObjectOutputStream(skt.getOutputStream());
//                    oos.flush();
//                    Logger.instance().Debug("Output stream constructed");
//                    oos.writeChars("NOTIFICATION:CONNECTED\n");
//
//                    ois = new ObjectInputStream(skt.getInputStream());
//                    Logger.instance().Debug("Input stream constructed");
//
//
//                    Object obj = ois.readObject();
//
//                    oos.writeChars("NOTIFICATION:RECEIVED\n");
//
//                    Logger.instance().Info("Received data from "+skt.getInetAddress()+":"+skt.getPort());
//                    Logger.instance().Debug(obj.toString());
//
//                    this.func.accept(skt, obj);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//
//                        if(oos != null) {
//                            oos.flush();
//                            oos = null;
//                        }
//
//                        if(ois != null) {
//                            ois = null;
//                        }
//
//                        skt = null;
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            try {
//                this.socket.close();
//                this.socket = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        public void Kill() {
            this.isAlive = false;
        }
    }


}
