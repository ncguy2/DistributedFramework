package net.shared.distributed.event;

public class EventHandledEvent<T extends AbstractEvent> extends AbstractEvent {

    public T handledEvent;
    public Class<T> cls;

    public EventHandledEvent(T handledEvent, Class<T> cls) {
        this.handledEvent = handledEvent;
        this.cls = cls;
    }

    public static interface EventHandledListener<T extends AbstractEvent> {

        @Subscribe
        void OnEventHandled(EventHandledEvent<T> event);
    }

}
