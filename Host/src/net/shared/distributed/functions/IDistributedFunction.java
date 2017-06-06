package net.shared.distributed.functions;

import net.shared.distributed.distributor.Distributor;

import java.util.Map;

public abstract class IDistributedFunction<T, U> {

    protected Distributor distributor;

    public IDistributedFunction(Distributor distributor) {
        this.distributor = distributor;
    }

    public void Invoke(T t) {
        distributor.Distribute(t, GetSegmentClass(), this::SegmentPayload, this::ReformData, this::UseData);
    }

    public abstract Class<U> GetSegmentClass();

    public abstract U SegmentPayload(Distributor.DistributionInfo<T> info);
    public abstract T ReformData(Map<Integer, Object> data);
    public abstract void UseData(T original, T processed);
}
