package net.shared.distributed.receptor;

import net.shared.distributed.CoreStart;
import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Created by Guy on 16/05/2017.
 */
public class Receptor {

    public final Core core;
    public final Distributor distributor;
    protected Map<Integer, ServerSocketWrapper> portSockets;

    public Receptor(Core core, Distributor distributor) {
        this.core = core;
        this.distributor = distributor;
        portSockets = new HashMap<>();
    }

    public void StartListening(int port, BiConsumer<Socket, Object> func) throws IOException {
        ServerSocket skt = new ServerSocket(port);
        ServerSocketWrapper wrapper = new ServerSocketWrapper(skt, func);
        portSockets.put(port, wrapper.Start());
    }

    public void StopListening(int port) {
        if(!portSockets.containsKey(port)) return;
        ServerSocketWrapper ssw = portSockets.get(port);
        ssw.Kill();
        portSockets.remove(port);
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

        public void Process() {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            Socket skt = null;
            while(this.isAlive) {
                try {
                    skt = socket.accept();
                    if(skt == null) continue;
                    Logger.instance().Debug("Socket connected: "+skt.getInetAddress());
                    Distributor.NodeSocketKey key = new Distributor.NodeSocketKey(skt.getInetAddress(), skt.getPort());
                    CoreStart.distributor.nodeSockets.put(key, skt);

                    oos = new ObjectOutputStream(skt.getOutputStream());
                    oos.flush();
                    Logger.instance().Debug("Output stream constructed");
                    oos.writeChars("NOTIFICATION:CONNECTED\n");

                    ois = new ObjectInputStream(skt.getInputStream());
                    Logger.instance().Debug("Input stream constructed");


                    Object obj = ois.readObject();

                    oos.writeChars("NOTIFICATION:RECEIVED\n");

                    Logger.instance().Info("Received data from "+skt.getInetAddress()+":"+skt.getPort());
                    Logger.instance().Debug(obj.toString());

                    this.func.accept(skt, obj);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {

                        if(oos != null) {
                            oos.flush();
                            oos = null;
                        }

                        if(ois != null) {
                            ois = null;
                        }

                        skt = null;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                this.socket.close();
                this.socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void Kill() {
            this.isAlive = false;
        }
    }


}
