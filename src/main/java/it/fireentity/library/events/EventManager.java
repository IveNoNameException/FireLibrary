package it.fireentity.library.events;

import it.fireentity.library.interfaces.Event;
import it.fireentity.library.interfaces.EventListener;

import java.util.*;

public class EventManager {

    private final Map<Class<? extends Event>, List<EventListener>> listenerMap = new HashMap<>();

    public <T extends Event> void registerListener(EventListener listener) {
        for(Class<? extends Event> event : listener.getListeningEvent()) {
            //Check if the list of listeners is already initialized
            List<EventListener> listeners = this.listenerMap.get(event);

            if (listeners == null) {
                listeners = new ArrayList<>();
                listenerMap.put(event, listeners);
            }

            listeners.add(listener);
        }
    }

    public <T extends Event> void callEvent(T event) {
        EventListener listener = (EventListener) listenerMap.get(event.getClass());

        if (listener == null) {
            return;
        }
        listener.onEvent(event);
    }
}