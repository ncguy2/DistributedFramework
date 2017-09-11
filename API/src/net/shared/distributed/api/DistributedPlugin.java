package net.shared.distributed.api;

import java.util.List;
import java.util.function.Consumer;

/**
 * The core interface for external plugins
 */
public interface DistributedPlugin {

    /**
     * @return The plugin name, for diagnostic purposes
     */
    String Name();

    /**
     * Register explicit function capability classes,
     * classes tagged with @Capability are already registered
     * @param clsList
     */
    void RegisterPackets(List<Class<?>> clsList);

    /**
     * Register function classes
     * @param consumer The consumer to register with
     */
    void RegisterFunctions(Consumer<Class<?>> consumer);

}
