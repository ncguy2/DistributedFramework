package net.shared.distributed.node.operation;

import net.shared.distributed.api.logging.Logger;

import java.util.Map;
import java.util.function.Consumer;

public class NodeOperator {

    public Map<String, Object> operatorData;
    public final String name;
    private final Consumer<Map<String, Object>> func;

    public NodeOperator(String name, Consumer<Map<String, Object>> func, Map<String, Object> operatorData) {
        this.name = name;
        this.func = func;
        this.operatorData = operatorData;
        Logger.instance().Info(name+" Spawned");
    }

    public void Invoke() {
        Logger.instance().Info(name+" invoked");
        func.accept(operatorData);
    }

}
