package net.shared.distributed.api;

import net.shared.distributed.api.internal.DistributionInfo;
import net.shared.distributed.api.internal.IDistributor;

import java.util.Map;
import java.util.function.BiConsumer;

public abstract class IDistributedFunction<T, U> {

    protected IDistributor distributor;
    protected BiConsumer<T, T> callback = null;

    public IDistributedFunction(IDistributor distributor) {
        this.distributor = distributor;
    }

    public void Invoke(T t) {
        distributor.Distribute(t, GetSegmentClass(), this::SegmentPayload, this::ReformData, this::UseData);
    }

    public abstract Class<U> GetSegmentClass();

    public abstract U SegmentPayload(DistributionInfo<T> info);
    public abstract T ReformData(Map<Integer, Object> data);

    public void UseData(T original, T processed) {
        if(callback != null)
            callback.accept(original, processed);
    }
}
