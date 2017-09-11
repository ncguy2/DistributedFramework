package net.shared.distributed.api;

import net.shared.distributed.api.internal.IConnection;

/**
 * Basic function framework for capabilities
 * @param <T> The target data type
 * @param <U> The connection object
 */
public abstract class CapabilityFunction<T, U extends IConnection> {

    protected T packet = null;

    public CapabilityFunction() {}

    /**
     * Type-safe set function
     * @param pkt The new packet data
     * @return this instance for method chaining
     */
    public CapabilityFunction<T, U> SetPacket(T pkt) { this.packet = pkt; return this; }

    /**
     * Type-unsafe set function, uses raw cast to T
     * @param pkt The new packet data
     * @return this instance for method chaining
     */
    public CapabilityFunction<T, U> SetPacket_Unsafe(Object pkt) {
        return SetPacket((T) pkt);
    }

    /**
     * The capability invocation function
     * @param conn The connection instance
     */
    public abstract void Invoke(U conn);

    /**
     * Default function is to do nothing
     * @param <U> The connection object
     */
    public static class NoFunction<U extends IConnection> extends CapabilityFunction<Object, U> {
        public NoFunction() {
            super();
        }
        @Override public void Invoke(U conn) {}
    }

}
