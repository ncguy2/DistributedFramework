package net.shared.distributed;

import com.esotericsoftware.kryo.Kryo;
import net.shared.distributed.api.internal.IDistributedFunctions;
import net.shared.distributed.api.logging.Logger;
import net.shared.distributed.capabilities.Capabilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Registry {

    public static final int TCP_PORT;
    public static final int UDP_PORT;

    public static IDistributedFunctions functionHost = null;

    static {
        TCP_PORT = 3301;
        UDP_PORT = 3302;
    }

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
        System.out.println("Registry.RegisterKryoClasses");

        // Native
        kryo.register(String[].class);

        // POJO
        kryo.register(Logger.LogLevel.class);
        kryo.register(RoutedResponse.class);

        kryo.setRegistrationRequired(false);

        List<Class<?>> clsList = new ArrayList<>();

        // Dynamic
        Capabilities.instance().capabilities.values()
                .stream()
                .sorted(Comparator.comparing(Class::getCanonicalName))
                .forEach(clsList::add);

        Capabilities.instance().loader.plugins.stream().sorted().forEach(plugin -> plugin.RegisterPackets(clsList));

        clsList.stream().distinct().forEach(c -> Register(kryo, c));

//        pluginRegistration.entrySet().stream().sorted().forEach(entry -> {
//
//            DistributedPlugin key = entry.getKey();
//            List<Class<?>> cls = entry.getValue();
//            Logger.instance().Debug("Plugin %s registering classes:", key.Name());
//
//            cls.stream().sorted(Comparator.comparing(Class::getCanonicalName)).forEach(c -> Register(kryo, c));
//        });
        kryo.setRegistrationRequired(true);

    }

    protected static void Register(Kryo kryo, Class<?> cls) {
        kryo.register(cls);
        Logger.instance().Debug("\t- %s now registered", cls.getCanonicalName());
    }

}
