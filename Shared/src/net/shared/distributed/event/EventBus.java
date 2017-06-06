package net.shared.distributed.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventBus {

    protected Thread eventThread;
    protected List<Runnable> eventTasks;

    protected void postTask(Runnable runnable) {
        eventTasks.add(runnable);
    }

    //    private List<Object> subscribers;
    private Map<Class<? extends AbstractEvent>, List<AbstractMap.SimpleEntry<Object, Method>>> eventSubscribers;

    private static EventBus instance;
    public static EventBus instance() {
        if (instance == null)
            instance = new EventBus();
        return instance;
    }

    private EventBus() {
        this.eventSubscribers = new HashMap<>();
        eventTasks = new ArrayList<>();
        eventThread = new Thread(this::eventLoop, "Event Bus");
        eventThread.setDaemon(true);
        eventThread.start();
    }

    private void addSubscriber(Class<? extends AbstractEvent> event, Object subscriber, Method method) {
        if(!eventSubscribers.containsKey(event))
            eventSubscribers.put(event, new ArrayList<>());
        eventSubscribers.get(event).add(new AbstractMap.SimpleEntry<>(subscriber, method));
    }

    public void register(final Object subscriber) {
        postTask(() -> {
            for(Method method : subscriber.getClass().getDeclaredMethods()) {
                if(isSubscriber(method)) {
                    Class<?> param0 = method.getParameterTypes()[0];
                    if(AbstractEvent.class.isAssignableFrom(param0))
                        addSubscriber((Class<? extends AbstractEvent>) param0, subscriber, method);
                }
            }
        });
    }

    public void unregister(Object subscriber) {

    }

    public void post(final AbstractEvent event) {
        postTask(() -> {
            final Class eventType = event.getClass();
            if(eventSubscribers.containsKey(eventType)) {
                for (AbstractMap.SimpleEntry<Object, Method> e : eventSubscribers.get(eventType)) {
                    try {
                        e.getValue().invoke(e.getKey(), event);
                    } catch (IllegalAccessException | InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean isSubscriber(Method method) {
        boolean isSub = method.isAnnotationPresent(Subscribe.class);
        if(isSub) return true;

        Class[] interfaces = method.getDeclaringClass().getInterfaces();
        for(Class i : interfaces) {
            try{
                Method interfaceMethod = i.getMethod(method.getName(), method.getParameterTypes());
                if(interfaceMethod != null) {
                    isSub = interfaceMethod.isAnnotationPresent(Subscribe.class);
                    if(isSub) return true;
                }
            }catch (NoSuchMethodException nsme) {

            }
        }
        return false;
    }

    // Concurrency

    protected Runnable getEventTask() {
        if(eventTasks.size() <= 0) return null;
        return eventTasks.remove(0);
    }

    protected void eventLoop() {
        while(true) {
            Runnable task = getEventTask();
            if (task == null) try { Thread.sleep(100); } catch (InterruptedException e) {}
            else task.run();
        }
    }

}
