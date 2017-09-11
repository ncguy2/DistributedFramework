package net.shared.distributed.logging;

import net.shared.distributed.Registry;
import net.shared.distributed.api.Capability;
import net.shared.distributed.api.logging.Logger;
import net.shared.distributed.capabilities.ConnectionWrapper;
import net.shared.distributed.capabilities.KryoCapabilityFunction;

import java.io.Serializable;

@Capability(name = "Logging", nodeFunction = LogPayload.LogPayloadFunction.class, hostFunction = LogPayload.LogPayloadFunction.class, internal = true)
public class LogPayload implements Serializable {

    public static long serialVersionUID = Registry.SerialVersions.LOG_PAYLOAD;

    public Logger.LogLevel level;
    public String text;

    public LogPayload() {}

    public LogPayload(Logger.LogLevel level, String text) {
        this.level = level;
        this.text = text;
    }

    public static class LogPayloadFunction extends KryoCapabilityFunction<LogPayload> {

        public LogPayloadFunction() { super(); }

        @Override
        public void Invoke(ConnectionWrapper conn) {
            Logger.instance().Log(packet.level, packet.text);
        }
    }

}
