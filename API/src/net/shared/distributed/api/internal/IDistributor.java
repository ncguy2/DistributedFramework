package net.shared.distributed.api.internal;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A basic interface for the distributor, allows for the API to have a loose reference to the distributor without having to include it in the API
 */
public interface IDistributor {

    /**
     * Gets the node ids that have the capability to use pktCls
     * @param pktCls
     * @return
     */
    Optional<Set<Integer>> GetCapableNodeIds(Class<?> pktCls);

    /**
     * Segments the data and distributes the packets to capable nodes
     * @param target The target data
     * @param segmentType The resulting segment type
     * @param segmentation The segmentation function
     * @param callbackFactory The function to reassemble the resulting data
     * @param callback The function to invoke with the whole payload
     * @param <T> The target data type
     * @param <U> The segment data type
     */
    <T, U> void Distribute(T target, Class<U> segmentType, Function<DistributionInfo<T>, U> segmentation, Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback);

    /**
     * Spawns a distribution thread for each segment, and registers the request for later access
     * @param target The target data
     * @param hashCode The internal hash code
     * @param segmentMap The ordered segments
     * @param callbackFactory The function to reassemble the resulting data
     * @param callback The function to invoke with the whole payload
     * @param <T> The target data type
     * @param <U> The segment data type
     */
    <T, U> void SpawnThread(T target, int hashCode, final Map<Integer, U> segmentMap, final Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback);

}