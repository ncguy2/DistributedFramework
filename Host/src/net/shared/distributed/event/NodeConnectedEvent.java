package net.shared.distributed.event;

public class NodeConnectedEvent extends AbstractEvent {

    public int id;

    public NodeConnectedEvent(int id) {
        this.id = id;
    }

    public static interface NodeConnectedListener {
        @Subscribe
        void onNodeConnected(NodeConnectedEvent event);
    }

}
