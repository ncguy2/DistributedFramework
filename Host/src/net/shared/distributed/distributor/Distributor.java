package net.shared.distributed.distributor;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.RoutedResponse;
import net.shared.distributed.capabilities.Capability;
import net.shared.distributed.core.Core;
import net.shared.distributed.event.EventBus;
import net.shared.distributed.event.host.CapabilityResponseEvent;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.utils.ReflectionHelper;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Guy on 16/05/2017.
 */
public class Distributor implements CapabilityResponseEvent.CapabilityResponseListener {


    public Map<Integer, ResponseWrapper> pendingResponses;
    public Map<Integer, Connection> nodeSockets;
    public Map<Integer, Set<String>> nodeCapabilities;
    Random rand = new Random();
    public final Core core;

    public Distributor(Core core) {
        this.core = core;
        this.core.SetDistributor(this);
        this.pendingResponses = new HashMap<>();
        this.nodeSockets = new HashMap<>();
        this.nodeCapabilities = new HashMap<>();
        EventBus.instance().register(this);
    }

    public Optional<Set<Integer>> GetCapableNodeIds(Class<?> pktCls) {
        Optional<Capability> capability = ReflectionHelper.GetAnnotation(pktCls, Capability.class);
        if(!capability.isPresent()) return Optional.empty();
        String name = capability.get().name();
        final Set<Integer> capableNodes = new LinkedHashSet<>();

        nodeCapabilities.forEach((id, strs) -> {
            if(strs.contains(name)) {
                capableNodes.add(id);
                return;
            }
        });
        return Optional.of(capableNodes);
    }

    public <T, U> void Distribute(T target, Class<U> segmentType, Function<DistributionInfo<T>, U> segmentation, Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback) {
        Class<?> dataCls = target.getClass();
        Logger.instance().Debug("Distributor invoked, classtype " + dataCls.getSimpleName());
        Optional<Set<Integer>> capableNodeIds = GetCapableNodeIds(segmentType);
        int hashCode = ReflectionHelper.GetClassHashcode(segmentType);
        if(!capableNodeIds.isPresent()) {
            Logger.instance().Debug("No capable nodes detected");
            return;
        }
        Set<Integer> set = capableNodeIds.get();
        int max = set.size();
        if(max <= 0) {
            Logger.instance().Debug("No capable nodes detected");
            return;
        }

        Map<Integer, U> segmentMap = new HashMap<>();
        int index = 0;

        for (Integer id : set) {
            DistributionInfo<T> info = new DistributionInfo<>();
            info.current = index++;
            info.max = max;
            info.targetData = target;
            Optional<U> apply = Optional.ofNullable(segmentation.apply(info));
            apply.ifPresent(u -> segmentMap.put(id, u));
        }
        SpawnThread(target, hashCode, segmentMap, callbackFactory, callback);
    }

    public <T, U> void SpawnThread(T target, int hashCode, final Map<Integer, U> segmentMap, final Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback) {
        Map<Integer, Object> returnedData = new HashMap<>();
        List<Integer> remainingIds = new ArrayList<>();
        segmentMap.keySet()
                .stream()
                .sorted()
                .forEach(connId -> {
                    remainingIds.add(connId);
                    final Connection conn = nodeSockets.get(connId);
                    conn.sendTCP(segmentMap.get(connId));
        });

        pendingResponses.put(hashCode, new ResponseWrapper<>(target, returnedData, remainingIds, callbackFactory, callback));
    }

    public void HandleRoutedResponse(Connection conn, RoutedResponse response) {
        Object object = response.coreResponse;
        int name = ReflectionHelper.GetHashcode(object);
        if(!pendingResponses.containsKey(name)) return;

        ResponseWrapper wrapper = pendingResponses.get(name);
        int connId = conn.getID();

        System.out.println("Connection received: " + object.toString());
        wrapper.returnedData.put(connId, object);
        wrapper.remainingIds.remove((Integer)connId);
        if (wrapper.remainingIds.isEmpty())
            wrapper.Invoke();
    }

    @Override
    public void onCapabilityResponse(CapabilityResponseEvent event) {
        int id = event.id;
        if(nodeCapabilities.containsKey(id))
            Collections.addAll(nodeCapabilities.get(id), event.response.capabilities);
    }

    public static class ResponseWrapper<T> {

        T originalData;
        Map<Integer, Object> returnedData;
        List<Integer> remainingIds;
        Function<Map<Integer, Object>, T> callbackFactory;
        BiConsumer<T, T> callback;

        public ResponseWrapper(T originalData, Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback) {
            this(originalData, new HashMap<>(), new ArrayList<>(), callbackFactory, callback);
        }

        public ResponseWrapper(T originalData, Map<Integer, Object> returnedData, List<Integer> remainingIds, Function<Map<Integer, Object>, T> callbackFactory, BiConsumer<T, T> callback) {
            this.originalData = originalData;
            this.returnedData = returnedData;
            this.remainingIds = remainingIds;
            this.callbackFactory = callbackFactory;
            this.callback = callback;
        }

        public void Invoke() {
            callback.accept(originalData, callbackFactory.apply(returnedData));
        }

    }

    public static class DistributionInfo<T> {
        public int current;
        public int max;
        public T targetData;
    }

}
