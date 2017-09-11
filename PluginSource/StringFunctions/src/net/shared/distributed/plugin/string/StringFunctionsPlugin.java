package net.shared.distributed.plugin.string;

import net.shared.distributed.api.DistributedPlugin;
import net.shared.distributed.plugin.string.tolower.ToLower;
import net.shared.distributed.plugin.string.tolower.ToLowerFunction;
import net.shared.distributed.plugin.string.toupper.ToUpper;
import net.shared.distributed.plugin.string.toupper.ToUpperFunction;

import java.util.List;
import java.util.function.Consumer;

public class StringFunctionsPlugin implements DistributedPlugin {

    @Override
    public String Name() {
        return "String functions";
    }

    @Override
    public void RegisterPackets(List<Class<?>> clsList) {
        clsList.add(ToUpper.class);
        clsList.add(ToLower.class);
    }

    @Override
    public void RegisterFunctions(Consumer<Class<?>> consumer) {
        consumer.accept(ToUpperFunction.class);
        consumer.accept(ToLowerFunction.class);
    }

}
