package it.fireentity.library.events;


import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.inventories.GuiPage;

public class GuiOpenEvent extends GuiEvent {

    public GuiOpenEvent(CustomPlayer player, GuiPage guiPage) {
        super(player, guiPage);
    }
}
