package com.tophattowl.dungeonsofvetir.game.event;

import com.tophattowl.dungeonsofvetir.game.event.events.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventSubscriptions {
    private final List<EventBus.ListenerHandle<?>> handles = new ArrayList<>();

    public <T extends Event> void on(Class<T> eventType, Consumer<T> listener) {
        handles.add(EventBus.on(eventType, listener));
    }

    public void unsubscribeAll() {
        handles.forEach(EventBus::off);
        handles.clear();
    }
}
