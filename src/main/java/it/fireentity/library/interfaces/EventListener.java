package it.fireentity.library.interfaces;

import java.util.List;

public interface EventListener {

    List<Class<? extends Event>> getListeningEvent();

    void onEvent(Event event);
}
