package net.shared.distributed.network.commands;

import com.esotericsoftware.kryonet.Connection;
import net.shared.distributed.Registry;
import net.shared.distributed.capabilities.Capability;
import net.shared.distributed.capabilities.CapabilityFunction;
import net.shared.distributed.event.node.NodeShutdownEvent;

import java.io.Serializable;

@Capability(name = "shutdown", nodeFunction = NodeShutdownCommand.NodeShutdownFunction.class)
public class NodeShutdownCommand implements Serializable {

    public static final long serialVersionUID = Registry.SerialVersions.KILL_COMMAND;

    public NodeShutdownCommand() {
    }

    public static class NodeShutdownFunction extends CapabilityFunction<NodeShutdownCommand> {

        @Override
        public void Invoke(Connection conn) {
            new NodeShutdownEvent().Fire();
        }
    }

}
