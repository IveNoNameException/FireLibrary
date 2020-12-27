package it.fireentity.library.events;

import it.fireentity.library.interfaces.Event;
import it.fireentity.library.interfaces.EventListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Consumer;

public class EventManager {

    @RequiredArgsConstructor
    @Getter
    private static class EventListeners<T extends Event> {
        private final List<Consumer<T>> listeners;
    }

    private final Map<Class<? extends Event>, EventListeners<? extends Event>> listenerMap = new HashMap<>();

    public <T extends Event> void registerListener(Class<T> clazz, Consumer<T> function) {
        //Check if the event listeners
        if(listenerMap.get(clazz) == null) {
            listenerMap.put(clazz,new EventListeners<>(new ArrayList<>()));
        }

        EventListeners<T> listeners = (EventListeners<T>) listenerMap.get(clazz);
        listeners.getListeners().add(function);
    }

    public <T extends Event> void callEvent(T event) {
        EventListener listener = (EventListener) listenerMap.get(event.getClass());

        if (listener == null) {
            return;
        }
        listener.onEvent(event);
    }
}