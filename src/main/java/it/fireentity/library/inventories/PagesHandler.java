package it.fireentity.library.inventories;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.events.GuiClickEvent;
import it.fireentity.library.interfaces.Event;
import it.fireentity.library.interfaces.EventListener;
import it.fireentity.library.storage.Cache;
import it.fireentity.library.storage.GenericCache;
import it.fireentity.library.events.GuiCloseEvent;
import it.fireentity.library.events.GuiOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PagesHandler implements EventListener {
    private final GenericCache<Player, GuiPage> openedInventories = new GenericCache<>();
    private final Cache<String, GuiPage> pages = new Cache<>();

    public PagesHandler(AbstractPlugin abstractPlugin) {
        Bukkit.getPluginManager().registerEvents(this, abstractPlugin);
    }

    public int totalPages() {
        return pages.getValues().size();
    }

    public Optional<GuiPage> getGuiPage(String guiPage) {
        return pages.getValue(guiPage);
    }

    public Collection<GuiPage> getPages() {
        return pages.getValues();
    }

    public void addPage(GuiPage guiPage) {
        pages.addValue(guiPage);
    }

    public Optional<GuiPage> getGuiPage(Player player) {
        return openedInventories.getValue(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        openedInventories.removeValue(event.getPlayer());
    }

    @Override
    public List<Class<? extends Event>> getListeningEvent() {
        List<Class<? extends Event>> classes = new ArrayList<>();
        classes.add(GuiClickEvent.class);
        classes.add(GuiCloseEvent.class);
        classes.add(GuiOpenEvent.class);
        return classes;
    }

    @Override
    public void onEvent(Event event) {
        if(event instanceof GuiClickEvent) {
            GuiClickEvent guiClickEvent = ((GuiClickEvent) event);
            if (!openedInventories.getValue(guiClickEvent.getPlayer().getPlayer()).isPresent()) {
                return;
            }
            if (this.getPages().contains(guiClickEvent.getGuiPage())) {
                guiClickEvent.getGuiPage().getInventory().onPlayerClick(guiClickEvent);
            }
            return;
        }

        if(event instanceof GuiCloseEvent) {
            GuiCloseEvent guiCloseEvent = (GuiCloseEvent) event;
            openedInventories.removeValue(guiCloseEvent.getPlayer().getPlayer());
            guiCloseEvent.getPlayer().closeInventory();
            return;
        }

        if(event instanceof GuiOpenEvent) {
            GuiOpenEvent guiOpenEvent = (GuiOpenEvent) event;
            openedInventories.addValue(guiOpenEvent.getPlayer().getPlayer(), guiOpenEvent.getGuiPage());
        }
    }
}