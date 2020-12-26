package it.fireentity.library.player;

import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.nms.ServerVersion;
import it.fireentity.library.nms.VersionBasedFactory;
import it.fireentity.library.storage.Cache;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayerCache implements Listener {
    private final VersionBasedFactory<CustomPlayer> player;
    private final Cache<String, CustomPlayer> players = new Cache<>();
    private final Server version;

    public PlayerCache(AbstractPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        player = new VersionBasedFactory<CustomPlayer>().register(ServerVersion.v1_12_R1, Player_v1_12_R1::new);
        version = plugin.getServer();
    }

    public Optional<CustomPlayer> getPlayer(String player) {
        return players.getValue(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Optional<CustomPlayer> customPlayer = player.newInstance(version);
        customPlayer.ifPresent(player -> {
            player.setPlayer(event.getPlayer());
            players.addValue(player);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.removeValue(event.getPlayer().getName());
    }
}
