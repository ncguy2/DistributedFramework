package net.shared.distributed.api.internal;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IDistributor {

    Optional<Set<Integer>> GetCapableNodeIds(Class<?> pktCls);

    <T, U> void Distribute(T target, Class<U> segmentType, Function<DistributionInfo<T>, U> segmentation, Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback);
    <T, U> void SpawnThread(T target, int hashCode, final Map<Integer, U> segmentMap, final Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback);

}