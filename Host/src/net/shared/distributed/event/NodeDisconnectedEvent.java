package net.shared.distributed.event;

public class NodeDisconnectedEvent extends AbstractEvent {

    public int id;

    public NodeDisconnectedEvent(int id) {
        this.id = id;
    }

    public static interface NodeDisconnectedListener {
        @Subscribe
        void onNodeDisconnected(NodeDisconnectedEvent event);
    }

}
