package net.shared.distributed.event.host;

import net.shared.distributed.capabilities.CapabilityPacket;
import net.shared.distributed.event.AbstractEvent;
import net.shared.distributed.event.Subscribe;

public class CapabilityResponseEvent extends AbstractEvent {

    public int id;
    public CapabilityPacket.Response response;

    public CapabilityResponseEvent(int id, CapabilityPacket.Response response) {
        this.id = id;
        this.response = response;
    }

    public static interface CapabilityResponseListener {
        @Subscribe
        void onCapabilityResponse(CapabilityResponseEvent event);
    }

}