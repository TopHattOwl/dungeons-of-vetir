package com.tophattowl.dungeonsofvetir.game.event;

import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    private final Map<Class<?>, List<ListenerHandle<?>>> listeners = new HashMap<>();

    public EventBus() {}

    /**
     * Registers a listener for an event type
     * @return a handle to unsubscribe later if needed
     */
    public static <T> ListenerHandle<T> on(Class<T> eventType, Consumer<T> listener) {
        ListenerHandle<T> handle = new ListenerHandle<>(eventType, listener);
        INSTANCE.listeners
            .computeIfAbsent(eventType, k -> new ArrayList<>())
            .add(handle);
        return handle;
    }

    /**
     * Unregisters a listener for an event type
     * @param handle The handle returned by the `on` method
     */
    public static void off(ListenerHandle<?> handle) {
        List<ListenerHandle<?>> list = INSTANCE.listeners.get(handle.eventType);
        if (list != null) list.remove(handle);
    }

    @SuppressWarnings("unchecked")
    public static <T> void emit(T event) {
        List<ListenerHandle<?>> list = INSTANCE.listeners.get(event.getClass());
        if (list == null) return;
        // copy list before iterating, dummy!!!
        // a listener might unsubscribe during emit
        for (ListenerHandle<?> handle : new ArrayList<>(list)) {
            DebugLogger.log(DebugLogger.Category.EVENT, "EventBus",
                "emitting event: " + event.getClass().getSimpleName()
            );
            ((Consumer<T>) handle.listener).accept(event);
        }
    }

    public static void clear() {
        INSTANCE.listeners.clear();
    }


    public static class ListenerHandle<T> {
        final Class<T> eventType;
        final Consumer<T> listener;

        ListenerHandle(Class<T> eventType, Consumer<T> listener) {
            this.eventType = eventType;
            this.listener = listener;
        }
    }
}
