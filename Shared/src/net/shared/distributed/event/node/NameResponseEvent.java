package net.shared.distributed.event.node;

import net.shared.distributed.event.AbstractEvent;
import net.shared.distributed.event.Subscribe;

public class NameResponseEvent extends AbstractEvent {

    public int id;
    public String name;

    public NameResponseEvent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static interface NameResponseListener {
        @Subscribe
        void onNameResponse(NameResponseEvent event);
    }

}

