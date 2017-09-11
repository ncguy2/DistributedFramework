package net.shared.distributed.functions;

import net.shared.distributed.CoreStart;
import net.shared.distributed.api.DistributedFunction;
import net.shared.distributed.api.IDistributedFunction;
import net.shared.distributed.api.internal.IDistributedFunctions;
import net.shared.distributed.utils.ReflectionHelper;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DistributedFunctions implements IDistributedFunctions {

    private static DistributedFunctions instance;
    public static DistributedFunctions instance() {
        if (instance == null)
            instance = new DistributedFunctions();
        return instance;
    }

    protected Reflections ref;
    public Map<Class<?>, Class<?>> functions;

    private DistributedFunctions() {
        ref = new Reflections();
        functions = new HashMap<>();
        FindFunctions();
    }

    protected void FindFunctions() {
        Set<Class<?>> typesAnnotatedWith = ref.getTypesAnnotatedWith(DistributedFunction.class);
        typesAnnotatedWith.forEach(this::AddFunctionClass);
    }

    @Override
    public void AddFunctionClass(Class<?> cls) {
        Optional<DistributedFunction> df = ReflectionHelper.GetAnnotation(cls, DistributedFunction.class);
        df.ifPresent(distributedFunction -> functions.put(distributedFunction.value(), cls));
    }

    public Optional<Class<?>> GetFunctionClass(Class<?> cls) {
        return Optional.of(functions.get(cls));
    }

    public Optional<IDistributedFunction> GetFunction(Class<?> cls) {
        Optional<Class<?>> aClass = GetFunctionClass(cls);
        if(aClass.isPresent()) {
            Optional<?> build = ReflectionHelper.BuildExpression(aClass.get(), CoreStart.distributor);
            if(build.isPresent()) {
                Object o = build.get();
                if(o instanceof IDistributedFunction) {
                    return Optional.of((IDistributedFunction) o);
                }
            }
        }
        return Optional.empty();
    }


}
