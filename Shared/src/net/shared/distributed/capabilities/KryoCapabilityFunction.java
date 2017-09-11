package net.shared.distributed.capabilities;

import net.shared.distributed.api.CapabilityFunction;

public abstract class KryoCapabilityFunction<T> extends CapabilityFunction<T, ConnectionWrapper> {

    public static class NoFunction extends CapabilityFunction.NoFunction<ConnectionWrapper> {}

}
