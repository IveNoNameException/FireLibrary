package it.fireentity.library.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.player.CustomPlayer;
import it.fireentity.library.events.GuiCloseEvent;
import it.fireentity.library.inventories.GuiPage;
import it.fireentity.library.inventories.PagesHandler;
import org.bukkit.Bukkit;

import java.util.Optional;

public class GuiCloseListener extends GuiListener {
    private final PagesHandler pagesHandler;
    private final AbstractPlugin plugin;

    public GuiCloseListener(AbstractPlugin plugin, PagesHandler pagesHandler) {
        super(plugin, pagesHandler, PacketType.Play.Client.CLOSE_WINDOW);
        this.pagesHandler = pagesHandler;
        this.plugin = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Optional<GuiPage> guiPage = pagesHandler.getGuiPage(event.getPlayer());
        if (!guiPage.isPresent()) {
            return;
        }
        Optional<CustomPlayer> player = plugin.getAPIFireLibrary().getPlayers().getPlayer(event.getPlayer().getName());
        player.ifPresent(value -> getPlugin().getEventManager().callEvent(new GuiCloseEvent(value, guiPage.get())));
    }
}
