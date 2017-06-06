package net.shared.distributed.event;

public abstract class AbstractEvent {

    public void Fire() {
        EventBus.instance().post(this);
    }

}
