package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.api.CapabilityFunction;

public abstract class KryoCapabilityFunction<T> extends CapabilityFunction<T, Connection> {

    public static class NoFunction extends CapabilityFunction.NoFunction<Connection> {}

}
