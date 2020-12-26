package it.fireentity.library.listener;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.storage.GenericCache;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ClickManager implements Listener {
    private final GenericCache<Player, Long> cache = new GenericCache<>();
    private final int delay;
    @Getter private final String errorDelayClick;

    public ClickManager(AbstractPlugin plugin, int delay, String errorDelayClick) {
        this.errorDelayClick = errorDelayClick;
        this.delay = delay;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void addPlayer(Player player) {
        cache.addValue(player, System.currentTimeMillis());
    }

    public boolean canClick(Player player) {
        return cache.getValue(player).map(value -> value < System.currentTimeMillis()-delay).orElse(false);
    }

    public void removePlayer(Player player) {
        cache.removeValue(player);
    }

    public boolean isRegistered(Player player) {
        return cache.getValue(player).isPresent();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }
}
