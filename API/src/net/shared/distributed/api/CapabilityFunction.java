package net.shared.distributed.api;

public abstract class CapabilityFunction<T, U> {

    protected T packet = null;
    public CapabilityFunction() {}
    public CapabilityFunction<T, U> SetPacket(T pkt) { this.packet = pkt; return this; }

    public CapabilityFunction<T, U> SetPacket_Unsafe(Object pkt) {
        return SetPacket((T) pkt);
    }

    public abstract void Invoke(U conn);

    public static class NoFunction<U> extends CapabilityFunction<Object, U> {
        public NoFunction() {
            super();
        }
        @Override public void Invoke(U conn) {}
    }

}
