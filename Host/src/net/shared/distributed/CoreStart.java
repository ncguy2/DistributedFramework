package net.shared.distributed;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.commands.NodeShutdownCommand;
import net.shared.distributed.receptor.Receptor;

import java.io.IOException;

public class CoreStart {

    public static Core core;
    public static Distributor distributor;
    public static Receptor receptor;

    public static void main(String... args) {
        core = new Core();
        distributor = new Distributor(core);
        receptor = new Receptor(core, distributor);

        core.GetHandler().AddTypeHandler(LogPayload.class, (log, conn) -> Logger.instance().Log(log.level, log.text));

        try {
            receptor.StartListening(Registry.TCP_PORT, Registry.UDP_PORT, CoreStart::NodeCommFunctions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(2500);
            distributor.nodeSockets.forEach((key, conn) -> {
                conn.sendTCP(new NodeShutdownCommand());
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(true);

    }

    public static void NodeCommFunctions(Connection conn, Object obj) {
        Logger.instance().Verbose(obj.getClass().getSimpleName()+", "+obj.toString());
        core.GetHandler().AcceptObject(obj, conn);
    }

}
