package net.shared.distributed;

import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.commands.NodeShutdownCommand;
import net.shared.distributed.receptor.Receptor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class CoreStart {

    public static Core core;
    public static Distributor distributor;
    public static Receptor receptor;

    public static void main(String[] args) {
        core = new Core();
        distributor = new Distributor(core);
        receptor = new Receptor(core, distributor);

        core.GetHandler().AddTypeHandler(NodeShutdownCommand.class, (cmd, socket) -> {
            Distributor.NodeSocketKey key = new Distributor.NodeSocketKey(socket.getInetAddress(), socket.getPort());
            Logger.instance().Warn("Node shutdown detected from "+key.toString());
            distributor.nodeSockets.remove(key);
        });

        try {
            receptor.StartListening(Registry.NODE_COMM_PORT, CoreStart::NodeCommFunctions);
            receptor.StartListening(Registry.REMOTE_LOG_PORT, CoreStart::LogFunction);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2500);
            distributor.nodeSockets.forEach((key, skt) -> {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(skt.getOutputStream());
                    oos.writeObject(new NodeShutdownCommand());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true);

    }

    public static void NodeCommFunctions(Socket skt, Object obj) {
        Logger.instance().Debug(obj.getClass().getSimpleName()+", "+obj.toString());
        core.GetHandler().AcceptObject(obj, skt);
    }

    public static void LogFunction(Socket skt, Object obj) {
        if(obj instanceof LogPayload) {
            LogPayload payload = (LogPayload)obj;
            Logger.instance().Log(payload.level, payload.text);
        }
    }

}
