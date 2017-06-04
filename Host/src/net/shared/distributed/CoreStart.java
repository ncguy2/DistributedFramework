package net.shared.distributed;

import net.shared.distributed.core.Core;
import net.shared.distributed.distributor.Distributor;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.receptor.Receptor;

import java.io.IOException;

public class CoreStart {

    public static void main(String[] args) {
        Core core = new Core();
        Distributor distributor = new Distributor();
        Receptor receptor = new Receptor();

        try {
            receptor.StartListening(Registry.NODE_COMM_PORT, (skt, obj) -> {
                Logger.instance().Debug(obj.getClass().getSimpleName()+", "+obj.toString());
            });
            receptor.StartListening(Registry.REMOTE_LOG_PORT, (skt, obj) -> {
                if(obj instanceof LogPayload) {
                    LogPayload payload = (LogPayload)obj;
                    Logger.instance().Log(payload.level, payload.text);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true);

    }

}
