package net.shared.distributed.logging;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.Registry;
import net.shared.distributed.capabilities.Capability;
import net.shared.distributed.capabilities.CapabilityFunction;

import java.io.Serializable;

@Capability(name = "Logging", nodeFunction = LogPayload.LogPayloadFunction.class, hostFunction = LogPayload.LogPayloadFunction.class)
public class LogPayload implements Serializable {

    public static long serialVersionUID = Registry.SerialVersions.LOG_PAYLOAD;

    public Logger.LogLevel level;
    public String text;

    public LogPayload() {}

    public LogPayload(Logger.LogLevel level, String text) {
        this.level = level;
        this.text = text;
    }

    public static class LogPayloadFunction extends CapabilityFunction<LogPayload> {

        public LogPayloadFunction() { super(); }

        @Override
        public void Invoke(Connection conn) {
            Logger.instance().Log(packet.level, packet.text);
        }
    }

}
