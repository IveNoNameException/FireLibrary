package it.fireentity.library.interfaces;

import it.fireentity.library.events.GuiClickEvent;

@FunctionalInterface
public interface Action {
    void onClick(GuiClickEvent event);
}
