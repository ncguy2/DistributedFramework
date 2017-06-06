package net.shared.distributed.event.node;

import net.shared.distributed.event.AbstractEvent;
import net.shared.distributed.event.Subscribe;

public class NodeShutdownEvent extends AbstractEvent {

    public static interface NodeShutdownListener {
        @Subscribe
        void onNodeShutdown(NodeShutdownEvent event);
    }

}