package net.shared.distributed.capabilities;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import net.shared.distributed.logging.Logger;
import net.shared.distributed.utils.ReflectionHelper;

import java.util.*;
import java.util.stream.Stream;

public class Capabilities {

    public Map<String, Class<?>> capabilities;
    public Set<Class<?>> packetBlacklist;
    public Map<Class<?>, CapabilityFunction<?>> functionCache;

    public Capability.Side side = Capability.Side.NONE;

    private static Capabilities instance;
    public static Capabilities instance() {
        if (instance == null)
            instance = new Capabilities();
        return instance;
    }

    private Capabilities() {
        capabilities = new HashMap<>();
        functionCache = new HashMap<>();
        packetBlacklist = new LinkedHashSet<>();

        PopulateBlackList();

        ProspectExternalForCapabilities();
        FindCapabilities();
    }

    private void PopulateBlackList() {
        packetBlacklist.add(String.class);
        packetBlacklist.add(FrameworkMessage.KeepAlive.class);
    }

    public boolean IsBlacklisted(Class<?> cls) {
        return packetBlacklist.contains(cls);
    }

    public void SetSide(Capability.Side side) {
        this.side = side;
    }

    public void AddCapability(String name, Class<?> payloadClass) {
        capabilities.put(name, payloadClass);
    }

    public void AddCapability(final Class<?> payloadClass) {
        ReflectionHelper.GetAnnotation(payloadClass, Capability.class)
                .ifPresent(c -> AddCapability(c.name(), payloadClass));
    }

    public void ProspectExternalForCapabilities() {

    }

    public void FindCapabilities() {
        ReflectionHelper.GetAnnotatedTypeStream(Capability.class)
                .forEach(this::AddCapability);
    }

    public <T> void Accept(final Connection conn, final T obj) {
        final Class<?> objCls = obj.getClass();
        Capability cap = objCls.getAnnotation(Capability.class);
        if(IsBlacklisted(objCls)) return;
        if(IsCapable(objCls)) {
            Logger.instance().Debug(cap.name() + " received, invoking...");
            GetFunction(obj.getClass()).SetPacket_Unsafe(obj).Invoke(conn);
        }
        else Logger.instance().Warn("Request received, but is incapable of executing [" + objCls.getSimpleName() + "]");
    }

    public boolean IsCapable(Class<?> cls) {
        return capabilities.values().contains(cls);
    }


    public <T> Class<? extends CapabilityFunction<T>> GetFunctionClass(Class<T> cls) {
        Capability cap = cls.getAnnotation(Capability.class);
        switch(side) {
            case NODE:
                return (Class<? extends CapabilityFunction<T>>) cap.nodeFunction();
            case HOST:
                return (Class<? extends CapabilityFunction<T>>) cap.hostFunction();
        }
        return null;
    }

    public <T> CapabilityFunction<T> GetFunction(Class<T> cls) {
        Class<? extends CapabilityFunction<T>> aClass = GetFunctionClass(cls);
        Optional<? extends CapabilityFunction<T>> build = ReflectionHelper.Build(aClass);
        return build.orElse(null);

        // TODO reimplement cache
//        if(functionCache.containsKey(cls))
//            return (CapabilityFunction<T>) functionCache.get(cls);
//        Capability cap = cls.getAnnotation(Capability.class);
//        Optional<? extends CapabilityFunction> build = ReflectionHelper.Build(cap.function());
//        if(build.isPresent()) {
//            if(cap.cache())
//                functionCache.put(cls, build.get());
//            return build.get();
//        }
//        return null;
    }

    public Set<String> GetNodeCapabilities() {
        return capabilities.keySet();
    }
    public Stream<String> GetNodeCapabilityStream() {
        return GetNodeCapabilities().stream();
    }
}
