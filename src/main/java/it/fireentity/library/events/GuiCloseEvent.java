package it.fireentity.library.events;

import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.inventories.GuiPage;
import lombok.Getter;

@Getter
public class GuiCloseEvent extends GuiEvent {

    public GuiCloseEvent(CustomPlayer player, GuiPage guiPage) {
        super(player, guiPage);
    }
}
