package net.shared.distributed;

import com.esotericsoftware.kryo.Kryo;
import net.shared.distributed.logging.LogPayload;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.network.commands.KillCommand;
import net.shared.distributed.network.commands.NodeShutdownCommand;

public class Registry {

    public static final int TCP_PORT = 3301;
    public static final int UDP_PORT = 3302;

    public static class SerialVersions {
        public static final long LOG_PAYLOAD = 0xFFFF_0001L;
        public static final long KILL_COMMAND = 0xFFFF_0002L;
        public static final long SHUTDOWN_COMMAND = 0xFFFF_0003L;
    }

    public static void RegisterKryoClasses(Kryo kryo) {
        // Native
        kryo.register(String.class);
        kryo.register(Integer.class);
        kryo.register(Character.class);
        kryo.register(Long.class);
        kryo.register(Boolean.class);

        // POJO
        kryo.register(Logger.LogLevel.class);
        kryo.register(LogPayload.class);
        kryo.register(KillCommand.class);
        kryo.register(NodeShutdownCommand.class);
    }

}
