package net.shared.distributed;

import com.esotericsoftware.kryo.Kryo;
import net.shared.distributed.capabilities.Capabilities;
import net.shared.distributed.logging.Logger;

import java.util.Collection;

public class Registry {

    public static final int TCP_PORT = 3301;
    public static final int UDP_PORT = 3302;

    public static String name = "UNDEFINED";

    public static class SerialVersions {
        public static final long LOG_PAYLOAD = 0xFFFF_0001L;
        public static final long KILL_COMMAND = 0xFFFF_0002L;
        public static final long SHUTDOWN_COMMAND = 0xFFFF_0003L;
        public static final long CAPABILITY_REQUEST = 0xFFFF_0004L;
        public static final long CAPABILITY_RESPONSE = 0xFFFF_0005L;
        public static final long NAME_REQUEST = 0xFFFF_0006L;
        public static final long NAME_RESPONSE = 0xFFFF_0007L;
    }

    public static void RegisterKryoClasses(Kryo kryo) {
        // Native
        kryo.register(String[].class);

        // POJO
        kryo.register(Logger.LogLevel.class);
        kryo.register(RoutedResponse.class);

        // Dynamic
        Collection<Class<?>> values = Capabilities.instance().capabilities.values();
        values.forEach(kryo::register);

    }

}
