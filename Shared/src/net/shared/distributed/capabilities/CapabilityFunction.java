package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;

public abstract class CapabilityFunction<T> {

    protected T packet = null;
    public CapabilityFunction() {}
    public CapabilityFunction<T> SetPacket(T pkt) { this.packet = pkt; return this; }

    public CapabilityFunction<T> SetPacket_Unsafe(Object pkt) {
        return SetPacket((T) pkt);
    }

    public abstract void Invoke(Connection conn);

    public static class NoFunction extends CapabilityFunction<Object> {
        public NoFunction() {
            super();
        }
        @Override public void Invoke(Connection conn) {}
    }

}
