package it.fireentity.library.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import it.fireentity.library.AbstractPlugin;
import it.fireentity.library.events.GuiClickEvent;
import it.fireentity.library.interfaces.packets.IPacketPlayInWindowClick;
import it.fireentity.library.inventories.GuiPage;
import it.fireentity.library.inventories.PagesHandler;
import it.fireentity.library.nms.ServerVersion;
import it.fireentity.library.nms.VersionBasedFactory;
import it.fireentity.library.nms.packets.PacketPlayInWindowClick_v1_12_R1;
import it.fireentity.library.player.CustomPlayer;

import java.util.Optional;

public class GuiClickListener extends GuiListener {
    private final ClickManager clickManager;
    private final VersionBasedFactory<IPacketPlayInWindowClick> packetPlayInWindowClick;

    public GuiClickListener(AbstractPlugin abstractPlugin, PagesHandler pagesHandler, ClickManager clickManager) {
        super(abstractPlugin, pagesHandler, PacketType.Play.Client.WINDOW_CLICK);
        this.clickManager = clickManager;
        packetPlayInWindowClick = new VersionBasedFactory<IPacketPlayInWindowClick>().register(ServerVersion.v1_12_R1, PacketPlayInWindowClick_v1_12_R1::new);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

        Optional<IPacketPlayInWindowClick> packet = packetPlayInWindowClick.newInstance(getPlugin().getServer());
        if (!packet.isPresent()) {
            return;
        }

        packet.get().setPacket(event.getPacket().getHandle());

        Optional<Integer> slot = packet.get().getSlot();
        if (!slot.isPresent()) {
            return;
        }

        if(slot.get() < 0){
            return;
        }

        Optional<GuiPage> guiPage = getPagesHandler().getGuiPage(event.getPlayer());
        if (!guiPage.isPresent()) {
            return;
        }

        Optional<ServerVersion> serverVersion = ServerVersion.getCurrent(getPlugin().getServer());
        if (!serverVersion.isPresent()) {
            return;
        }

        Optional<CustomPlayer> player = getPlugin().getAPIFireLibrary().getPlayers().getPlayer(event.getPlayer().getName());
        if (!player.isPresent()) {
            return;
        }
        GuiClickEvent clickEvent = new GuiClickEvent(packet.get(), serverVersion.get(), guiPage.get(), player.get(), slot.get());
        if(clickManager.isRegistered(event.getPlayer()) && !clickManager.canClick(event.getPlayer())) {
            player.get().getPlayer().sendMessage(clickManager.getErrorDelayClick());
            clickEvent.setCancelled(true);
        } else {
            getPlugin().getAPIFireLibrary().getEventManager().callEvent(clickEvent);
            clickManager.addPlayer(event.getPlayer());
        }
        event.setCancelled(true);
    }
}
