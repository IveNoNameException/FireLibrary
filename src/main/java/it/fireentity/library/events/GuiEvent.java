package it.fireentity.library.events;

import it.fireentity.library.interfaces.Event;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.inventories.GuiPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public abstract class GuiEvent implements Event {
    private final CustomPlayer player;
    private final GuiPage guiPage;
}
